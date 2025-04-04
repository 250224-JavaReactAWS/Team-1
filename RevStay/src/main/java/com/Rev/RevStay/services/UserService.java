package com.Rev.RevStay.services;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.models.UserType;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.util.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional

public class UserService {

    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";


    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //TODO Register new user
    public Optional<User> register(User userToBeRegistered){

        Optional<User> potentialUser = userDAO.findUserByEmail(userToBeRegistered.getEmail());

        if (potentialUser.isPresent()){
            throw new GenericException("Email: " + userToBeRegistered.getEmail() + " is already taken!");
        }

        if (!userToBeRegistered.getEmail().matches(EMAIL_PATTERN)) {
            throw new GenericException("The email must be like Example@domainExamle.com");
        }

        if (!userToBeRegistered.getPasswordHash().matches(PASSWORD_PATTERN)) {
            throw new GenericException("Invalid Password. Must be at least 8 characters" +
                    "and need to contain at least one uppercase and lowercase letter " );
        }

        String hashPassword = PasswordUtil.hashPassword(userToBeRegistered.getPasswordHash());

        userToBeRegistered.setPasswordHash(hashPassword);

        return Optional.of(userDAO.save(userToBeRegistered));
    }

    //registration of an Hotel Owner and Hotel details
    public Optional<Map<String, Object>> registerOwner(User ownerToBeRegistered, Hotel hotelToBeRegistered) {

        Optional<User> potentialOwner = userDAO.findUserByEmail(ownerToBeRegistered.getEmail());

        if (potentialOwner.isPresent()) {
            throw new GenericException("Email: " + ownerToBeRegistered.getEmail() + " is already taken!");
        }

        if (!ownerToBeRegistered.getEmail().matches(EMAIL_PATTERN)) {
            throw new GenericException("The email must be like Example@domainExample.com");
        }

        if (!ownerToBeRegistered.getPasswordHash().matches(PASSWORD_PATTERN)) {
            throw new GenericException("Invalid Password. Must be at least 8 characters" +
                    " and need to contain at least one uppercase and lowercase letter.");
        }

        String hashPassword = PasswordUtil.hashPassword(ownerToBeRegistered.getPasswordHash());

        ownerToBeRegistered.setPasswordHash(hashPassword);

        ownerToBeRegistered.setUserType(UserType.OWNER);

        Optional<Hotel> potentialHotel = hotelDAO.findHotelByName(hotelToBeRegistered.getName());

        if (potentialHotel.isPresent()) {
            throw new GenericException("Hotel with name: " + hotelToBeRegistered.getName() + " already exists!");
        }

        Hotel savedHotel = hotelDAO.save(hotelToBeRegistered);

        User savedOwner = userDAO.save(ownerToBeRegistered);

        Map<String, Object> response = new HashMap<>();
        response.put("user", savedOwner);
        response.put("hotel", savedHotel);

        return Optional.of(response);
    }

    //TODO Login User
    public Optional<User> login(User userCredentials) {
        Optional<User> user = userDAO.findUserByEmail(userCredentials.getEmail());
        User userToLogin;

        if (user.isEmpty()) {
            throw new GenericException("User not found with tha Email");
        } else {
            userToLogin = user.get();
        }

        if (!PasswordUtil.checkPassword(userToLogin.getPasswordHash(),userCredentials.getPasswordHash())) {
            throw new GenericException("Incorrect Password");
        }

        return Optional.of(userToLogin);
    }

}

