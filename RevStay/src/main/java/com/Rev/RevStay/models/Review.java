package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity class representing a review in the system.
 * 
 * This class maps to the `reviews` table in the database and contains details
 * about:
 * - The user who created the review.
 * - The hotel being reviewed.
 * - The rating and comment provided by the user.
 * - The creation timestamp of the review.
 * 
 * Annotations:
 * - `@Entity`: Marks this class as a JPA entity.
 * - `@Table`: Specifies the table name in the database.
 * - `@Id`: Marks the primary key of the entity.
 * - `@GeneratedValue`: Specifies the generation strategy for the primary key.
 * - `@ManyToOne`: Defines many-to-one relationships with `User` and `Hotel`.
 * - `@JoinColumn`: Specifies the foreign key columns for relationships.
 * - `@Column`: Maps fields to database columns and specifies constraints.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor for the Review class.
     * Initializes the creation timestamp to the current time.
     */
    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters for all fields.

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.reviewId = id;
    }

    public Integer getId() {
        return this.reviewId;
    }
}
