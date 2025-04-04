package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.Hotel;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

    Optional<Booking> saveReservation(Hotel hotel, Date checkInDate2, Date checkOutDate, String roomType2, int numberOfGuests);

    boolean checkRoomAvailability(String checkInDate, String checkOutDate, String roomType, String roomType2);
}
