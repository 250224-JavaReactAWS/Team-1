package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.UserDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.models.UserType;
import com.Rev.RevStay.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * REST controller for managing user-related operations.
 * 
 * This controller provides endpoints for:
 * - User registration.
 * - User login.
 * - Adding and removing favorite hotels.
 * - Retrieving the current user's session role.
 * - Logging out.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/users` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 */
@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Constructor for UserController.
     * 
     * @param userService The service layer for user-related operations.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for user registration.
     * 
     * @param user The user details to register.
     * @return A ResponseEntity containing the created user or a bad request status.
     */
    @PostMapping("register")
    public ResponseEntity<User> registerHandler(@RequestBody User user) {
        Optional<User> newUser = userService.register(user);

        newUser.ifPresent(value -> logger.info("User created with Id: {}", value.getUserId()));

        return newUser.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for user login.
     * 
     * @param userCredentials The user's login credentials.
     * @param session         The HTTP session to store user details.
     * @return The logged-in user's details as a UserDTO, or null if login fails.
     */
    @PostMapping("login")
    public UserDTO loginHandler(@RequestBody User userCredentials, HttpSession session) {
        Optional<UserDTO> userLogged = userService.login(userCredentials);

        if (userLogged.isPresent()) {
            session.setAttribute("userId", userLogged.get().getUserId());
            session.setAttribute("role", userLogged.get().getUserType().name());
            return userLogged.get();
        }
        return null;
    }

    /**
     * Endpoint for adding a hotel to the user's favorites.
     * 
     * @param session The HTTP session to retrieve user details.
     * @param hotelId The ID of the hotel to add to favorites.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/favorites/{hotelId}")
    public ResponseEntity<String> addHotelToFavorites(HttpSession session, @PathVariable int hotelId) {

        String roleStr = (String) session.getAttribute("role");
        if (roleStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        UserType userType = UserType.valueOf(roleStr);
        int userId = (int) session.getAttribute("userId");

        if (userType != UserType.USER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be USER");
        }

        userService.addHotelToFavorites(userId, hotelId);
        return ResponseEntity.ok("Hotel added to favorites successfully");
    }

    /**
     * Endpoint for removing a hotel from the user's favorites.
     * 
     * @param session The HTTP session to retrieve user details.
     * @param hotelId The ID of the hotel to remove from favorites.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/favorites/{hotelId}")
    public ResponseEntity<String> removeHotelFromFavorite(HttpSession session, @PathVariable int hotelId) {

        String roleStr = (String) session.getAttribute("role");
        if (roleStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        UserType userType = UserType.valueOf(roleStr);
        int userId = (int) session.getAttribute("userId");

        if (userType != UserType.USER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be USER");
        }

        userService.removeHotelFromFavorites(userId, hotelId);
        return ResponseEntity.ok("Hotel removed from favorites successfully");
    }

    /**
     * Endpoint for retrieving the current user's session role.
     * 
     * @param session The HTTP session to retrieve user details.
     * @return The role of the current user as a string.
     * @throws GenericException if the user is not logged in.
     */
    @GetMapping("session")
    public String getCurrentRoleHandler(HttpSession session) {
        if (session.getAttribute("role") == null) {
            // This means we are not authenticated
            throw new GenericException("Not logged in!");
        }

        UserType role = UserType.valueOf((String) session.getAttribute("role"));

        return role.toString();

    }

    /**
     * Endpoint for logging out the current user.
     * 
     * @param session The HTTP session to invalidate.
     */
    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutHandler(HttpSession session) {
        session.invalidate();
    }
}
