package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Represents a user in the e-commerce system.
 * This class follows the Value Object pattern with immutable properties after creation.
 */
public class User {
    private final int userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private UserType userType;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;

    /**
     * Constructor for creating a new User object.
     *
     * @param userId    The unique identifier for the user
     * @param username  The username for login
     * @param email     The email address of the user
     * @param firstName The first name of the user
     * @param lastName  The last name of the user
     * @param phone     The phone number of the user
     * @param address   The address of the user
     * @param userType  The type of user (CUSTOMER or ADMIN)
     * @param createdAt The timestamp when the user was created
     * @param updatedAt The timestamp when the user was last updated
     * @param isActive  Whether the user account is active
     */
    public User(int userId, String username, String email, String firstName, String lastName,
                String phone, String address, UserType userType, LocalDateTime createdAt,
                LocalDateTime updatedAt, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.userType = userType;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        this.isActive = isActive;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public UserType getUserType() { return userType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return isActive; }

    // Setters (with update timestamp tracking)
    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.updatedAt = LocalDateTime.now();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
        this.updatedAt = LocalDateTime.now();
    }

    public void setActive(boolean active) {
        isActive = active;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                ", isActive=" + isActive +
                '}';
    }

    /**
     * Enum representing the type of user in the system.
     */
    public enum UserType {
        CUSTOMER,
        ADMIN
    }
}