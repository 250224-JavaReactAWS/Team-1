package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDAO extends JpaRepository<Payment, Integer> {
}
