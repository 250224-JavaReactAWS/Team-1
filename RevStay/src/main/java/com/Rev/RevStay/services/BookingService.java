package com.Rev.RevStay.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.Rev.RevStay.DTOS.BookingDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.exceptions.RoomNotAvailableException;
import com.Rev.RevStay.models.*;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.RoomDAO;

import jakarta.transaction.Transactional;

/**
 * Service class for managing booking-related operations such as creating,
 * updating,
 * retrieving, and checking room availability.
 * 
 * This class provides methods to:
 * - Make a reservation.
 * - Check room availability.
 * - Retrieve bookings by user ID.
 * - Retrieve bookings by hotel ID.
 * - Update the status of a booking.
 * - Mark a booking as completed, confirmed, or accepted.
 * 
 * It uses `BookingDAO`, `RoomDAO`, `UserDAO`, and `HotelDAO` for database
 * interactions.
 * 
 * Exceptions:
 * - Throws `GenericException` or `RoomNotAvailableException` for invalid
 * inputs, unauthorized actions,
 * or when required entities (hotel, room, or user) are not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 * - `@Transactional`: Ensures that database operations are executed within a
 * transaction.
 */
@Service
@Transactional
public class BookingService {

    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;

    /**
     * Constructor for BookingService.
     * 
     * @param bookingDAO Data access object for booking-related operations.
     * @param roomDAO    Data access object for room-related operations.
     * @param userDAO    Data access object for user-related operations.
     * @param hotelDAO   Data access object for hotel-related operations.
     */
    @Autowired
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO, UserDAO userDAO, HotelDAO hotelDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
    }

    /**
     * Makes a reservation for a user.
     * 
     * @param booking The booking details.
     * @param userId  The ID of the user making the reservation.
     * @return An Optional containing the created BookingDTO.
     * @throws GenericException if the hotel, room, or user does not exist, or if
     *                          the room is not available.
     */
    public Optional<BookingDTO> makeReservation(Booking booking, int userId) {
        System.out.println("Hotel: " + booking.getHotel());
        System.out.println("CheckIn: " + booking.getCheckIn());
        System.out.println("CheckOut: " + booking.getCheckOut());
        System.out.println("Room: " + booking.getRoom());
        System.out.println("Guests: " + booking.getGuests());
        if (booking.getHotel() == null || booking.getCheckIn() == null ||
                booking.getCheckOut() == null || booking.getRoom() == null ||
                booking.getGuests() <= 0) {
            throw new IllegalArgumentException("Invalid reservation details provided.");
        }

        Hotel hotel = hotelDAO.findById(booking.getHotel().getHotelId())
                .orElseThrow(() -> new GenericException("Hotel not found"));

        Room room = roomDAO.findById(booking.getRoom().getRoomId())
                .orElseThrow(() -> new GenericException("Room not found"));

        if (!isRoomAvailable(booking.getHotel(), booking.getCheckIn(), booking.getCheckOut(),
                booking.getRoom().getRoomId())) {
            throw new RoomNotAvailableException("Room not available for selected dates.");
        }

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new GenericException("User not found"));

        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatusPending();

        return Optional.of(convertToDTO(bookingDAO.save(booking)));
    }

    /**
     * Checks if a room is available for the given dates.
     * 
     * @param hotel        The hotel containing the room.
     * @param checkInDate  The check-in date.
     * @param checkOutDate The check-out date.
     * @param roomId       The ID of the room to check.
     * @return True if the room is available, false otherwise.
     */
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

    /**
     * Retrieves all bookings made by a specific user.
     * 
     * @param userId The ID of the user.
     * @return A list of BookingDTOs for the bookings made by the user.
     */
    public List<BookingDTO> getBookingsByUser(int userId) {
        return bookingDAO.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Retrieves all bookings associated with a specific hotel.
     * 
     * @param hotelId The ID of the hotel.
     * @return A list of BookingDTOs for the bookings associated with the hotel.
     */
    public List<BookingDTO> getBookingsByHotelId(int hotelId) {
        return bookingDAO.findByHotel_HotelId(hotelId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Updates the status of a booking.
     * 
     * @param bookId The ID of the booking to be updated.
     * @param status The new status of the booking.
     * @param userId The ID of the user attempting to update the booking.
     * @return The updated BookingDTO.
     * @throws GenericException if the booking does not exist, the status is
     *                          invalid, or the user is not authorized.
     */
    public BookingDTO updateBookingStatus(int bookId, BookingStatus status, Integer userId) {
        Booking booking = bookingDAO.findById(bookId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookId));

        boolean validStatus = status == BookingStatus.CANCELLED || status == BookingStatus.ACCEPTED
                || status == BookingStatus.CONFIRMED || status == BookingStatus.COMPLETED;
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

    /**
     * Marks a booking as completed.
     * 
     * @param bookingId The ID of the booking to be marked as completed.
     * @throws GenericException if the booking does not exist.
     */
    public void markBookingAsCompleted(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            booking.setStatusCompleted();
            bookingDAO.save(booking);
        }
    }

    /**
     * Marks a booking as confirmed.
     * 
     * @param bookingId The ID of the booking to be marked as confirmed.
     * @throws GenericException if the booking does not exist.
     */
    public void markBookingAsConfirmed(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            booking.setStatusConfirmed();
            bookingDAO.save(booking);
        }
    }

    /**
     * Marks a booking as accepted.
     * 
     * @param bookingId The ID of the booking to be marked as accepted.
     * @throws GenericException if the booking does not exist.
     */
    public void markBookingAsAccepted(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new GenericException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            booking.setStatusAccepted();
            bookingDAO.save(booking);
        }
    }

    /**
     * Converts a Booking entity to a BookingDTO.
     * 
     * @param booking The Booking entity to be converted.
     * @return The corresponding BookingDTO.
     */
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
                booking.getUser().getEmail());
    }

}
