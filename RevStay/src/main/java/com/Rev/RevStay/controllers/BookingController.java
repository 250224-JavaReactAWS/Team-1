package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.BookingDTO;
import com.Rev.RevStay.models.UserType;
import com.Rev.RevStay.repos.BookingDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.services.BookingService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {
    
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    //make a reservation
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

    // Get bookings by hotel ID
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingDTO>> getBookingByHotelId(@PathVariable int hotelId) {
        List<BookingDTO> bookings = bookingService.getBookingsByHotelId(hotelId);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by user
    @GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        // String roleStr = (String) session.getAttribute("role");
        // System.out.println(userId);
        // System.out.println(roleStr);
        // System.out.println(UserType.valueOf(roleStr).equals(UserType.USER));
            


        if (userId == null ) {
            System.out.println(userId);
            // System.out.println(roleStr);
            // System.out.println(UserType.valueOf(roleStr).equals(UserType.USER));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<BookingDTO> bookings = bookingService.getBookingsByUser(userId);

        // Check if bookings are completed
        LocalDateTime today = LocalDateTime.now();
        for (BookingDTO booking : bookings) {
            if (booking.getCheckOut() != null && booking.getCheckOut().isBefore(today) && !"COMPLETED".equals(booking.getStatus())) {
                bookingService.markBookingAsCompleted(booking.getBookingId());
            }
        }

        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    // Update booking status
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
