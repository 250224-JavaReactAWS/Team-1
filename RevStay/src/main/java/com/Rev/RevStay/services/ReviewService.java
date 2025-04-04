package com.Rev.RevStay.services;

import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.repos.ReviewDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) { this.reviewDao = reviewDao; }

    //TODO Get Review By ID
    Optional<Review> reviewById(int reviewId){return reviewDao.findById(reviewId);}

    //TODO Get Reviews By HotelId
    public List<Review> getReviewsByHotelId(int hotelId){
        return reviewDao.getReviewsByHotelId(hotelId);
    }

    //TODO Get Reviews By UserId
    public List<Review> getReviewsByUserId(int userId){
        return reviewDao.getReviewsByUserId(userId);
    }

    //TODO Get Review By User and Hotel Id
    public Review getReviewByUserAndHotelId(int hotelId, int userId){
        return reviewDao.getReviewByHotelIdAndUserId(hotelId, userId);
    }
}
