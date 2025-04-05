package com.Rev.RevStay.services;

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
    public List<Hotel> getAllHotels() {
        return hotelDAO.findAll();
    }

    // Get hotel by ID
    public Optional<Hotel> getById(int hotelId) {
        return hotelDAO.findById(hotelId);
    }

    // Update an existing hotel
    public Hotel updateHotel(int hotelId, int ownerId, Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelDAO.findById(hotelId);
        Optional<User> owner = userDAO.findById(ownerId);

        if (existingHotel.isPresent() && owner.isPresent()) {
            if (existingHotel.get().getOwner() == owner.get()) {
                updatedHotel.setHotelId(hotelId);
                return hotelDAO.save(updatedHotel);
            } else {
                throw new IllegalArgumentException("Owner ID does not match the hotel's owner ID.");
            }
        } else {
            throw new IllegalArgumentException("Hotel with ID " + hotelId + " does not exist.");
        }
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

    public Optional<Hotel> createHotel(Hotel hotel, int userId){
        Optional<Hotel> potentialHotel = hotelDAO.findHotelByName(hotel.getName());

        if (potentialHotel.isPresent()) {
            throw new GenericException("Hotel with name: " + hotel.getName() + " already exists!");
        }

        Optional<User> owner = userDAO.findById(userId);

        if (owner.isPresent()) {
            hotel.setOwner(owner.get());
        }else{
            throw new IllegalArgumentException("No owner found with id: " + userId);
        }

       return Optional.of(hotelDAO.save(hotel));
    }
}
