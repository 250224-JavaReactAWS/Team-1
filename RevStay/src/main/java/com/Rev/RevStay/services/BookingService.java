package com.Rev.RevStay.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.Rev.RevStay.exceptions.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.BookingStatus;
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

    public Optional<Booking> makeReservation(Booking booking) {
        // Validate booking details
        if (booking.getHotel() == null || booking.getCheckIn() == null || 
            booking.getCheckOut() == null || booking.getRoom() == null || 
            booking.getGuests() <= 0) {
            throw new IllegalArgumentException("Invalid reservation details provided.");
        }

        // Check room availability
        if (!isRoomAvailable(booking.getHotel(), booking.getCheckIn(), 
                             booking.getCheckOut(), booking.getRoom().getRoomId())) {
            throw new IllegalStateException("No rooms of the specified type are available for the given dates at the specified hotel.");
        }

        // Add hotel and room details to the reservation
        String hotelDetails = getHotelDetails(booking.getHotel());
        String roomDetails = getRoomDetails(booking.getHotel(), booking.getRoom());

        if (hotelDetails == null || roomDetails == null) {
            throw new IllegalStateException("Hotel or room details could not be retrieved.");
        }

        // Call the DAO to handle database operations
        return Optional.of(bookingDAO.save(booking));
    }

    public boolean isRoomAvailable(Hotel hotel, LocalDateTime checkInDate, LocalDateTime checkOutDate, int roomId) {
        // Call the DAO to check room availability
        return bookingDAO.isRoomAvailable(hotel.getHotelId(), checkInDate, checkOutDate, roomId);
    }

    private String getHotelDetails(Hotel hotel) {
        // Logic to fetch hotel details (stubbed for now)
        return "Hotel details for " + hotel;
    }

    private String getRoomDetails(Hotel hotel, Room roomType) {
        // Logic to fetch room details (stubbed for now)
        return "Room details for " + roomType + " in " + hotel;
    }
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingDAO.findByUserId(userId);
    }

    public Booking updateBookingStatus(Long bookingId, BookingStatus cancelled, Integer userId) {
        Optional<Booking> bookingOptional = bookingDAO.findById(bookingId);

        boolean validStatus = cancelled.equals("cancelled") || cancelled.equals("completed");
        if(!validStatus) {
            throw new GenericException("Invalid status: " + cancelled);
        }


        //TODO check if the user is the owner of the booking with the id in session

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            if(bookingOptional.get().getUserId()==(int)(userId)) {
                if (cancelled.equals("cancelled")) {
                    booking.setStatusCancelled();

                }
            }else if((int)(userId)==booking.getHotel().getOwner().getUserId()) {
                if (cancelled.equals("completed")) {
                    booking.setStatusCompleted();
                }
            }

            return bookingDAO.save(booking);
        } else {
            throw new GenericException("Booking not found with id: ");
        }
    }
}
