package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.UserDTO;
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
            session.setAttribute("role", userLogged.get().getUserType());

            return userLogged.get();
        }
        return null;
    }

    @PostMapping("favorites/{hotelId}")
    public ResponseEntity<String>
        addHotelToFavorites(HttpSession session, @PathVariable int hotelId){

        UserType userType = (UserType) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (userType != UserType.USER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be USER");
        }

        userService.addHotelToFavorites(userId, hotelId);
        return ResponseEntity.ok("Hotel added to favorites successfully");
    }

    @DeleteMapping("favorites/{hotelId}")
    public ResponseEntity<String>
        removeHotelFromFavorite(HttpSession session, @PathVariable int hotelId){

        UserType userType = (UserType) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (userType != UserType.USER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role must be USER");
        }

        userService.removeHotelFromFavorites(userId, hotelId);
        return ResponseEntity.ok("Hotel removed from favorites successfully");
    }

    /*@PostMapping("register/owner")
    public ResponseEntity<User> registerOwnerHandler(@RequestBody User user, @RequestBody Hotel hotel){
        Optional<Map<String, Object>> newUserMap = userService.registerOwner(user, hotel);

        if (newUserMap.isPresent() && newUserMap.get().containsKey("user")) {
            User newUser = (User) newUserMap.get().get("user");
            logger.info("User created with Id: {}", newUser.getUserId());
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }*/

}
