package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.ReviewDTO;
import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.services.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing review-related operations.
 * 
 * This controller provides endpoints for:
 * - Retrieving a review by its ID.
 * - Retrieving reviews by hotel ID.
 * - Retrieving reviews by user ID.
 * - Retrieving a review by both user ID and hotel ID.
 * - Creating a new review.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/reviews` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 */
@RestController
@RequestMapping("reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookingDAO bookingDAO;

    /**
     * Constructor for ReviewController.
     * 
     * @param reviewService The service layer for review-related operations.
     * @param bookingDAO    The DAO layer for booking-related operations.
     */
    @Autowired
    public ReviewController(ReviewService reviewService, BookingDAO bookingDAO) {
        this.reviewService = reviewService;
        this.bookingDAO = bookingDAO;
    }

    /**
     * Endpoint for retrieving a review by its ID.
     * 
     * @param reviewId The ID of the review to retrieve.
     * @return A ResponseEntity containing the ReviewDTO or a not found status.
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewByIdHandler(@PathVariable int reviewId) {
        Optional<ReviewDTO> review = reviewService.reviewById(reviewId);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for retrieving reviews by hotel ID.
     * 
     * @param hotelId The ID of the hotel to retrieve reviews for.
     * @return A list of ReviewDTOs for the specified hotel.
     */
    @GetMapping("/hotel/{hotelId}")
    public List<ReviewDTO> getReviewsByHotelIdHandler(@PathVariable int hotelId) {
        return reviewService.getReviewsByHotelId(hotelId);
    }

    /**
     * Endpoint for retrieving reviews by user ID.
     * 
     * @param userId The ID of the user to retrieve reviews for.
     * @return A list of ReviewDTOs for the specified user.
     */
    @GetMapping("/user/{userId}")
    public List<ReviewDTO> getReviewsByUserIdHandler(@PathVariable int userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    /**
     * Endpoint for retrieving a review by both user ID and hotel ID.
     * 
     * @param hotelId The ID of the hotel.
     * @param userId  The ID of the user.
     * @return A ResponseEntity containing the ReviewDTO or a not found status.
     */
    @GetMapping("/hotel/{hotelId}/user/{userId}")
    public ResponseEntity<ReviewDTO> getReviewByUserAndHotelIdHandler(@PathVariable int hotelId,
            @PathVariable int userId) {
        Optional<ReviewDTO> review = reviewService.getReviewByUserAndHotelId(hotelId, userId);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for creating a new review.
     * 
     * @param review  The review details to create.
     * @param session The HTTP session to retrieve user details.
     * @param hotelId The ID of the hotel associated with the review.
     * @return A ResponseEntity containing the created ReviewDTO or an error status.
     */
    @PostMapping("/register")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review,
            HttpSession session,
            @RequestParam int hotelId) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (reviewService.getReviewByUserAndHotelId(hotelId, userId).isPresent()) {
            return ResponseEntity.status(409).build();
        }

        Optional<Booking> bookingOpt = bookingDAO.findByUserAndHotel(userId, hotelId);

        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Booking booking = bookingOpt.get();

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            return ResponseEntity.status(403).body(null);
        }

        Optional<ReviewDTO> created = reviewService.registerReview(review, userId, hotelId);
        return created.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
