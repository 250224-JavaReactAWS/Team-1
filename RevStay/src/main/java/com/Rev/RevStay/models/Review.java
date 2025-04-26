package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//Create review as an Entity
@Entity
@Table(name = "reviews")

public class Review {
    //Define the columns of the table reviews with JPA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    //Relation ManyToOne in references key to users table
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    //Relation ManyToOne in references key to hotels table
    @ManyToOne
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //Make the constructor for obtain the created_at at the time is created
    public Review(){
        this.createdAt = LocalDateTime.now();
    }

    //Getters and Setters

    public int getReviewId() { return reviewId; }

    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Hotel getHotel() { return hotel; }

    public void setHotel(Hotel hotel) { this.hotel = hotel;}

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setId(int i) {
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

    public Integer getId() {
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
}
