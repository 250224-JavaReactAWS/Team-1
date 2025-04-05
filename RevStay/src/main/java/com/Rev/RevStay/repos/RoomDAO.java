package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomDAO extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> getRoomsByHotelId(@Param("hotelId") int hotelId);

}
