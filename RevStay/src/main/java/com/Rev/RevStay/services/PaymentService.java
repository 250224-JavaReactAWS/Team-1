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

/**
 * Service class for managing payment-related operations such as retrieving,
 * registering,
 * and updating payments.
 * 
 * This class provides methods to:
 * - Retrieve payments by user ID.
 * - Retrieve payments by hotel ID.
 * - Retrieve payments by user and hotel ID.
 * - Retrieve payments by hotel and payment status.
 * - Register a new payment.
 * - Update the status of an existing payment.
 * 
 * It uses `PaymentDAO` and `BookingDAO` for database interactions.
 * 
 * Exceptions:
 * - Throws `GenericException` for invalid inputs, unauthorized actions, or when
 * required entities are not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 */
@Service
public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    /**
     * Constructor for PaymentService.
     * 
     * @param paymentDAO Data access object for payment-related operations.
     * @param bookingDAO Data access object for booking-related operations.
     */
    @Autowired
    public PaymentService(PaymentDAO paymentDAO, BookingDAO bookingDAO) {
        this.paymentDAO = paymentDAO;
        this.bookingDAO = bookingDAO;
    }

    /**
     * Retrieves all payments made by a specific user.
     * 
     * @param userId The ID of the user whose payments are to be retrieved.
     * @return A list of PaymentDTOs for the payments made by the specified user.
     */
    public List<PaymentDTO> getPaymentsByUserId(int userId) {
        return paymentDAO.getPaymentsByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all payments associated with a specific hotel.
     * 
     * @param hotelId The ID of the hotel whose payments are to be retrieved.
     * @return A list of PaymentDTOs for the payments associated with the specified
     *         hotel.
     */
    public List<PaymentDTO> getPaymentsByHotelId(int hotelId) {
        return paymentDAO.getPaymentsByHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all payments made by a specific user for a specific hotel.
     * 
     * @param userId  The ID of the user.
     * @param hotelId The ID of the hotel.
     * @return A list of PaymentDTOs for the payments made by the user for the
     *         hotel.
     */
    public List<PaymentDTO> getPaymentsByUserAndHotelId(int userId, int hotelId) {
        return paymentDAO.getPaymentsByUserIdAndHotelId(userId, hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all payments associated with a specific hotel and payment status.
     * 
     * @param hotelId The ID of the hotel.
     * @param status  The payment status to filter by.
     * @return A list of PaymentDTOs for the payments matching the criteria.
     */
    public List<PaymentDTO> getPaymentsByHotelAndStatus(int hotelId, PaymentStatus status) {
        return paymentDAO.getPaymentsByHotelIdAndStatus(hotelId, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Registers a new payment for a booking.
     * 
     * @param paymentNew The payment to be registered.
     * @param userId     The ID of the user making the payment.
     * @param bookId     The ID of the booking associated with the payment.
     * @return An Optional containing the registered PaymentDTO.
     * @throws GenericException if the booking does not exist, the user is
     *                          incorrect,
     *                          or the booking details are incomplete.
     */
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
                booking.getCheckOut().toLocalDate());

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

    /**
     * Updates the status of an existing payment.
     * 
     * @param paymentId The ID of the payment to be updated.
     * @param newStatus The new payment status.
     * @param userId    The ID of the user attempting to update the payment.
     * @param role      The role of the user (e.g., OWNER).
     * @param bookingId The ID of the booking associated with the payment.
     * @return An Optional containing the updated PaymentDTO.
     * @throws GenericException if the payment or booking does not exist, or the
     *                          user is not authorized.
     */
    public Optional<PaymentDTO> updatePaymentStatus(int paymentId, PaymentStatus newStatus, int userId, String role,
            int bookingId) {
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

    /**
     * Converts a Payment entity to a PaymentDTO.
     * 
     * @param payment The Payment entity to be converted.
     * @return The corresponding PaymentDTO.
     */
    private PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(payment);
    }
}
