package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.PaymentDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.*;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    @Autowired
    public PaymentService(PaymentDAO paymentDAO, BookingDAO bookingDAO) {
        this.paymentDAO = paymentDAO;
        this.bookingDAO = bookingDAO;
    }

    // Helper to convert to DTO
    private PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(payment);
    }

    // Get Payments By UserId
    public List<PaymentDTO> getPaymentsByUserId(int userId) {
        return paymentDAO.getPaymentsByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Payments By HotelId
    public List<PaymentDTO> getPaymentsByHotelId(int hotelId) {
        return paymentDAO.getPaymentsByHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get payments by User and Hotel Id
    public List<PaymentDTO> getPaymentsByUserAndHotelId(int userId, int hotelId) {
        return paymentDAO.getPaymentsByUserIdAndHotelId(userId, hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get payments by Hotel and Status
    public List<PaymentDTO> getPaymentsByHotelAndStatus(int hotelId, PaymentStatus status) {
        return paymentDAO.getPaymentsByHotelIdAndStatus(hotelId, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // Register new Payment
    public Optional<PaymentDTO> registerPayment(Payment paymentNew, int userId, int bookId) {
        Optional<Booking> bookingOpt = bookingDAO.findById(bookId);

        if (bookingOpt.isEmpty()) {
            throw new GenericException("Don't find reservations for this user");
        }

        Booking booking = bookingOpt.get();

        if (booking.getUser().getUserId() != userId) {
            throw new GenericException("Wrong user");
        }

        Room room = booking.getRoom();
        if (room == null) {
            return Optional.empty();
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (nights <= 0) {
            return Optional.empty();
        }

        double amount = room.getPrice().doubleValue() * nights;
        paymentNew.setAmount(BigDecimal.valueOf(amount));
        paymentNew.setCreatedAt(LocalDateTime.now());
        paymentNew.setPaymentStatus(PaymentStatus.PENDING);
        paymentNew.setBooking(booking);
        paymentNew.setUser(booking.getUser());


        return Optional.of(convertToDTO(paymentDAO.save(paymentNew)));
    }

    // Update the paymentStatus
    public Optional<PaymentDTO> updatePaymentStatus(int paymentId, PaymentStatus newStatus) {
        Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);

        if (paymentOpt.isEmpty()) {
            throw new GenericException("Payment not found");
        }

        Payment payment = paymentOpt.get();
        payment.setPaymentStatus(newStatus);

        return Optional.of(convertToDTO(paymentDAO.save(payment)));
    }

}
