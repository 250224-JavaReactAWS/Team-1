package com.Rev.RevStay.DTOS;

/**
 * Data Transfer Object (DTO) class for transferring review-related data.
 * 
 * This class is used to encapsulate review information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `Review` entity.
 * 
 * Fields:
 * - `reviewId`: The unique identifier of the review.
 * - `reviewText`: The text content of the review.
 * - `rating`: The rating provided in the review.
 * - `userId`: The unique identifier of the user who created the review.
 * - `userFullName`: The full name of the user who created the review.
 * - `bookingId`: The unique identifier of the booking associated with the
 * review.
 * 
 * Constructors:
 * - Default constructor for creating an empty `ReviewDTO`.
 * - Parameterized constructor for creating a `ReviewDTO` with all fields.
 */
public class ReviewDTO {

    private int reviewId;
    private String reviewText;
    private int rating;
    private int userId;
    private String userFullName;
    private int bookingId;

    /**
     * Default constructor for the ReviewDTO class.
     */
    public ReviewDTO() {
    }

    /**
     * Constructor for creating a ReviewDTO with all fields.
     * 
     * @param reviewId     The unique identifier of the review.
     * @param reviewText   The text content of the review.
     * @param rating       The rating provided in the review.
     * @param userId       The unique identifier of the user who created the review.
     * @param userFullName The full name of the user who created the review.
     * @param bookingId    The unique identifier of the booking associated with the
     *                     review.
     */
    public ReviewDTO(int reviewId, String reviewText, int rating, int userId, String userFullName, int bookingId) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userId = userId;
        this.userFullName = userFullName;
        this.bookingId = bookingId;
    }

    // Getters and setters for all fields.

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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
