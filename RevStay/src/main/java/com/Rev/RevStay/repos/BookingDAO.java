package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

    Optional<Booking> saveReservation(Hotel hotel, LocalDateTime checkInDate, LocalDateTime checkOutDate, Room roomType, int numberOfGuests);

    boolean checkRoomAvailability(String checkInDate, String checkOutDate, String roomType, String roomType2);
}
