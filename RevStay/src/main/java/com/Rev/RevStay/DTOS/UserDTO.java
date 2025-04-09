package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.UserType;


public class UserDTO {
    private int userId;


    private String email;


    private String passwordHash;
    private String fullName;
    private UserType userType;



    public UserDTO(int userId, String passwordHash, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    public UserDTO(String email, String passwordHash, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.userType = UserType.USER;
    }

    public UserDTO(int userId, String email, String fullName, UserType userType) {

        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
