package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.ReviewDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.ReviewDao;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ReviewServiceTest {

    @Mock
    private ReviewDao reviewDao;

    @Mock
    private UserDAO userDAO;

    @Mock
    private HotelDAO hotelDAO;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private User user;
    private Hotel hotel;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setFullName("Test User");

        hotel = new Hotel();
        hotel.setHotelId(1);

        review = new Review();
        review.setReviewId(100);
        review.setComment("Great stay!");
        review.setRating(5);
        review.setUser(user);
        review.setHotel(hotel);
        review.setCreatedAt(LocalDateTime.now());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReviewById() {

        when(reviewDao.findById(100)).thenReturn(Optional.of(review));

        Optional<ReviewDTO> result = reviewService.reviewById(100);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getRating());
        assertEquals("Great stay!", result.get().getReviewText());
    }

    @Test
    void testGetReviewsByHotelId() {
        when(reviewDao.getReviewsByHotelId(1)).thenReturn(List.of(review));

        List<ReviewDTO> results  = reviewService.getReviewsByHotelId(1);

        assertEquals(1, results.size());
        assertEquals("Great stay!", results.get(0).getReviewText());
    }

    @Test
    void testGetReviewsByUserId() {
        when(reviewDao.getReviewsByUserId(1)).thenReturn(List.of(review));

        List<ReviewDTO> results = reviewService.getReviewsByUserId(1);

        assertEquals(1, results.size());
        assertEquals(5, results.get(0).getRating());
    }


    @Test
    void testGetReviewByUserAndHotelId() {
        when(reviewDao.getReviewByHotelIdAndUserId(1, 1)).thenReturn(review);

        Optional<ReviewDTO> result = reviewService.getReviewByUserAndHotelId(1, 1);

        assertTrue(result.isPresent());
        assertEquals("Great stay!", result.get().getReviewText());
    }


    @Test
    void testRegisterReviewSuccess() {
        when(userDAO.findById(1)).thenReturn(Optional.of(user));
        when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));
        when(reviewDao.save(any(Review.class))).thenReturn(review);

        Optional<ReviewDTO> result = reviewService.registerReview(review, 1, 1);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getRating());
        assertEquals("Great stay!", result.get().getReviewText());
    }


    @Test
    void testRegisterReviewFailure() {
        when(userDAO.findById(1)).thenReturn(Optional.empty());
        when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));

        GenericException exception = assertThrows(GenericException.class, () -> {
            reviewService.registerReview(new Review(), 1, 1);
        });

        assertEquals("The hotel or user does not exist", exception.getMessage());
        verify(userDAO, times(1)).findById(1);
        verify(hotelDAO, times(1)).findById(1);
        verify(reviewDao, never()).save(any(Review.class));
    }
}
