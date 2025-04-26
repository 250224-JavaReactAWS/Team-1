package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a user in the system.
 * 
 * This class maps to the `users` table in the database and contains details
 * about:
 * - The user's email, password, full name, and user type.
 * - The user's favorite hotels.
 * 
 * Annotations:
 * - `@Entity`: Marks this class as a JPA entity.
 * - `@Table`: Specifies the table name in the database.
 * - `@Id`: Marks the primary key of the entity.
 * - `@GeneratedValue`: Specifies the generation strategy for the primary key.
 * - `@Column`: Maps fields to database columns and specifies constraints.
 * - `@Enumerated`: Maps the `UserType` enum to a string column in the database.
 * - `@ManyToMany`: Defines a many-to-many relationship with the `Hotel` entity
 * for favorite hotels.
 * - `@JoinTable`: Specifies the join table for the many-to-many relationship.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @ManyToMany
    @JoinTable(name = "favorites", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "hotelId"))
    private Set<Hotel> favoriteHotels = new HashSet<>();

    /**
     * Default constructor for the User class.
     */
    public User() {
    }

    /**
     * Constructor for creating a user with all fields.
     * 
     * @param userId       The user's ID.
     * @param passwordHash The user's hashed password.
     * @param email        The user's email.
     * @param fullName     The user's full name.
     * @param userType     The user's type (e.g., ADMIN, USER).
     */
    public User(int userId, String passwordHash, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    /**
     * Constructor for creating a user with default user type.
     * 
     * @param email        The user's email.
     * @param passwordHash The user's hashed password.
     * @param fullName     The user's full name.
     */
    public User(String email, String passwordHash, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.userType = UserType.USER;
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

    public Set<Hotel> getFavoriteHotels() {
        return favoriteHotels;
    }

    public void setFavoriteHotels(Set<Hotel> favoriteHotels) {
        this.favoriteHotels = favoriteHotels;
    }
}
