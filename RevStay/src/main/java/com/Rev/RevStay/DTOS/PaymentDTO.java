package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.PaymentStatus;
import com.Rev.RevStay.models.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {
    private int paymentId;
    private Booking booking;
    private User user;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private LocalDateTime createdAt;




    //Getters and Setters
    public int getPaymentId() { return paymentId; }

    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Booking getBooking() { return booking; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount;}

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() {  return paymentStatus; }

    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
