package com.Rev.RevStay.exceptions;

/**
 * Custom exception class representing a scenario where a room is not available
 * for booking.
 * 
 * This exception is thrown when a user attempts to book a room that is
 * unavailable
 * for the specified dates or conditions.
 * 
 * Extends:
 * - `RuntimeException`: Allows this exception to be thrown without requiring
 * explicit handling.
 */
public class RoomNotAvailableException extends RuntimeException {

    /**
     * Constructor for RoomNotAvailableException.
     * 
     * @param message The error message describing the reason for the exception.
     */
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
