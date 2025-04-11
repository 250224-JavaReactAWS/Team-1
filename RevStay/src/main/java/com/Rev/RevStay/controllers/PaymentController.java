package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.PaymentDTO;
import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Payment;
import com.Rev.RevStay.models.PaymentStatus;
import com.Rev.RevStay.services.BookingService;
import com.Rev.RevStay.services.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("payments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @GetMapping("/user/{userId}")
    public List<PaymentDTO> getPaymentsByUser(HttpSession session ) {
        return paymentService.getPaymentsByUserId( (Integer) session.getAttribute("userId"));
    }

    // Get payments by hotelId
    @GetMapping("/hotel/{hotelId}")
    public List<PaymentDTO> getPaymentsByHotel(@PathVariable int hotelId) {
        return paymentService.getPaymentsByHotelId(hotelId);
    }

    // Get payments by userId and hotelId
    @GetMapping("/user/{userId}/hotel/{hotelId}")
    public List<PaymentDTO> getPaymentsByUserAndHotel(@PathVariable int userId, @PathVariable int hotelId) {
        return paymentService.getPaymentsByUserAndHotelId(userId, hotelId);
    }

    // Get payments by hotelId and status
    @GetMapping("/hotel/{hotelId}/status/{status}")
    public List<PaymentDTO> getPaymentsByHotelAndStatus(@PathVariable int hotelId, @PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByHotelAndStatus(hotelId, status);
    }

    @PostMapping("/register")
    public ResponseEntity<PaymentDTO> registerPayment(@RequestBody Payment payment,
                                                      HttpSession session,
                                                      @RequestParam int bookId) {
        Integer userId = (Integer) session.getAttribute("userId");
        Optional<PaymentDTO> created = paymentService.registerPayment(payment, userId, bookId);
        return created.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable int paymentId,
                                                          @RequestParam PaymentStatus newStatus,
                                                          HttpSession session,
                                                          @RequestParam int bookingId) {
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        Optional<PaymentDTO> updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus, userId, role, bookingId);

        if (newStatus == PaymentStatus.COMPLETED) {
            bookingService.markBookingAsConfirmed(bookingId);
        } else if (newStatus == PaymentStatus.FAILED) {
            bookingService.markBookingAsAccepted(bookingId);
        }

        return updatedPayment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
