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

/**
 * REST controller for managing room-related operations.
 * 
 * This controller provides endpoints for:
 * - Creating a new room.
 * - Updating an existing room.
 * - Deleting a room.
 * - Retrieving rooms by hotel ID.
 * - Retrieving a room by its ID.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/rooms` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 */
@RestController
@RequestMapping("rooms")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RoomController {

    private final RoomService roomService;

    /**
     * Constructor for RoomController.
     * 
     * @param roomService The service layer for room-related operations.
     */
    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Endpoint for creating a new room.
     * 
     * @param room    The room details to create.
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the created RoomDTO or a bad request
     *         status.
     */
    @PostMapping
    public ResponseEntity<RoomDTO> registerRoom(@RequestBody Room room, HttpSession session) {
        Optional<RoomDTO> createdRoom = roomService.register(room, (Integer) session.getAttribute("userId"));
        return createdRoom.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a room.
     * 
     * @param roomId  The ID of the room to delete.
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId, HttpSession session) {
        try {
            roomService.deleteRoom(roomId, (Integer) session.getAttribute("userId"));
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for updating an existing room.
     * 
     * @param roomId      The ID of the room to update.
     * @param updatedRoom The updated room details.
     * @param session     The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the updated RoomDTO or a not found
     *         status.
     */
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable int roomId, @RequestBody Room updatedRoom,
            HttpSession session) {
        Optional<RoomDTO> updated = roomService.updateRoom(roomId, updatedRoom,
                (Integer) session.getAttribute("userId"));
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for retrieving rooms by hotel ID.
     * 
     * @param hotelId The ID of the hotel to retrieve rooms for.
     * @return A list of RoomDTOs for the specified hotel.
     */
    @GetMapping("/hotel/{hotelId}")
    public List<RoomDTO> getRoomsByHotelId(@PathVariable int hotelId) {
        return roomService.getRoomsByHotelId(hotelId);
    }

    /**
     * Endpoint for retrieving a room by its ID.
     * 
     * @param roomId The ID of the room to retrieve.
     * @return A ResponseEntity containing the RoomDTO or a not found status.
     */
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
