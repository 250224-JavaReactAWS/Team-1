package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewDao extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.hotel.hotelId = :hotelId")
    List<Review> getReviewsByHotelId(@Param("hotelId") int hotelId);

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId")
    List<Review> getReviewsByUserId(@Param("userId") int userId);

    @Query("SELECT r FROM Review r WHERE r.hotel.hotelId = :hotelId AND r.user.userId = :userId")
    Review getReviewByHotelIdAndUserId(@Param("hotelId") int hotelId, @Param("userId") int userId);

}
