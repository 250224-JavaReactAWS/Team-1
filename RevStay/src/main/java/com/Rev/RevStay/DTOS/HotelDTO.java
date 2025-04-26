package com.Rev.RevStay.DTOS;

import java.util.List;

/**
 * Data Transfer Object (DTO) class for transferring hotel-related data.
 * 
 * This class is used to encapsulate hotel information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `Hotel` entity.
 * 
 * Fields:
 * - `hotelId`: The unique identifier of the hotel.
 * - `name`: The name of the hotel.
 * - `location`: The location of the hotel.
 * - `description`: A description of the hotel.
 * - `amenities`: A string representing the amenities offered by the hotel.
 * - `priceRange`: The price range of the hotel.
 * - `images`: A list of image URLs associated with the hotel.
 * - `ownerEmail`: The email address of the hotel's owner.
 * - `ownerFullName`: The full name of the hotel's owner.
 * 
 * Constructors:
 * - Allows creating `HotelDTO` objects with all fields.
 * 
 * Getters and Setters:
 * - Provides methods to access and modify the fields.
 */
public class HotelDTO {

    private int hotelId;
    private String name;
    private String location;
    private String description;
    private String amenities;
    private String priceRange;
    private List<String> images;
    private String ownerEmail;
    private String ownerFullName;

    /**
     * Constructor for creating a HotelDTO with all fields.
     * 
     * @param hotelId       The unique identifier of the hotel.
     * @param name          The name of the hotel.
     * @param location      The location of the hotel.
     * @param description   A description of the hotel.
     * @param amenities     A string representing the amenities offered by the
     *                      hotel.
     * @param priceRange    The price range of the hotel.
     * @param images        A list of image URLs associated with the hotel.
     * @param ownerEmail    The email address of the hotel's owner.
     * @param ownerFullName The full name of the hotel's owner.
     */
    public HotelDTO(int hotelId, String name, String location, String description, String amenities,
            String priceRange, List<String> images, String ownerEmail, String ownerFullName) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.description = description;
        this.amenities = amenities;
        this.priceRange = priceRange;
        this.images = images;
        this.ownerEmail = ownerEmail;
        this.ownerFullName = ownerFullName;
    }

    // Getters and setters for all fields.

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
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

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }
}
