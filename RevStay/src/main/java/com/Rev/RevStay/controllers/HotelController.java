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
import java.util.Optional;

/**
 * REST controller for managing hotel-related operations.
 * 
 * This controller provides endpoints for:
 * - Retrieving all hotels.
 * - Retrieving a hotel by its ID.
 * - Checking permissions for a hotel.
 * - Retrieving favorite hotels for a user.
 * - Retrieving hotels owned by a user.
 * - Searching hotels by criteria.
 * - Registering a new hotel.
 * - Updating an existing hotel.
 * - Deleting a hotel.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/hotels` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 */
@RestController
@RequestMapping("hotels")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HotelController {

    private final HotelService hotelService;
    private final Logger logger = LoggerFactory.getLogger(HotelController.class);

    /**
     * Constructor for HotelController.
     * 
     * @param hotelService The service layer for hotel-related operations.
     */
    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Endpoint for retrieving all hotels.
     * 
     * @return A list of all hotels as HotelDTOs.
     */
    @GetMapping
    public List<HotelDTO> getAllHotelsHandler() {
        return hotelService.getAllHotels();
    }

    /**
     * Endpoint for retrieving a hotel by its ID.
     * 
     * @param hotelId The ID of the hotel to retrieve.
     * @return A ResponseEntity containing the HotelDTO or a not found status.
     */
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelByIdHandler(@PathVariable int hotelId) {
        Optional<HotelDTO> hotel = hotelService.getById(hotelId);
        return hotel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for checking permissions for a hotel.
     * 
     * @param hotelId The ID of the hotel.
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity indicating whether the user has permission.
     */
    @GetMapping("/{hotelId}/permissions")
    public ResponseEntity<Boolean> checkPermissions(@PathVariable int hotelId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        boolean hasPermission = hotelService.hasPermission(hotelId, userId);
        return hasPermission ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }

    /**
     * Endpoint for retrieving favorite hotels for a user.
     * 
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity containing a list of favorite hotels as HotelDTOs.
     */
    @GetMapping("/favoritesUser")
    public ResponseEntity<List<HotelDTO>> getHotelFavoriteByUserId(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || "OWNER".equals(session.getAttribute("role"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<HotelDTO> favorites = hotelService.findFavoriteHotelsByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * Endpoint for retrieving hotels owned by a user.
     * 
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity containing a list of hotels owned by the user as
     *         HotelDTOs.
     */
    @GetMapping("/owner")
    public ResponseEntity<List<HotelDTO>> getHotelByOwner(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || "USER".equals(session.getAttribute("role"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<HotelDTO> ownerHotels = hotelService.findHotelByUserId(userId);
        return ResponseEntity.ok(ownerHotels);
    }

    /**
     * Endpoint for searching hotels by criteria.
     * 
     * @param request The search criteria encapsulated in a HotelSearchRequest
     *                object.
     * @return A ResponseEntity containing a list of filtered hotels as HotelDTOs.
     */
    @PostMapping("/search")
    public ResponseEntity<List<HotelDTO>> searchHotels(@RequestBody HotelSearchRequest request) {
        List<HotelDTO> filteredHotels = hotelService.filterHotels(request);
        return ResponseEntity.ok(filteredHotels);
    }

    /**
     * Endpoint for registering a new hotel.
     * 
     * @param hotelToBeRegistered The hotel details to register.
     * @param session             The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the created HotelDTO or a bad request
     *         status.
     */
    @PostMapping("/register")
    public ResponseEntity<HotelDTO> registerHotel(@RequestBody Hotel hotelToBeRegistered, HttpSession session) {
        if (!"OWNER".equals(session.getAttribute("role"))) {
            throw new GenericException("User does not have the OWNER role.");
        }
        Optional<HotelDTO> newHotel = hotelService.createHotel(hotelToBeRegistered,
                (Integer) session.getAttribute("userId"));
        newHotel.ifPresent(value -> logger.info("Hotel created with Id: {}", value.getHotelId()));
        return newHotel.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for updating an existing hotel.
     * 
     * @param hotelId      The ID of the hotel to update.
     * @param updatedHotel The updated hotel details.
     * @param session      The HTTP session to retrieve user details.
     * @return A ResponseEntity containing the updated HotelDTO or a not found
     *         status.
     */
    @PutMapping("/update/{hotelId}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable int hotelId, @RequestBody Hotel updatedHotel,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<HotelDTO> updated = Optional.of(hotelService.updateHotel(hotelId, userId, updatedHotel));
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for deleting a hotel.
     * 
     * @param hotelId The ID of the hotel to delete.
     * @param session The HTTP session to retrieve user details.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/delete/{hotelId}")
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
