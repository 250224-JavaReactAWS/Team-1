package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.DTOS.HotelSearchRequest;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelDAO hotelDAO;
    private final UserDAO userDAO;
    private final BookingDAO bookingDAO;

    @Autowired
    public HotelService(HotelDAO hotelDAO, UserDAO userDAO, BookingDAO bookingDAO) { this.hotelDAO = hotelDAO;
        this.userDAO = userDAO;
        this.bookingDAO = bookingDAO;
    }

    // Get all hotels
    public List<HotelDTO> getAllHotels() {
        return hotelDAO.findAll().stream()
                .map(this::convertToDTO)
                .toList();

    }

    // Get hotel by ID
    public Optional<HotelDTO> getById(int hotelId) {
        return hotelDAO.findById(hotelId).map(this::convertToDTO);
    }

    //Get hotels by favorite by UserId
    public List<HotelDTO> findFavoriteHotelsByUserId(int userId){
        List<Hotel> favoriteHotels = hotelDAO.findFavoriteHotelsByUserId(userId);
        return favoriteHotels.stream()
                .map(this::convertToDTO).toList();
    }

    //Get hotels by User Id
    public List<HotelDTO> findHotelByUserId(int userId){
        List<Hotel> ownerHotels = hotelDAO.findHotelsByOwnerId(userId);
        return ownerHotels.stream()
                .map(this::convertToDTO).toList();
    }

    //Get hotels by amenities

    public List<HotelDTO> filterHotels(HotelSearchRequest request) {
        String locationQuery = request.getLocation() != null ? request.getLocation().trim().toLowerCase() : "";
        List<String> requiredAmenities = request.getAmenities() != null ? request.getAmenities() : List.of();
        LocalDateTime checkIn = request.getCheckIn();
        LocalDateTime checkOut = request.getCheckOut();

        return hotelDAO.findAll().stream()
                .filter(hotel -> {
                    // Filter by location
                    boolean matchesLocation = locationQuery.isEmpty() ||
                            (hotel.getLocation() != null &&
                                    Arrays.stream(hotel.getLocation().split(","))
                                            .map(this::normalize)
                                            .anyMatch(part -> normalize(locationQuery).contains(part) || part.contains(normalize(locationQuery))));

                    // Filter by amenities
                    List<String> hotelAmenities = hotel.getAmenities().stream()
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();

                    boolean matchesAmenities = requiredAmenities.isEmpty() ||
                            requiredAmenities.stream()
                                    .map(String::toLowerCase)
                                    .allMatch(hotelAmenities::contains);

                    // Filter by dates if they are provided
                    boolean hasAvailableRoom = hotel.getRooms().stream()
                            .anyMatch(room -> bookingDAO.isRoomAvailable(hotel.getHotelId(), checkIn, checkOut, room.getRoomId()));

                    return matchesLocation && matchesAmenities && hasAvailableRoom;
                })
                .map(this::convertToDTO)
                .toList();
    }

    private String normalize(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase().trim();
    }


    // Update an existing hotel
    public HotelDTO updateHotel(int hotelId, int ownerId, Hotel updatedHotel) {
        Optional<Hotel> existingHotelOpt = hotelDAO.findById(hotelId);
        Optional<User> ownerOpt = userDAO.findById(ownerId);

        if (existingHotelOpt.isEmpty()) {
            throw new IllegalArgumentException("Hotel with ID " + hotelId + " does not exist.");
        }

        if (ownerOpt.isEmpty()) {
            throw new IllegalArgumentException("Owner with ID " + ownerId + " does not exist.");
        }

        Hotel existingHotel = existingHotelOpt.get();
        User owner = ownerOpt.get();

        if (!existingHotel.getOwner().equals(owner)) {
            throw new IllegalArgumentException("Owner ID does not match the hotel's owner ID.");
        }

        updatedHotel.setHotelId(hotelId);
        updatedHotel.setOwner(owner);

        Hotel savedHotel = hotelDAO.save(updatedHotel);
        return convertToDTO(savedHotel);
    }


    // Delete a hotel by ID
    public void deleteHotel(int hotelId, int ownerId) {
        Optional<Hotel> existingHotel = hotelDAO.findById(hotelId);
        Optional<User> owner = userDAO.findById(ownerId);

        if (existingHotel.isPresent() && owner.isPresent()) {
            if (existingHotel.get().getOwner() == owner.get()) {
                hotelDAO.deleteById(hotelId);
            } else {
                throw new IllegalArgumentException("Owner ID does not match the hotel's owner ID.");
            }
        } else {
            throw new IllegalArgumentException("Hotel with ID " + hotelId + " does not exist.");
        }
    }

    public Optional<HotelDTO> createHotel(Hotel hotel, int userId) {
        Optional<Hotel> potentialHotel = hotelDAO.findHotelByName(hotel.getName());

        if (potentialHotel.isPresent()) {
            throw new GenericException("Hotel with name: " + hotel.getName() + " already exists!");
        }

        Optional<User> owner = userDAO.findById(userId);
        if (owner.isPresent()) {
            hotel.setOwner(owner.get());
        } else {
            throw new IllegalArgumentException("No owner found with id: " + userId);
        }

        return Optional.of(convertToDTO(hotelDAO.save(hotel)));
    }

    public boolean hasPermission(int hotelId, int userId) {
        Optional<Hotel> hotelOpt = hotelDAO.findById(hotelId);
        Optional<User> userOpt = userDAO.findById(userId);

        if (hotelOpt.isEmpty() || userOpt.isEmpty()) {
            return false; // Hotel or user does not exist
        }

        Hotel hotel = hotelOpt.get();
        User user = userOpt.get();

        // Check if the user is the owner of the hotel or has the "OWNER" role
        return hotel.getOwner().getUserId() == userId || "OWNER".equals(user.getUserType());
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        return new HotelDTO(
                hotel.getHotelId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                hotel.getAmenities() != null ? String.join(",", hotel.getAmenities()) : "",
                hotel.getPriceRange(),
                hotel.getImages(),
                hotel.getOwner().getEmail(),
                hotel.getOwner().getFullName()
        );
    }
}
