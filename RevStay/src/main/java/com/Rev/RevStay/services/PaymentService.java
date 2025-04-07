package com.Rev.RevStay.services;

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

@Service
public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    @Autowired
    public PaymentService(PaymentDAO paymentDAO, BookingDAO bookingDAO) {
        this.paymentDAO = paymentDAO;
        this.bookingDAO = bookingDAO;
    }

    //Get Payments By UserId
    public List<Payment> getPaymentsByUserId(int userId){ return paymentDAO.getPaymentsByUserId(userId);
    }

    //Get Payments By HotelId
    public List<Payment> getPaymentsByHotelId(int hotelId){
        return paymentDAO.getPaymentsByHotelId(hotelId);
    }

    //Get payments by User and Hotel Id
    public List<Payment> getPaymentsByUserAndHotelId(int userId, int hotelId){
        return paymentDAO.getPaymentsByUserIdAndHotelId(userId, hotelId);
    }

    //Get payments by Hotel and Status
    public List<Payment> getPaymentsByHotelAndStatus(int hotelId, PaymentStatus status){
        return  paymentDAO.getPaymentsByHotelIdAndStatus(hotelId, status);
    }

    //Register new Payment
    public Optional<Payment> registerPayment(Payment paymentNew, int userId, int bookId){

        Optional<Booking> bookingOpt = bookingDAO.findById(bookId);

        if (bookingOpt.isEmpty()) {
            throw new GenericException("Don't find reservations for this user");
        }

        Booking booking =  bookingOpt.get();

        if (booking.getUser().getUserId() != userId){
            throw new GenericException("Wrong user");
        }

        Room room = booking.getRoom();
        if (room == null) {
            return Optional.empty(); // No room associate with the booking Id
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (nights <= 0) {
            return Optional.empty(); // Invalid Dates
        }

        double amount = room.getPrice().doubleValue() * nights;
        paymentNew.setAmount(BigDecimal.valueOf(amount));
        paymentNew.setCreatedAt(LocalDateTime.now());
        paymentNew.setPaymentStatus(PaymentStatus.PENDING);

        return Optional.of(paymentDAO.save(paymentNew));
    }

    //Update the paymentStatus
    public Optional<Payment> updatePaymentStatus(int paymentId, PaymentStatus newStatus) {
        Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);

        if (paymentOpt.isEmpty()) {
            throw new GenericException("Payment not found");
        }

        Payment payment = paymentOpt.get();
        payment.setPaymentStatus(newStatus);

        return Optional.of(paymentDAO.save(payment));
    }

}
