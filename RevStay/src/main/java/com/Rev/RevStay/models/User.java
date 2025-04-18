package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    //This is the relation of favorites in the table
    //user and hotel for mark the favorites hotels per user

    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "hotelId")
    )
    private Set<Hotel> favoriteHotels = new HashSet<>();


    public User() {}

    public User(int userId, String passwordHash, String email, String fullName, UserType userType) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    public User(String email, String passwordHash, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.userType = UserType.USER;
    }

    public User(int userId2) {
        // Auto-generated constructor stub
    }

    public User(String string, String string2) {
        // Auto-generated constructor stub
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

    public Set<Hotel> getFavoriteHotels() { return favoriteHotels; }

    public void setFavoriteHotels(Set<Hotel> favoriteHotels) { this.favoriteHotels = favoriteHotels; }

}
