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

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService userService){this.userService = userService;}

    @PostMapping("register")
    public ResponseEntity<User> registerHandler(@RequestBody User user){
        Optional<User> newUser = userService.register(user);

        newUser.ifPresent(value -> logger.info("User created with Id: {}", value.getUserId()));

        return newUser.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build() );
    }

    @PostMapping("login")
    public UserDTO loginHandler(@RequestBody User userCredentials, HttpSession session){
        Optional<UserDTO> userLogged = userService.login(userCredentials);

        if(userLogged.isPresent()) {
            session.setAttribute("userId", userLogged.get().getUserId());
            session.setAttribute("role", userLogged.get().getUserType().name());
            // System.out.println(userLogged.get().getUserType().name());
            return userLogged.get();
        }
        return null;
    }

    @PostMapping("/favorites/{hotelId}")
    public ResponseEntity<String>
        addHotelToFavorites(HttpSession session, @PathVariable int hotelId) {

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


    @DeleteMapping("/favorites/{hotelId}")
    public ResponseEntity<String>
        removeHotelFromFavorite(HttpSession session, @PathVariable int hotelId){

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
    @GetMapping("session")
    public String getCurrentRoleHandler(HttpSession session){
        if (session.getAttribute("role") == null){
            // This means we are not authenticated
            throw new GenericException("Not logged in!");
        }

        UserType role = UserType.valueOf((String) session.getAttribute("role"));

        return role.toString();

    }

    // Logout Endpoint
    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutHandler(HttpSession session){
        session.invalidate();
    }
}
