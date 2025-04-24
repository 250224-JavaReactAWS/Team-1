package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.RoomDTO;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.services.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("rooms")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Create a new Room
    @PostMapping
    public ResponseEntity<RoomDTO> registerRoom(@RequestBody Room room, HttpSession session) {
        Optional<RoomDTO> createdRoom = roomService.register(room, (Integer) session.getAttribute("userId"));
        return createdRoom.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    // Delete Room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId, HttpSession session) {
        try {
            roomService.deleteRoom(roomId, (Integer) session.getAttribute("userId"));
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update Room
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable int roomId, @RequestBody Room updatedRoom, HttpSession session) {
        Optional<RoomDTO> updated = roomService.updateRoom(roomId, updatedRoom, (Integer) session.getAttribute("userId"));
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get rooms by Hotel Id
    @GetMapping("/hotel/{hotelId}")
    public List<RoomDTO> getRoomsByHotelId(@PathVariable int hotelId) {
        return roomService.getRoomsByHotelId(hotelId);
    }

    // Get Room by Id
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable int roomId) {
        RoomDTO room = roomService.getRoomById(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
