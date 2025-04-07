package com.Rev.RevStay.services;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.Payment;
import com.Rev.RevStay.models.PaymentStatus;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
