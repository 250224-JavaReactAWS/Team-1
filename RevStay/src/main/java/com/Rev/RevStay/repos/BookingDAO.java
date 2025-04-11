package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookingDAO extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByBookId(int book_Id);

    @Query("""
    SELECT COUNT(b) = 0
    FROM Booking b
    WHERE b.hotel.hotelId = :hotelId
      AND b.room.roomId = :roomId
      AND b.status <> 'CANCELLED'
      AND (
        b.checkIn < :checkOut AND
        b.checkOut > :checkIn
      )
""")
    boolean isRoomAvailable(@Param("hotelId") int hotelId,
                            @Param("checkIn") LocalDateTime checkIn,
                            @Param("checkOut") LocalDateTime checkOut,
                            @Param("roomId") int roomId);



    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(int userId);

    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
    Optional<Booking> findById(int bookingId);

    @Query("SELECT b FROM Booking b WHERE b.hotel.hotelId = :hotelId")
    List<Booking> findByHotel_HotelId(@Param("hotelId") int hotelId);

    @Query("SELECT b FROM Booking b WHERE b.hotel.hotelId = :hotelId AND b.user.userId = :userId AND b.status = 'COMPLETED'")
    Optional<Booking> findByUserAndHotel(@Param("userId") int userId, @Param("hotelId") int hotelId);


}
