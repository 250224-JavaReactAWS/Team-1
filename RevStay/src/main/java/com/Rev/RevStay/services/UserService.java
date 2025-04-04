package com.Rev.RevStay.services;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional

public class UserService {

    private final UserDAO userDAO;

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

        String HashPassword = PasswordUtil.hashPassword(userToBeRegistered.getPasswordHash());

        userToBeRegistered.setPasswordHash(HashPassword);

        return Optional.of(userDAO.save(userToBeRegistered));
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
