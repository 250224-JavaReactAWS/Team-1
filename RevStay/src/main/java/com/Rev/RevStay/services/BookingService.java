package com.Rev.RevStay.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.RoomDAO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {
    
    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    
    @Autowired
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO){
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
    }

    public Optional<Booking> makeReservation(Hotel hotel, LocalDateTime checkInDate, LocalDateTime checkOutDate, Room roomType, int numberOfGuests) {
        // Logic to make a hotel reservation
        if (hotel == null || checkInDate == null || checkOutDate == null || roomType == null || numberOfGuests <= 0) {
            throw new IllegalArgumentException("Invalid reservation details provided.");
        }

        // Check room availability
        if (!isRoomAvailable(hotel, checkInDate, checkOutDate, roomType)) {
            throw new IllegalStateException("No rooms of the specified type are available for the given dates at the specified hotel.");
        }

        // Add hotel and room details to the reservation
        String hotelDetails = getHotelDetails(hotel);
        String roomDetails = getRoomDetails(hotel, roomType);

        if (hotelDetails == null || roomDetails == null) {
            throw new IllegalStateException("Hotel or room details could not be retrieved.");
        }

        // Call the DAO to handle database operations
        return bookingDAO.saveReservation(hotel, checkInDate, checkOutDate, roomType, numberOfGuests);
        
    }

    public boolean isRoomAvailable(Hotel hotel, LocalDateTime checkInDate, LocalDateTime checkOutDate, Room roomType) {
        // Call the DAO to check room availability
        return roomDAO.checkRoomAvailability(hotel, checkInDate, checkOutDate, roomType);
    }

    private String getHotelDetails(Hotel hotel) {
        // Logic to fetch hotel details (stubbed for now)
        return "Hotel details for " + hotel;
    }

    private String getRoomDetails(Hotel hotel, Room roomType) {
        // Logic to fetch room details (stubbed for now)
        return "Room details for " + roomType + " in " + hotel;
    }
}
