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

/**
 * Service class for managing hotel-related operations such as retrieving,
 * creating,
 * updating, and deleting hotels, as well as filtering hotels based on various
 * criteria.
 * 
 * This class provides methods to:
 * - Retrieve all hotels.
 * - Retrieve a hotel by its ID.
 * - Retrieve hotels favorited by a user.
 * - Retrieve hotels owned by a user.
 * - Filter hotels based on location, amenities, and availability.
 * - Create a new hotel.
 * - Update an existing hotel.
 * - Delete a hotel.
 * - Check if a user has permission to manage a hotel.
 * 
 * It uses `HotelDAO`, `UserDAO`, and `BookingDAO` for database interactions.
 * 
 * Exceptions:
 * - Throws `GenericException` or `IllegalArgumentException` for invalid inputs,
 * unauthorized actions,
 * or when required entities (hotel or user) are not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 */
@Service
public class HotelService {

    private final HotelDAO hotelDAO;
    private final UserDAO userDAO;
    private final BookingDAO bookingDAO;

    /**
     * Constructor for HotelService.
     * 
     * @param hotelDAO   Data access object for hotel-related operations.
     * @param userDAO    Data access object for user-related operations.
     * @param bookingDAO Data access object for booking-related operations.
     */
    @Autowired
    public HotelService(HotelDAO hotelDAO, UserDAO userDAO, BookingDAO bookingDAO) {
        this.hotelDAO = hotelDAO;
        this.userDAO = userDAO;
        this.bookingDAO = bookingDAO;
    }

    /**
     * Retrieves all hotels.
     * 
     * @return A list of HotelDTOs for all hotels.
     */
    public List<HotelDTO> getAllHotels() {
        return hotelDAO.findAll().stream()
                .map(this::convertToDTO)
                .toList();

    }

    /**
     * Retrieves a hotel by its ID.
     * 
     * @param hotelId The ID of the hotel to be retrieved.
     * @return An Optional containing the HotelDTO for the specified hotel.
     */
    public Optional<HotelDTO> getById(int hotelId) {
        return hotelDAO.findById(hotelId).map(this::convertToDTO);
    }

    /**
     * Retrieves hotels favorited by a specific user.
     * 
     * @param userId The ID of the user.
     * @return A list of HotelDTOs for the hotels favorited by the user.
     */
    public List<HotelDTO> findFavoriteHotelsByUserId(int userId) {
        List<Hotel> favoriteHotels = hotelDAO.findFavoriteHotelsByUserId(userId);
        return favoriteHotels.stream()
                .map(this::convertToDTO).toList();
    }

    /**
     * Retrieves hotels owned by a specific user.
     * 
     * @param userId The ID of the user.
     * @return A list of HotelDTOs for the hotels owned by the user.
     */
    public List<HotelDTO> findHotelByUserId(int userId) {
        List<Hotel> ownerHotels = hotelDAO.findHotelsByOwnerId(userId);
        return ownerHotels.stream()
                .map(this::convertToDTO).toList();
    }

    /**
     * Filters hotels based on location, amenities, and availability.
     * 
     * @param request The HotelSearchRequest containing the filter criteria.
     * @return A list of HotelDTOs for the hotels matching the criteria.
     */
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
                                            .anyMatch(part -> normalize(locationQuery).contains(part)
                                                    || part.contains(normalize(locationQuery))));

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
                            .anyMatch(room -> bookingDAO.isRoomAvailable(hotel.getHotelId(), checkIn, checkOut,
                                    room.getRoomId()));

                    return matchesLocation && matchesAmenities && hasAvailableRoom;
                })
                .map(this::convertToDTO)
                .toList();
    }

    private String normalize(String input) {
        if (input == null)
            return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase().trim();
    }

    /**
     * Creates a new hotel.
     * 
     * @param hotel  The hotel to be created.
     * @param userId The ID of the user creating the hotel.
     * @return An Optional containing the created HotelDTO.
     * @throws GenericException if a hotel with the same name already exists or the
     *                          user is not found.
     */
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

    /**
     * Updates an existing hotel.
     * 
     * @param hotelId      The ID of the hotel to be updated.
     * @param ownerId      The ID of the owner attempting to update the hotel.
     * @param updatedHotel The updated hotel details.
     * @return The updated HotelDTO.
     * @throws IllegalArgumentException if the hotel or owner does not exist, or the
     *                                  owner is not authorized.
     */
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

    /**
     * Deletes a hotel by its ID.
     * 
     * @param hotelId The ID of the hotel to be deleted.
     * @param ownerId The ID of the owner attempting to delete the hotel.
     * @throws IllegalArgumentException if the hotel or owner does not exist, or the
     *                                  owner is not authorized.
     */
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

    /**
     * Checks if a user has permission to manage a hotel.
     * 
     * @param hotelId The ID of the hotel.
     * @param userId  The ID of the user.
     * @return True if the user has permission, false otherwise.
     */
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

    /**
     * Converts a Hotel entity to a HotelDTO.
     * 
     * @param hotel The Hotel entity to be converted.
     * @return The corresponding HotelDTO.
     */
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
                hotel.getOwner().getFullName());
    }
}
