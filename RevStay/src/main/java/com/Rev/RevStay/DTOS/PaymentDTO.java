package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {
    private int paymentId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private int bookingId;
    private int hotelId;
    private int userId;

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
