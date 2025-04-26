package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entity class representing a room in the system.
 * 
 * This class maps to the `rooms` table in the database and contains details
 * about:
 * - The hotel to which the room belongs.
 * - The type, description, price, and maximum number of guests for the room.
 * 
 * Annotations:
 * - `@Entity`: Marks this class as a JPA entity.
 * - `@Table`: Specifies the table name in the database.
 * - `@Id`: Marks the primary key of the entity.
 * - `@GeneratedValue`: Specifies the generation strategy for the primary key.
 * - `@ManyToOne`: Defines a many-to-one relationship with the `Hotel` entity.
 * - `@JoinColumn`: Specifies the foreign key column for the hotel relationship.
 * - `@Column`: Maps fields to database columns and specifies constraints.
 */
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String roomType;

    @Column
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int maxGuests;

    /**
     * Default constructor for the Room class.
     */
    public Room() {
    }

    // Getters and setters for all fields.

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
}
