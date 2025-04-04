package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDAO extends JpaRepository<Booking, Integer> {
}
