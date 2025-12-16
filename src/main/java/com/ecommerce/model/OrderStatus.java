package com.ecommerce.model;

/**
 * Enum representing the possible statuses of an order in the e-commerce system.
 */
public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the next status in the order lifecycle.
     *
     * @return The next status in the sequence, or null if no next status exists
     */
    public OrderStatus getNextStatus() {
        switch (this) {
            case PENDING: return CONFIRMED;
            case CONFIRMED: return SHIPPED;
            case SHIPPED: return DELIVERED;
            default: return null; // No next status for DELIVERED or CANCELLED
        }
    }

    /**
     * Checks if the order can transition to the specified status.
     *
     * @param targetStatus The status to check transition to
     * @return true if transition is allowed, false otherwise
     */
    public boolean canTransitionTo(OrderStatus targetStatus) {
        if (targetStatus == this) {
            return true; // Already in this status
        }
        
        switch (this) {
            case PENDING:
                return targetStatus == CONFIRMED || targetStatus == CANCELLED;
            case CONFIRMED:
                return targetStatus == SHIPPED || targetStatus == CANCELLED;
            case SHIPPED:
                return targetStatus == DELIVERED || targetStatus == CANCELLED;
            case DELIVERED:
                return false; // Can't change status after delivery
            case CANCELLED:
                return false; // Can't change status after cancellation
            default:
                return false;
        }
    }

    /**
     * Checks if the order is in a completed state (delivered or cancelled).
     *
     * @return true if the order is completed, false otherwise
     */
    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * Checks if the order is in a final state (cannot be changed).
     *
     * @return true if the order is in a final state, false otherwise
     */
    public boolean isFinalState() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * Checks if the order is still active (not cancelled or delivered).
     *
     * @return true if the order is active, false otherwise
     */
    public boolean isActive() {
        return !isCompleted();
    }
}