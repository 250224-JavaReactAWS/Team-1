package com.Rev.RevStay.DTOS;

public class ReviewDTO {
    private int reviewId;
    private String reviewText;
    private int rating;
    private int userId;
    private String userFullName;
    private int bookingId;

    public ReviewDTO() {
    }

    public ReviewDTO(int reviewId, String reviewText, int rating, int userId, String userFullName, int bookingId) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userId = userId;
        this.userFullName = userFullName;
        this.bookingId = bookingId;
    }

    public int getId() {
        return reviewId;
    }

    public void setId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserFullName() { return userFullName; }

    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
}
