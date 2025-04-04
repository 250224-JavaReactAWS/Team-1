package com.Rev.RevStay.controllers;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("reviews")

public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewByIdHandler(@PathVariable int reviewId) {
        Optional<Review> Review = reviewService.reviewById(reviewId);

        //Return 404 Not Found
        return Review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/hotel/{hotelId}")
    public List<Review> getReviewsByHotelIdHandler(@PathVariable int hotelId){
        return reviewService.getReviewsByHotelId(hotelId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserIdHandler(@PathVariable int userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/hotel/{hotelId}/user/{userId}")
    public ResponseEntity<Review> getReviewByUserAndHotelIdHandler(@PathVariable int hotelId, @PathVariable int userId) {
        Optional<Review> review = reviewService.getReviewByUserAndHotelId(hotelId, userId);

        //Return 404 not found
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
