package com.devlawal.exception;

/**
 * Exception thrown when a booking operation fails due to business rules.
 * Use this for already booked cars, unavailable users, etc.
 */
public class BookingException extends RuntimeException {
    
    public BookingException(String message) {
        super(message);
    }
    
    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
