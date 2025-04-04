package com.Rev.RevStay.controllers;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("hotels")

public class HotelController {

    private final HotelService hotelService;

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
    public ResponseEntity<Hotel> updateHotel(@PathVariable int hotelId, @RequestAttribute User owner,  @RequestBody Hotel updatedHotel) {
        Optional<Hotel> updated = Optional.of(hotelService.updateHotel(hotelId, owner, updatedHotel));
        
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
