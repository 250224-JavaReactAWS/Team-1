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

/**
 * Service class for managing review-related operations such as retrieving,
 * registering,
 * and converting reviews.
 * 
 * This class provides methods to:
 * - Retrieve a review by its ID.
 * - Retrieve reviews by hotel ID.
 * - Retrieve reviews by user ID.
 * - Retrieve a review by both user ID and hotel ID.
 * - Register a new review.
 * 
 * It uses `ReviewDao`, `UserDAO`, and `HotelDAO` for database interactions.
 * 
 * Exceptions:
 * - Throws `GenericException` for invalid inputs or when required entities
 * (user or hotel) are not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 */
@Service
public class ReviewService {

    private final ReviewDao reviewDao;
    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;

    /**
     * Constructor for ReviewService.
     * 
     * @param reviewDao Data access object for review-related operations.
     * @param userDAO   Data access object for user-related operations.
     * @param hotelDAO  Data access object for hotel-related operations.
     */
    public ReviewService(ReviewDao reviewDao, UserDAO userDAO, HotelDAO hotelDAO) {
        this.reviewDao = reviewDao;
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
    }

    /**
     * Retrieves a review by its ID.
     * 
     * @param reviewId The ID of the review to be retrieved.
     * @return An Optional containing the ReviewDTO for the specified review.
     */
    public Optional<ReviewDTO> reviewById(int reviewId) {
        return reviewDao.findById(reviewId).map(this::convertToDTO);
    }

    /**
     * Retrieves all reviews for a specific hotel by its ID.
     * 
     * @param hotelId The ID of the hotel whose reviews are to be retrieved.
     * @return A list of ReviewDTOs for the reviews of the specified hotel.
     */
    public List<ReviewDTO> getReviewsByHotelId(int hotelId) {
        return reviewDao.getReviewsByHotelId(hotelId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reviews for a specific user by their ID.
     * 
     * @param userId The ID of the user whose reviews are to be retrieved.
     * @return A list of ReviewDTOs for the reviews made by the specified user.
     */
    public List<ReviewDTO> getReviewsByUserId(int userId) {
        return reviewDao.getReviewsByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a review by both user ID and hotel ID.
     * 
     * @param hotelId The ID of the hotel.
     * @param userId  The ID of the user.
     * @return An Optional containing the ReviewDTO for the specified review.
     */
    public Optional<ReviewDTO> getReviewByUserAndHotelId(int hotelId, int userId) {
        return Optional.ofNullable(reviewDao.getReviewByHotelIdAndUserId(hotelId, userId))
                .map(this::convertToDTO);
    }

    /**
     * Registers a new review.
     * 
     * @param reviewToBeRegistered The review to be registered.
     * @param userId               The ID of the user registering the review.
     * @param hotelId              The ID of the hotel for which the review is being
     *                             registered.
     * @return An Optional containing the registered ReviewDTO.
     * @throws GenericException if the user or hotel does not exist.
     */
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

    /**
     * Converts a Review entity to a ReviewDTO.
     * 
     * @param review The Review entity to be converted.
     * @return The corresponding ReviewDTO.
     */
    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getReviewId(),
                review.getComment(),
                review.getRating(),
                review.getUser().getUserId(),
                review.getUser().getFullName(),
                review.getHotel().getHotelId());
    }

}
