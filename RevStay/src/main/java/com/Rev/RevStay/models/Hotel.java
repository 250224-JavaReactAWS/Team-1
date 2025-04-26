package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entity class representing a hotel in the system.
 * 
 * This class maps to the `hotels` table in the database and contains details
 * about:
 * - The owner of the hotel.
 * - The hotel's name, location, description, and amenities.
 * - The price range of the hotel.
 * - A list of images associated with the hotel.
 * - The creation timestamp.
 * - The rooms associated with the hotel.
 * 
 * Annotations:
 * - `@Entity`: Marks this class as a JPA entity.
 * - `@Table`: Specifies the table name in the database.
 * - `@Id`: Marks the primary key of the entity.
 * - `@GeneratedValue`: Specifies the generation strategy for the primary key.
 * - `@ManyToOne`: Defines a many-to-one relationship with the `User` entity
 * (hotel owner).
 * - `@JoinColumn`: Specifies the foreign key column for the owner relationship.
 * - `@Column`: Maps fields to database columns and specifies constraints.
 * - `@ElementCollection`: Maps a collection of elements (e.g., images) to a
 * separate table.
 * - `@OneToMany`: Defines a one-to-many relationship with the `Room` entity.
 */
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hotelId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String amenities;

    @Column(nullable = false)
    private String priceRange;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms;

    /**
     * Default constructor for the Hotel class.
     */
    public Hotel() {
    }

    /**
     * Constructor for creating a hotel with an owner.
     * 
     * @param user The owner of the hotel.
     */
    public Hotel(User user) {
        this.owner = user;
    }

    // Getters and setters for all fields.

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getAmenities() {
        if (amenities == null || amenities.isEmpty())
            return new ArrayList<>();
        return Arrays.asList(amenities.split(","));
    }

    public void setAmenities(List<String> amenitiesList) {
        this.amenities = String.join(",", amenitiesList);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
