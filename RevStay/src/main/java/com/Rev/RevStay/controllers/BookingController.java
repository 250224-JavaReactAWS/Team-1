package com.Rev.RevStay.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.services.BookingService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("bookings")
public class BookingController {
    
    private BookingService bookingService;

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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    //update booking status
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(updatedBooking);
    }
}
