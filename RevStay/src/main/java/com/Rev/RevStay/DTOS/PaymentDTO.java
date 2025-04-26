package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) class for transferring payment-related data.
 * 
 * This class is used to encapsulate payment information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `Payment` entity.
 * 
 * Fields:
 * - `paymentId`: The unique identifier of the payment.
 * - `amount`: The amount of the payment.
 * - `paymentMethod`: The method used for the payment (e.g., credit card,
 * PayPal).
 * - `paymentStatus`: The status of the payment (e.g., PENDING, COMPLETED,
 * FAILED).
 * - `createdAt`: The timestamp when the payment was created.
 * - `bookingId`: The unique identifier of the booking associated with the
 * payment.
 * - `hotelId`: The unique identifier of the hotel associated with the payment.
 * - `userId`: The unique identifier of the user who made the payment.
 * 
 * Constructors:
 * - Allows creating `PaymentDTO` objects from individual fields or directly
 * from a `Payment` entity.
 */
public class PaymentDTO {

    private int paymentId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private int bookingId;
    private int hotelId;
    private int userId;

    /**
     * Constructor for creating a PaymentDTO with all fields.
     * 
     * @param paymentId     The unique identifier of the payment.
     * @param amount        The amount of the payment.
     * @param paymentMethod The method used for the payment.
     * @param paymentStatus The status of the payment.
     * @param createdAt     The timestamp when the payment was created.
     * @param bookingId     The unique identifier of the booking associated with the
     *                      payment.
     * @param hotelId       The unique identifier of the hotel associated with the
     *                      payment.
     * @param userId        The unique identifier of the user who made the payment.
     */
    public PaymentDTO(int paymentId, BigDecimal amount, String paymentMethod,
            PaymentStatus paymentStatus, LocalDateTime createdAt,
            int bookingId, int hotelId, int userId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.bookingId = bookingId;
        this.hotelId = hotelId;
        this.userId = userId;
    }

    /**
     * Constructor for creating a PaymentDTO from a Payment entity.
     * 
     * @param payment The Payment entity to convert into a PaymentDTO.
     */
    public PaymentDTO(com.Rev.RevStay.models.Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getPaymentStatus();
        this.createdAt = payment.getCreatedAt();
        this.bookingId = payment.getBooking().getBookId();
        this.hotelId = payment.getBooking().getHotel().getHotelId();
        this.userId = payment.getBooking().getUser().getUserId();
    }

    // Getters and setters for all fields.

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
