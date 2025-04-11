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
            throw new GenericException("This booking has no assigned room. Cannot calculate total.");
        }

        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            throw new GenericException("Booking dates are incomplete.");
        }

        long nights = ChronoUnit.DAYS.between(
                booking.getCheckIn().toLocalDate(),
                booking.getCheckOut().toLocalDate()
        );

        if (nights <= 0) {
            nights = 1;
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
    public Optional<PaymentDTO> updatePaymentStatus(int paymentId, PaymentStatus newStatus, int userId, String role, int bookingId) {
        Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new GenericException("Payment not found");
        }

        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new GenericException("Booking not found"));

        if (!"OWNER".equals(role) || booking.getHotel().getOwner().getUserId() != userId) {
            throw new GenericException("Not authorized to update this payment status");
        }

        Payment payment = paymentOpt.get();
        payment.setPaymentStatus(newStatus);

        return Optional.of(convertToDTO(paymentDAO.save(payment)));
    }

}
