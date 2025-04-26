package com.Rev.RevStay.exceptions;

/**
 * Custom exception class representing a generic exception in the system.
 * 
 * This exception is used to handle various error scenarios where a specific
 * exception type is not required.
 * 
 * Extends:
 * - `RuntimeException`: Allows this exception to be thrown without requiring
 * explicit handling.
 */
public class GenericException extends RuntimeException {

    /**
     * Constructor for GenericException.
     * 
     * @param message The error message describing the reason for the exception.
     */
    public GenericException(String message) {
        super(message);
    }
}
