package com.Rev.RevStay.DTOS;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) class for handling hotel search requests.
 * 
 * This class is used to encapsulate the search criteria provided by the user
 * when searching for hotels. It allows filtering hotels based on location,
 * amenities, and availability dates.
 * 
 * Fields:
 * - `amenities`: A list of amenities that the user wants in the hotel.
 * - `location`: The location where the user wants to search for hotels.
 * - `checkIn`: The check-in date for the hotel stay.
 * - `checkOut`: The check-out date for the hotel stay.
 * 
 * Getters and Setters:
 * - Provides methods to access and modify the search criteria fields.
 */
public class HotelSearchRequest {

    private List<String> amenities;
    private String location;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    // Getters and Setters

    /**
     * Gets the list of amenities.
     * 
     * @return A list of amenities that the user wants in the hotel.
     */
    public List<String> getAmenities() {
        return amenities;
    }

    /**
     * Sets the list of amenities.
     * 
     * @param amenities A list of amenities to set.
     */
    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    /**
     * Gets the location for the hotel search.
     * 
     * @return The location where the user wants to search for hotels.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location for the hotel search.
     * 
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the check-in date for the hotel stay.
     * 
     * @return The check-in date.
     */
    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    /**
     * Sets the check-in date for the hotel stay.
     * 
     * @param checkIn The check-in date to set.
     */
    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * Gets the check-out date for the hotel stay.
     * 
     * @return The check-out date.
     */
    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    /**
     * Sets the check-out date for the hotel stay.
     * 
     * @param checkOut The check-out date to set.
     */
    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }
}
