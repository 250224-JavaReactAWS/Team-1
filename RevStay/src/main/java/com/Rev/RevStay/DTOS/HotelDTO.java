package com.Rev.RevStay.DTOS;

import java.time.LocalDateTime;
import java.util.List;

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
