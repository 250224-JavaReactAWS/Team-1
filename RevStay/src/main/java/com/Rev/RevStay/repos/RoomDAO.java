package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Review;
import com.Rev.RevStay.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomDAO extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> getRoomsByHotelId(@Param("hotelId") int hotelId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Room r WHERE r.hotel.name = :hotelName AND r.roomType = :roomType " +
           "AND r.id NOT IN (SELECT b.room.id FROM Booking b WHERE " +
           "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))")
    boolean checkRoomAvailability(@Param("hotelName") Hotel hotel, 
                                  @Param("checkInDate") LocalDateTime checkInDate, 
                                  @Param("checkOutDate") LocalDateTime checkOutDate, 
                                  @Param("roomType") Room roomType);

}
