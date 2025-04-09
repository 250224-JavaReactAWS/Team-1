package com.Rev.RevStay.controllers;

import com.Rev.RevStay.models.UserType;
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
@RequestMapping("bookings")
public class BookingController {
    
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    //make a reservation
    @PostMapping("/reserve")
    public ResponseEntity<Optional<Booking>> makeReservation(@RequestBody Booking bookingRequest) {
        Optional<Booking> bookingResponse = bookingService.makeReservation(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Booking>> getBookingByHotelId(@PathVariable int hotelId){
        List<Booking> bookings = bookingService.getBookingsByHotelId(hotelId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getBookingsByUser(HttpSession session) {
        int userId = (Integer) session.getAttribute("userId");
        UserType userType = (UserType) session.getAttribute("role");

        if (userType != UserType.USER) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be USER");
        }

        List<Booking> bookings = bookingService.getBookingsByUser(userId);

        //Check in the bookings if the checkOut pass, set the status for booking as completed
        LocalDateTime today = LocalDateTime.now();
        for (Booking booking : bookings){
            //Check the Date
            if (booking.getCheckOut() != null && booking.getCheckOut().isBefore(today)){
                //The checkOut date pass
                if (booking.getStatus() != BookingStatus.COMPLETED){
                    bookingService.updateBookingStatus(booking.getBookId(), BookingStatus.COMPLETED, userId);
                }
            }
        }

        return ResponseEntity.ok(bookings);
    }

    //update booking status
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable int bookingId, @RequestParam BookingStatus bookingStatus, HttpSession session) {

        UserType userType = (UserType) session.getAttribute("role");

        if (userType != UserType.OWNER) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be OWNER");
        }

        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, bookingStatus, (Integer) session.getAttribute("userId"));
        return ResponseEntity.ok(updatedBooking);
    }
}
