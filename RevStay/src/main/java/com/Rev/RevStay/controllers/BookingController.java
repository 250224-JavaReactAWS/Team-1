package com.Rev.RevStay.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.services.BookingService;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
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

}
