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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testReviewById() {
//        Review review = new Review();
//        review.setReviewId(1);
//
//        when(reviewDao.findById(1)).thenReturn(Optional.of(review));
//
//        Optional<ReviewDTO> result = reviewService.reviewById(1);
//
//        assertTrue(result.isPresent());
//        assertEquals(1, result.get().getId());
//        verify(reviewDao, times(1)).findById(1);
//    }
//
//    @Test
//    void testGetReviewsByHotelId() {
//        Review review = new Review();
//        review.setReviewId(1);
//
//        when(reviewDao.getReviewsByHotelId(1)).thenReturn(List.of(review));
//
//        List<ReviewDTO> result = reviewService.getReviewsByHotelId(1);
//
//        assertEquals(1, result.size());
//        assertEquals(1, result.get(0).getId());
//        verify(reviewDao, times(1)).getReviewsByHotelId(1);
//    }
//
//    @Test
//    void testGetReviewsByUserId() {
//        Review review = new Review();
//        review.setReviewId(1);
//
//        when(reviewDao.getReviewsByUserId(1)).thenReturn(List.of(review));
//
//        List<ReviewDTO> result = reviewService.getReviewsByUserId(1);
//
//        assertEquals(1, result.size());
//        assertEquals(1, result.get(0).getId());
//        verify(reviewDao, times(1)).getReviewsByUserId(1);
//    }
//
//    @Test
//    void testGetReviewByUserAndHotelId() {
//        Review review = new Review();
//        review.setReviewId(1);
//
//        when(reviewDao.getReviewByHotelIdAndUserId(1, 1)).thenReturn(review);
//
//        Optional<ReviewDTO> result = reviewService.getReviewByUserAndHotelId(1, 1);
//
//        assertTrue(result.isPresent());
//        assertEquals(1, result.get().getId());
//        verify(reviewDao, times(1)).getReviewByHotelIdAndUserId(1, 1);
//    }
//
//    @Test
//    void testRegisterReviewSuccess() {
//        Review review = new Review();
//        User user = new User();
//        Hotel hotel = new Hotel();
//
//        when(userDAO.findById(1)).thenReturn(Optional.of(user));
//        when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));
//        when(reviewDao.save(any(Review.class))).thenReturn(review);
//
//        Optional<ReviewDTO> result = reviewService.registerReview(review, 1, 1);
//
//        assertTrue(result.isPresent());
//        verify(userDAO, times(1)).findById(1);
//        verify(hotelDAO, times(1)).findById(1);
//        verify(reviewDao, times(1)).save(review);
//    }
//
//    @Test
//    void testRegisterReviewFailure() {
//        when(userDAO.findById(1)).thenReturn(Optional.empty());
//        when(hotelDAO.findById(1)).thenReturn(Optional.empty());
//
//        GenericException exception = assertThrows(GenericException.class, () -> {
//            reviewService.registerReview(new Review(), 1, 1);
//        });
//
//        assertEquals("The hotel or user not exist", exception.getMessage());
//        verify(userDAO, times(1)).findById(1);
//        verify(hotelDAO, times(1)).findById(1);
//        verify(reviewDao, never()).save(any(Review.class));
//    }
}
