package com.Rev.RevStay.models;

import jakarta.persistence.*;

//Create review as an Entity
@Entity
@Table(name = "reviews")

public class Review {
    @Id
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
