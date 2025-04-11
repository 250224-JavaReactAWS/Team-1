package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelDAO hotelDAO;
    private final UserDAO userDAO;

    @Autowired
    public HotelService(HotelDAO hotelDAO, UserDAO userDAO) { this.hotelDAO = hotelDAO;
        this.userDAO = userDAO;
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
    public List<Hotel> findFavoriteHotelsByUserId(int userId){ return hotelDAO.findFavoriteHotelsByUserId(userId); }

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


    private HotelDTO convertToDTO(Hotel hotel) {
        return new HotelDTO(
                hotel.getHotelId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                hotel.getAmenities() != null ? String.join(",", hotel.getAmenities()) : "",
                hotel.getPriceRange(),
                hotel.getImages(),
                hotel.getCreatedAt(),
                hotel.getOwner().getEmail()
        );
    }
}
