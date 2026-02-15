package com.devlawal.exception;

/**
 * Exception thrown when input validation fails.
 * Use this for invalid user input, malformed data, or constraint violations.
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
