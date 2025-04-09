package com.Rev.RevStay.DTOS;

public class ReviewDTO {
    private Long id;
    private String reviewText;
    private int rating;
    private Long userId;
    private Long bookingId;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String reviewText, int rating, Long userId, Long bookingId) {
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userId = userId;
        this.bookingId = bookingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
}
