package com.Rev.RevStay.controllers;

import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Payment;
import com.Rev.RevStay.models.PaymentStatus;
import com.Rev.RevStay.services.BookingService;
import com.Rev.RevStay.services.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("payments")

public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentsByUser(HttpSession session ) {
        return paymentService.getPaymentsByUserId( (Integer) session.getAttribute("userId"));
    }

    // Get payments by hotelId
    @GetMapping("/hotel/{hotelId}")
    public List<Payment> getPaymentsByHotel(@PathVariable int hotelId) {
        return paymentService.getPaymentsByHotelId(hotelId);
    }

    // Get payments by userId and hotelId
    @GetMapping("/user/{userId}/hotel/{hotelId}")
    public List<Payment> getPaymentsByUserAndHotel(@PathVariable int userId, @PathVariable int hotelId) {
        return paymentService.getPaymentsByUserAndHotelId(userId, hotelId);
    }

    // Get payments by hotelId and status
    @GetMapping("/hotel/{hotelId}/status/{status}")
    public List<Payment> getPaymentsByHotelAndStatus(@PathVariable int hotelId, @PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByHotelAndStatus(hotelId, status);
    }

    // Register a new payment
    @PostMapping("/register")
    public Optional<Payment> registerPayment(@RequestBody Payment payment,
                                             HttpSession session,
                                             @RequestParam int bookId) {
        return paymentService.registerPayment(payment, (Integer) session.getAttribute("userId"), bookId);
    }

    @PutMapping("/{paymentId}/status")
    public Optional<Payment> updatePaymentStatus(@PathVariable int paymentId,
                                                 @RequestParam PaymentStatus newStatus,
                                                 HttpSession session,
                                                 @RequestParam int bookingId) {

        Optional<Payment> updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);

        // If the state of the payment is COMPLETED update the booking status to confirmed
        if (newStatus == PaymentStatus.COMPLETED) {
            bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED, (Integer) session.getAttribute("userId"));
            return updatedPayment;
        }
        //otherwise the state of payment is FAILED

        return updatedPayment;
    }

}
