package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.ReviewDTO;
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
import java.util.stream.Collectors;

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
    public Optional<ReviewDTO> reviewById(int reviewId){ return reviewDao.findById(reviewId).map(this::convertToDTO);}

    //Get Reviews By HotelId
    public List<ReviewDTO> getReviewsByHotelId(int hotelId) {
        return reviewDao.getReviewsByHotelId(hotelId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Get Reviews By UserId
    public List<ReviewDTO> getReviewsByUserId(int userId) {
        return reviewDao.getReviewsByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Get Review By User and Hotel Id
    public Optional<ReviewDTO> getReviewByUserAndHotelId(int hotelId, int userId) {
        return Optional.ofNullable(reviewDao.getReviewByHotelIdAndUserId(hotelId, userId))
                .map(this::convertToDTO);
    }

    //Register new review
    public Optional<ReviewDTO> registerReview(Review reviewToBeRegistered, int userId, int hotelId) {
        Optional<User> userOpt = userDAO.findById(userId);
        Optional<Hotel> hotelOpt = hotelDAO.findById(hotelId);

        if (userOpt.isEmpty() || hotelOpt.isEmpty()) {
            throw new GenericException("The hotel or user does not exist");
        }

        reviewToBeRegistered.setUser(userOpt.get());
        reviewToBeRegistered.setHotel(hotelOpt.get());
        reviewToBeRegistered.setCreatedAt(LocalDateTime.now());

        return Optional.of(convertToDTO(reviewDao.save(reviewToBeRegistered)));
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getReviewId(),
                review.getComment(),
                review.getRating(),
                review.getUser().getUserId(),
                review.getUser().getFullName(),
                review.getHotel().getHotelId()
        );
    }

}
