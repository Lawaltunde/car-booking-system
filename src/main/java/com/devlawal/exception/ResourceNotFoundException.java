package com.devlawal.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * Use this for missing users, cars, bookings, etc.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s with identifier '%s' not found", resourceType, identifier));
    }
}
