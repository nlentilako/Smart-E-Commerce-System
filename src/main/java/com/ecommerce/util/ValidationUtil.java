package com.ecommerce.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 * Provides methods for validating various input formats.
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s'-]{2,50}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$"); // Basic international phone format

    /**
     * Validates if the provided email address is in a valid format.
     *
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates if the provided username is in a valid format.
     * Username must be 3-20 alphanumeric characters or underscores.
     *
     * @param username The username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates if the provided name is in a valid format.
     * Name must contain only letters, spaces, hyphens, and apostrophes, and be 2-50 characters.
     *
     * @param name The name to validate
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates if the provided phone number is in a valid format.
     *
     * @param phone The phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s\\-()]", "")).matches();
    }

    /**
     * Validates if the provided string is not null and not empty after trimming.
     *
     * @param value The string to validate
     * @return true if the string is not null and not empty after trimming, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if the provided value is within the specified range.
     *
     * @param value The value to validate
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @return true if the value is within the range, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validates if the provided value is within the specified range.
     *
     * @param value The value to validate
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @return true if the value is within the range, false otherwise
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Validates if the provided value is positive (greater than 0).
     *
     * @param value The value to validate
     * @return true if the value is positive, false otherwise
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates if the provided value is positive (greater than 0).
     *
     * @param value The value to validate
     * @return true if the value is positive, false otherwise
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }

    /**
     * Validates if the provided value is not negative (greater than or equal to 0).
     *
     * @param value The value to validate
     * @return true if the value is not negative, false otherwise
     */
    public static boolean isNotNegative(int value) {
        return value >= 0;
    }

    /**
     * Validates if the provided value is not negative (greater than or equal to 0).
     *
     * @param value The value to validate
     * @return true if the value is not negative, false otherwise
     */
    public static boolean isNotNegative(double value) {
        return value >= 0;
    }

    /**
     * Validates if the provided string length is within the specified range.
     *
     * @param str The string to validate
     * @param minLength The minimum allowed length (inclusive)
     * @param maxLength The maximum allowed length (inclusive)
     * @return true if the string length is within the range, false otherwise
     */
    public static boolean isStringLengthValid(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength <= 0;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validates if the provided string represents a valid URL.
     *
     * @param url The URL string to validate
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        try {
            java.net.URL urlObj = new java.net.URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
}