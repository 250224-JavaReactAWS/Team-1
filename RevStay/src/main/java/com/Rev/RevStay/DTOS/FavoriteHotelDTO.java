package com.Rev.RevStay.DTOS;

public class FavoriteHotelDTO {
    private int hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelImageUrl;
    private double hotelRating;

    public FavoriteHotelDTO(int hotelId, String hotelName, String hotelAddress, String hotelImageUrl, double hotelRating, int numberOfReviews) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelImageUrl = hotelImageUrl;
        this.hotelRating = hotelRating;

    }

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
