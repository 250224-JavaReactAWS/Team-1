package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Payment;
import com.Rev.RevStay.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentDAO extends JpaRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId")
    List<Payment> getPaymentsByUserId(@Param("userId") int userId);

    @Query("SELECT p FROM Payment p WHERE p.booking.hotel.hotelId = :hotelId")
    List<Payment> getPaymentsByHotelId(@Param("hotelId") int hotelId);

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId AND p.booking.hotel.hotelId = :hotelId")
    List<Payment> getPaymentsByUserIdAndHotelId(@Param("userId") int userId, @Param("hotelId") int hotelId);

    @Query("SELECT p FROM Payment p WHERE p.booking.hotel.hotelId = :hotelId AND p.paymentStatus = :status")
    List<Payment> getPaymentsByHotelIdAndStatus(@Param("hotelId") int hotelId, @Param("status") PaymentStatus status);

}
