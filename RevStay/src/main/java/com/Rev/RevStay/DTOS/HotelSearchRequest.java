package com.Rev.RevStay.DTOS;

import java.time.LocalDateTime;
import java.util.List;

public class HotelSearchRequest {
    private List<String> amenities;
    private String location;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    // Getters y Setters


    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }
}
