package com.Rev.RevStay.controllers;

import com.Rev.RevStay.DTOS.PaymentDTO;
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

/**
 * REST controller for managing payment-related operations.
 * 
 * This controller provides endpoints for:
 * - Retrieving payments by user ID.
 * - Retrieving payments by hotel ID.
 * - Retrieving payments by user ID and hotel ID.
 * - Retrieving payments by hotel ID and payment status.
 * - Registering a new payment.
 * - Updating the status of a payment.
 * 
 * Annotations:
 * - `@RestController`: Marks this class as a REST controller.
 * - `@RequestMapping`: Maps requests to the `/payments` base path.
 * - `@CrossOrigin`: Enables cross-origin requests from the specified origin.
 */
@RestController
@RequestMapping("payments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    /**
     * Constructor for PaymentController.
     * 
     * @param paymentService The service layer for payment-related operations.
     * @param bookingService The service layer for booking-related operations.
     */
    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    /**
     * Endpoint for retrieving payments by user ID.
     * 
     * @param session The HTTP session to retrieve user details.
     * @return A list of PaymentDTOs for the specified user.
     */
    @GetMapping("/user/{userId}")
    public List<PaymentDTO> getPaymentsByUser(HttpSession session) {
        return paymentService.getPaymentsByUserId((Integer) session.getAttribute("userId"));
    }

    /**
     * Endpoint for retrieving payments by hotel ID.
     * 
     * @param hotelId The ID of the hotel to retrieve payments for.
     * @return A list of PaymentDTOs for the specified hotel.
     */
    @GetMapping("/hotel/{hotelId}")
    public List<PaymentDTO> getPaymentsByHotel(@PathVariable int hotelId) {
        return paymentService.getPaymentsByHotelId(hotelId);
    }

    /**
     * Endpoint for retrieving payments by user ID and hotel ID.
     * 
     * @param userId  The ID of the user.
     * @param hotelId The ID of the hotel.
     * @return A list of PaymentDTOs for the specified user and hotel.
     */
    @GetMapping("/user/{userId}/hotel/{hotelId}")
    public List<PaymentDTO> getPaymentsByUserAndHotel(@PathVariable int userId, @PathVariable int hotelId) {
        return paymentService.getPaymentsByUserAndHotelId(userId, hotelId);
    }

    /**
     * Endpoint for retrieving payments by hotel ID and payment status.
     * 
     * @param hotelId The ID of the hotel.
     * @param status  The payment status to filter by.
     * @return A list of PaymentDTOs for the specified hotel and status.
     */
    @GetMapping("/hotel/{hotelId}/status/{status}")
    public List<PaymentDTO> getPaymentsByHotelAndStatus(@PathVariable int hotelId, @PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByHotelAndStatus(hotelId, status);
    }

    /**
     * Endpoint for registering a new payment.
     * 
     * @param payment The payment details to register.
     * @param session The HTTP session to retrieve user details.
     * @param bookId  The ID of the booking associated with the payment.
     * @return A ResponseEntity containing the created PaymentDTO or a bad request
     *         status.
     */
    @PostMapping("/register")
    public ResponseEntity<PaymentDTO> registerPayment(@RequestBody Payment payment,
            HttpSession session,
            @RequestParam int bookId) {
        Integer userId = (Integer) session.getAttribute("userId");
        Optional<PaymentDTO> created = paymentService.registerPayment(payment, userId, bookId);
        return created.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for updating the status of a payment.
     * 
     * @param paymentId The ID of the payment to update.
     * @param newStatus The new status to set for the payment.
     * @param session   The HTTP session to retrieve user details.
     * @param bookingId The ID of the booking associated with the payment.
     * @return A ResponseEntity containing the updated PaymentDTO or a bad request
     *         status.
     */
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable int paymentId,
            @RequestParam PaymentStatus newStatus,
            HttpSession session,
            @RequestParam int bookingId) {
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        Optional<PaymentDTO> updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus, userId, role,
                bookingId);

        if (newStatus == PaymentStatus.COMPLETED) {
            bookingService.markBookingAsConfirmed(bookingId);
        } else if (newStatus == PaymentStatus.FAILED) {
            bookingService.markBookingAsAccepted(bookingId);
        }

        return updatedPayment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
