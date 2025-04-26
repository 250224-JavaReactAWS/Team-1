package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.BookingDTO;
import com.Rev.RevStay.exceptions.RoomNotAvailableException;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.services.BookingService;

import org.springframework.http.ResponseEntity;

/**
 * REST controller for managing booking-related operations.
 * 
 * This controller provides endpoints for:
 * - Making a reservation.
 * - Retrieving bookings by hotel ID.
 * - Retrieving bookings by user.
 * - Updating the status of a booking.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/bookings` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 * - `@ExceptionHandler`: Handles specific exceptions for the controller.
 */
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Exception handler for RoomNotAvailableException.
     * 
     * @param ex The exception to handle.
     * @return A ResponseEntity with a conflict status and the exception message.
     */
    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<String> handleRoomNotAvailableException(RoomNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Constructor for BookingController.
     * 
     * @param bookingService The service layer for booking-related operations.
     */
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Endpoint for making a reservation.
     * 
     * @param bookingRequest The booking details to create.
     * @param session        The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the created BookingDTO or an error
     *         status.
     */
    @PostMapping("/reserve")
    public ResponseEntity<BookingDTO> makeReservation(@RequestBody Booking bookingRequest, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<BookingDTO> bookingResponse = bookingService.makeReservation(bookingRequest, userId);
        return bookingResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for retrieving bookings by hotel ID.
     * 
     * @param hotelId The ID of the hotel to retrieve bookings for.
     * @return A ResponseEntity containing a list of BookingDTOs for the specified
     *         hotel.
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingDTO>> getBookingByHotelId(@PathVariable int hotelId) {
        List<BookingDTO> bookings = bookingService.getBookingsByHotelId(hotelId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Endpoint for retrieving bookings by user.
     * 
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity containing a list of BookingDTOs for the user.
     */
    @GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<BookingDTO> bookings = bookingService.getBookingsByUser(userId);

        // Mark bookings as completed if their check-out date has passed.
        LocalDateTime today = LocalDateTime.now();
        for (BookingDTO booking : bookings) {
            if (booking.getCheckOut() != null && booking.getCheckOut().isBefore(today)
                    && !"COMPLETED".equals(booking.getStatus())) {
                bookingService.markBookingAsCompleted(booking.getBookingId());
            }
        }

        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    /**
     * Endpoint for updating the status of a booking.
     * 
     * @param bookingId     The ID of the booking to update.
     * @param bookingStatus The new status to set for the booking.
     * @param session       The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the updated BookingDTO or a forbidden
     *         status.
     */
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable int bookingId,
            @RequestParam BookingStatus bookingStatus,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        String roleStr = (String) session.getAttribute("role");

        if (userId == null || roleStr == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookingDTO updatedBooking = bookingService.updateBookingStatus(bookingId, bookingStatus, userId);
        return ResponseEntity.ok(updatedBooking);
    }
}
