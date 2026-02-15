package com.devlawal.util;

import com.devlawal.exception.ValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 */
public class ValidationUtil {
    
    // RFC 5322 compliant email regex (simplified version)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 120;
    
    private ValidationUtil() {
        // Prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Validates email format.
     * 
     * @param email the email to validate
     * @throws ValidationException if email is null, empty, or invalid format
     */
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }
    
    /**
     * Validates age is within acceptable range.
     * 
     * @param age the age to validate
     * @throws ValidationException if age is out of range
     */
    public static void validateAge(int age) {
        if (age < MIN_AGE) {
            throw new ValidationException(
                String.format("Age must be at least %d years old (provided: %d)", MIN_AGE, age)
            );
        }
        
        if (age > MAX_AGE) {
            throw new ValidationException(
                String.format("Age must be less than %d years old (provided: %d)", MAX_AGE, age)
            );
        }
    }
    
    /**
     * Validates that a string is not null or empty.
     * 
     * @param value the string to validate
     * @param fieldName the name of the field (for error message)
     * @throws ValidationException if string is null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }
    
    /**
     * Checks if email format is valid (boolean version).
     * 
     * @param email the email to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
