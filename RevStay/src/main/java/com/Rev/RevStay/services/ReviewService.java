package com.Rev.RevStay.services;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.ReviewDao;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewDao reviewDao;
    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;

    public ReviewService(ReviewDao reviewDao, UserDAO userDAO, HotelDAO hotelDAO) { this.reviewDao = reviewDao;
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
    }

    //Get Review By ID
    public Optional<Review> reviewById(int reviewId){return reviewDao.findById(reviewId);}

    //Get Reviews By HotelId
    public List<Review> getReviewsByHotelId(int hotelId){
        return reviewDao.getReviewsByHotelId(hotelId);
    }

    //Get Reviews By UserId
    public List<Review> getReviewsByUserId(int userId){
        return reviewDao.getReviewsByUserId(userId);
    }

    //Get Review By User and Hotel Id
    public Optional<Review> getReviewByUserAndHotelId(int hotelId, int userId){
        return Optional.of(reviewDao.getReviewByHotelIdAndUserId(hotelId, userId));
    }

    //Register new review
    public Optional<Review> registerReview(Review reviewToBeRegistered, int userId, int hotelId) {
        Optional<User> userOpt = userDAO.findById(userId);
        Optional<Hotel> hotelOpt = hotelDAO.findById(hotelId);

        if (userOpt.isEmpty() || hotelOpt.isEmpty()) {
            throw new GenericException("The hotel or user not exist");
        }

        reviewToBeRegistered.setUser(userOpt.get());
        reviewToBeRegistered.setHotel(hotelOpt.get());
        reviewToBeRegistered.setCreatedAt(LocalDateTime.now());

        return Optional.of(reviewDao.save(reviewToBeRegistered));
    }

}
