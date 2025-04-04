package com.Rev.RevStay.services;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelDAO hotelDAO;

    @Autowired
    public HotelService(HotelDAO hotelDAO) { this.hotelDAO = hotelDAO; }

    // Get all hotels
    public List<Hotel> getAllHotels() {
        return hotelDAO.findAll();
    }

    // Get hotel by ID
    public Optional<Hotel> getById(int hotelId) {
        return hotelDAO.findById(hotelId);
    }

    // Update an existing hotel
    public Hotel updateHotel(int hotelId,User owner, Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelDAO.findById(hotelId);
        if (existingHotel.isPresent()) {
            if (existingHotel.get().getOwner() == owner) {
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
    public void deleteHotel(int hotelId, User owner) {
        Optional<Hotel> existingHotel = hotelDAO.findById(hotelId);
        if (existingHotel.isPresent()) {
            if (existingHotel.get().getOwner() == owner) {
                hotelDAO.deleteById(hotelId);
            } else {
                throw new IllegalArgumentException("Owner ID does not match the hotel's owner ID.");
            }
        } else {
            throw new IllegalArgumentException("Hotel with ID " + hotelId + " does not exist.");
        }
    }
}
