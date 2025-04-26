package com.Rev.RevStay.exceptions;

/**
 * Custom exception class representing an access denial in the system.
 * 
 * This exception is thrown when a user attempts to perform an action
 * they are not authorized to perform.
 * 
 * Extends:
 * - `RuntimeException`: Allows this exception to be thrown without requiring
 * explicit handling.
 */
public class AccessDeniedException extends RuntimeException {

    /**
     * Constructor for AccessDeniedException.
     * 
     * @param message The error message describing the reason for the exception.
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
