package com.Rev.RevStay.controllers;

import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("rooms")

public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Create a new Room
    @PostMapping
    public ResponseEntity<Room> registerRoom(@RequestBody Room room, @RequestAttribute User owner) {
        Optional<Room> createdRoom = roomService.register(room, owner);
        return createdRoom.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // Delete Room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int roomId, @RequestAttribute User owner) {
        try {
            roomService.deleteRoom(roomId, owner);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update Room
    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable int roomId, @RequestBody Room updatedRoom, @RequestAttribute User owner) {
        Optional<Room> updated = roomService.updateRoom(roomId, updatedRoom, owner);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get rooms by Hotel Id
    @GetMapping("/hotel/{hotelId}")
    public List<Room> getRoomsByHotelId(@PathVariable int hotelId) {
        return roomService.getRoomsByHotelId(hotelId);
    }

}
