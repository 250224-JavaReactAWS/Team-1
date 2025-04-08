package com.Rev.RevStay.controllers;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.services.HotelService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("hotels")

public class HotelController {

    private final HotelService hotelService;
    private final Logger logger = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<Hotel> getAllHotelsHandler() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelByIdHandler(@PathVariable int hotelId) {
        Optional<Hotel> hotel = hotelService.getById(hotelId);

        //Return 404 Not Found
        return hotel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update hotel
    @PutMapping("/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable int hotelId, @RequestBody Hotel updatedHotel, HttpSession session) {
        Optional<Hotel> updated = Optional.of(hotelService.updateHotel(hotelId, (Integer) session.getAttribute("userId"), updatedHotel));
        
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/register")
    public ResponseEntity<Hotel> registerHotel(@RequestBody Hotel hotelToBeRegistered, HttpSession session){

        if (!"OWNER".equals(session.getAttribute("role"))) {
            ResponseEntity.status(403).build();
            throw new GenericException("User does not have the OWNER role.");
        }
        
        Optional<Hotel> newHotel = hotelService.createHotel(hotelToBeRegistered, (Integer) session.getAttribute("userId"));

        newHotel.ifPresent(value -> logger.info("Hotel created with Id: {}", value.getHotelId()));

        return newHotel.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build() );
        
    }

}
