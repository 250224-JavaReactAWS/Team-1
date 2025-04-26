package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.UserType;

/**
 * Data Transfer Object (DTO) class for transferring user-related data.
 * 
 * This class is used to encapsulate user information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `User` entity.
 * 
 * Fields:
 * - `userId`: The unique identifier of the user.
 * - `email`: The email address of the user.
 * - `passwordHash`: The hashed password of the user.
 * - `fullName`: The full name of the user.
 * - `userType`: The type of the user (e.g., USER, OWNER).
 * 
 * Constructors:
 * - Allows creating `UserDTO` objects with different combinations of fields
 * based on the use case.
 */
public class UserDTO {

    private int userId;
    private String email;
    private String passwordHash;
    private String fullName;
    private UserType userType;

    /**
     * Constructor for creating a UserDTO with all fields.
     * 
     * @param userId       The unique identifier of the user.
     * @param passwordHash The hashed password of the user.
     * @param email        The email address of the user.
     * @param fullName     The full name of the user.
     * @param userType     The type of the user (e.g., USER, OWNER).
     */
    public UserDTO(int userId, String passwordHash, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    /**
     * Constructor for creating a UserDTO with default user type.
     * 
     * @param email        The email address of the user.
     * @param passwordHash The hashed password of the user.
     * @param fullName     The full name of the user.
     */
    public UserDTO(String email, String passwordHash, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.userType = UserType.USER;
    }

    /**
     * Constructor for creating a UserDTO without a password.
     * 
     * @param userId   The unique identifier of the user.
     * @param email    The email address of the user.
     * @param fullName The full name of the user.
     * @param userType The type of the user (e.g., USER, OWNER).
     */
    public UserDTO(int userId, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters and setters for all fields.

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
