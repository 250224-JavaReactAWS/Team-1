package com.Rev.RevStay.DTOS;

/**
 * Data Transfer Object (DTO) class for transferring favorite hotel-related
 * data.
 * 
 * This class is used to encapsulate information about a user's favorite hotels
 * and transfer it between different layers of the application (e.g., service
 * and controller layers).
 * 
 * Fields:
 * - `hotelId`: The unique identifier of the hotel.
 * - `hotelName`: The name of the hotel.
 * - `hotelAddress`: The address of the hotel.
 * - `hotelImageUrl`: The URL of an image representing the hotel.
 * - `hotelRating`: The average rating of the hotel.
 * 
 * Constructors:
 * - Allows creating `FavoriteHotelDTO` objects with all fields.
 * 
 * Getters and Setters:
 * - Provides methods to access and modify the fields.
 */
public class FavoriteHotelDTO {

    private int hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelImageUrl;
    private double hotelRating;

    /**
     * Constructor for creating a FavoriteHotelDTO with all fields.
     * 
     * @param hotelId       The unique identifier of the hotel.
     * @param hotelName     The name of the hotel.
     * @param hotelAddress  The address of the hotel.
     * @param hotelImageUrl The URL of an image representing the hotel.
     * @param hotelRating   The average rating of the hotel.
     */
    public FavoriteHotelDTO(int hotelId, String hotelName, String hotelAddress, String hotelImageUrl,
            double hotelRating) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelImageUrl = hotelImageUrl;
        this.hotelRating = hotelRating;
    }

    // Getters and setters for all fields.

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelImageUrl() {
        return hotelImageUrl;
    }

    public void setHotelImageUrl(String hotelImageUrl) {
        this.hotelImageUrl = hotelImageUrl;
    }

    public double getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(double hotelRating) {
        this.hotelRating = hotelRating;
    }
}
