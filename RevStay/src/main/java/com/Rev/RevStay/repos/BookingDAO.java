package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookingDAO extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByBookId(int book_Id);

    // Alternative approach for room availability verification
    @Query("SELECT COUNT(r) = 0 " +
           "FROM Room r LEFT JOIN Booking b ON r.id = b.room.id " +
           "WHERE r.hotel.id = :hotelId AND r.id = :room_id " +
           "AND (b.id IS NULL OR b.checkIn > :checkOut OR b.checkOut < :checkIn)")
    boolean isRoomAvailable(@Param("hotelId") int hotelId, 
                            @Param("checkIn") LocalDateTime checkIn, 
                            @Param("checkOut") LocalDateTime checkOut, 
                            @Param("room_id") int room_id);
}
