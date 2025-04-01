package com.Rev.RevStay.models;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    
}