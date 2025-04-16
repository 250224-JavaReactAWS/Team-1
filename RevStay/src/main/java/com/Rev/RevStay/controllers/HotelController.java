package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.DTOS.HotelSearchRequest;
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
import java.util.Map;
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

    @GetMapping("/favoritesUser")
    public ResponseEntity<List<HotelDTO>> getHotelFavoriteByUserId(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (session.getAttribute("role").equals("OWNER")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<HotelDTO> favorites = hotelService.findFavoriteHotelsByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<HotelDTO>> getHotelByOwner(HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (session.getAttribute("role").equals("USER")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<HotelDTO> ownerHotels = hotelService.findHotelByUserId(userId);
        return ResponseEntity.ok(ownerHotels);
    }

    //get Hotels by amenities
    @PostMapping("/search")
    public ResponseEntity<List<HotelDTO>> searchHotels(@RequestBody HotelSearchRequest request) {
        List<HotelDTO> filteredHotels = hotelService.filterHotels(request);
        return ResponseEntity.ok(filteredHotels);
    }

    // Update hotel
    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable int hotelId, @RequestBody Hotel updatedHotel, HttpSession session) {
        Optional<HotelDTO> updated = Optional.of(hotelService.updateHotel(hotelId, (Integer) session.getAttribute("userId"), updatedHotel));
        
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

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<String> deleteHotel(@PathVariable int hotelId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            hotelService.deleteHotel(hotelId, userId);
            return ResponseEntity.ok("Hotel deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

}
