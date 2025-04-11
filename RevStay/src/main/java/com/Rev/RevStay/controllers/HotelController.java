package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.services.HotelService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("hotels")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HotelController {

    private final HotelService hotelService;
    private final Logger logger = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelDTO> getAllHotelsHandler() { return hotelService.getAllHotels(); }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelByIdHandler(@PathVariable int hotelId) {
        Optional<HotelDTO> hotel = hotelService.getById(hotelId);

        //Return 404 Not Found
        return hotel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("favoritesUser")
    public List<Hotel> getHotelFavoriteByUserId(HttpSession session){
        int userId = (Integer) session.getAttribute("userId");
        return hotelService.findFavoriteHotelsByUserId(userId);
    }

    // Update hotel
    @PutMapping("/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable int hotelId, @RequestBody Hotel updatedHotel, HttpSession session) {
        Optional<Hotel> updated = Optional.of(hotelService.updateHotel(hotelId, (Integer) session.getAttribute("userId"), updatedHotel));
        
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping ("/register")
    public ResponseEntity<HotelDTO> registerHotel(@RequestBody Hotel hotelToBeRegistered, HttpSession session){

        if (!"OWNER".equals(session.getAttribute("role"))) {
            ResponseEntity.status(403).build();
            throw new GenericException("User does not have the OWNER role.");
        }
        
        Optional<HotelDTO> newHotel = hotelService.createHotel(hotelToBeRegistered, (Integer) session.getAttribute("userId"));

        newHotel.ifPresent(value -> logger.info("Hotel created with Id: {}", value.getHotelId()));

        return newHotel.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build() );
        
    }

}
