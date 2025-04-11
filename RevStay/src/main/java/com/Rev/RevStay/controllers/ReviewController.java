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

@RestController
@RequestMapping("reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {
    private final ReviewService reviewService;
    private final BookingDAO bookingDAO;



    @Autowired
    public ReviewController(ReviewService reviewService, BookingDAO bookingDAO) {
        this.reviewService = reviewService;
        this.bookingDAO = bookingDAO;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewByIdHandler(@PathVariable int reviewId) {
        Optional<ReviewDTO> Review = reviewService.reviewById(reviewId);

        //Return 404 Not Found
        return Review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/hotel/{hotelId}")
    public List<ReviewDTO> getReviewsByHotelIdHandler(@PathVariable int hotelId){
        return reviewService.getReviewsByHotelId(hotelId);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewDTO> getReviewsByUserIdHandler(@PathVariable int userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/hotel/{hotelId}/user/{userId}")
    public ResponseEntity<ReviewDTO> getReviewByUserAndHotelIdHandler(@PathVariable int hotelId, @PathVariable int userId) {
        Optional<ReviewDTO> review = reviewService.getReviewByUserAndHotelId(hotelId, userId);

        //Return 404 not found
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
