package com.Rev.RevStay.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.Rev.RevStay.DTOS.BookingDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.*;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.RoomDAO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {
    
    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;
    
    @Autowired
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO, UserDAO userDAO, HotelDAO hotelDAO){
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
    }

    public Optional<BookingDTO> makeReservation(Booking booking, int userId) {
        if (booking.getHotel() == null || booking.getCheckIn() == null ||
                booking.getCheckOut() == null || booking.getRoom() == null ||
                booking.getGuests() <= 0) {
            throw new IllegalArgumentException("Invalid reservation details provided.");
        }

        Hotel hotel = hotelDAO.findById(booking.getHotel().getHotelId())
                .orElseThrow(() -> new GenericException("Hotel not found"));

        Room room = roomDAO.findById(booking.getRoom().getRoomId())
                .orElseThrow(() -> new GenericException("Room not found"));

        if (!isRoomAvailable(hotel, booking.getCheckIn(), booking.getCheckOut(), room.getRoomId())) {
            throw new IllegalStateException("Room not available for selected dates.");
        }

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new GenericException("User not found"));

        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatusPending();

        return Optional.of(convertToDTO(bookingDAO.save(booking)));
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

    public List<BookingDTO> getBookingsByUser(int userId) {
        return bookingDAO.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<BookingDTO> getBookingsByHotelId(int hotelId) {
        return bookingDAO.findByHotel_HotelId(hotelId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public BookingDTO updateBookingStatus(int bookId, BookingStatus status, Integer userId) {
        Booking booking = bookingDAO.findById(bookId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookId));

        boolean validStatus = status == BookingStatus.CANCELLED || status == BookingStatus.ACCEPTED || status == BookingStatus.CONFIRMED || status == BookingStatus.COMPLETED;
        if (!validStatus) {
            throw new GenericException("Invalid status: " + status);
        }

        if (booking.getUser().getUserId() == (userId) && status == BookingStatus.CANCELLED) {
            booking.setStatusCancelled();
        } else if (booking.getHotel().getOwner().getUserId() == (userId) && status == BookingStatus.ACCEPTED) {
            booking.setStatusAccepted();
        } else {
            throw new GenericException("Not authorized to change booking status.");
        }

        return convertToDTO(bookingDAO.save(booking));
    }

    public void markBookingAsCompleted(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            booking.setStatusCompleted();
            bookingDAO.save(booking);
        }
    }



    private BookingDTO convertToDTO(Booking booking) {
        return new BookingDTO(
                booking.getBookId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getGuests(),
                booking.getStatus().toString(),
                booking.getHotel().getHotelId(),
                booking.getHotel().getName(),
                booking.getRoom().getRoomId(),
                booking.getRoom().getRoomType(),
                booking.getUser().getEmail()
        );
    }

}
