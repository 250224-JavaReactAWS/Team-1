package com.Rev.RevStay.models;

/**
 * Enum representing the status of a payment in the system.
 * 
 * This enum is used to track the current state of a payment:
 * - `PENDING`: The payment is awaiting processing or confirmation.
 * - `COMPLETED`: The payment has been successfully processed.
 * - `FAILED`: The payment has failed due to an error or issue.
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED
}
