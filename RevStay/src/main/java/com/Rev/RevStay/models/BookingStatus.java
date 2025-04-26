package com.Rev.RevStay.models;

/**
 * Enum representing the status of a booking in the system.
 * 
 * This enum is used to track the current state of a booking:
 * - `PENDING`: The booking is awaiting approval or confirmation.
 * - `ACCEPTED`: The booking has been accepted by the hotel or owner.
 * - `CONFIRMED`: The booking has been confirmed by the user or system.
 * - `CANCELLED`: The booking has been cancelled.
 * - `COMPLETED`: The booking has been completed successfully.
 */
public enum BookingStatus {
        PENDING, ACCEPTED, CONFIRMED, CANCELLED, COMPLETED
}
