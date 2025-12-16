package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents inventory information for a product in the e-commerce system.
 * This class manages stock levels and reorder thresholds.
 */
public class Inventory {
    private final int inventoryId;
    private final int productId;
    private int quantityAvailable;
    private int reservedQuantity;
    private int reorderLevel;
    private final LocalDateTime lastUpdated;

    /**
     * Constructor for creating a new Inventory object.
     *
     * @param inventoryId       The unique identifier for the inventory record
     * @param productId         The ID of the associated product
     * @param quantityAvailable The available quantity of the product
     * @param reservedQuantity  The quantity reserved for pending orders
     * @param reorderLevel      The threshold quantity that triggers reorder notifications
     * @param lastUpdated       The timestamp when the inventory was last updated
     */
    public Inventory(int inventoryId, int productId, int quantityAvailable, int reservedQuantity,
                     int reorderLevel, LocalDateTime lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantityAvailable = Math.max(0, quantityAvailable); // Ensure non-negative
        this.reservedQuantity = Math.max(0, reservedQuantity);   // Ensure non-negative
        this.reorderLevel = Math.max(0, reorderLevel);           // Ensure non-negative
        this.lastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.now();
    }

    // Getters
    public int getInventoryId() { return inventoryId; }
    public int getProductId() { return productId; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public int getReservedQuantity() { return reservedQuantity; }
    
    /**
     * Gets the total stock (available + reserved)
     *
     * @return Total stock count
     */
    public int getTotalStock() {
        return quantityAvailable + reservedQuantity;
    }
    
    /**
     * Gets the available quantity for sale (excluding reserved items)
     *
     * @return Quantity available for new orders
     */
    public int getAvailableForSale() {
        return Math.max(0, quantityAvailable - reservedQuantity);
    }
    
    public int getReorderLevel() { return reorderLevel; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters (with validation)
    public void setQuantityAvailable(int quantityAvailable) {
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negative");
        }
        this.quantityAvailable = quantityAvailable;
    }

    public void setReservedQuantity(int reservedQuantity) {
        if (reservedQuantity < 0) {
            throw new IllegalArgumentException("Reserved quantity cannot be negative");
        }
        this.reservedQuantity = reservedQuantity;
    }

    public void setReorderLevel(int reorderLevel) {
        if (reorderLevel < 0) {
            throw new IllegalArgumentException("Reorder level cannot be negative");
        }
        this.reorderLevel = reorderLevel;
    }

    /**
     * Updates the quantity available by adding the specified amount.
     *
     * @param amount The amount to add to available quantity
     */
    public void increaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to increase cannot be negative");
        }
        this.quantityAvailable += amount;
    }

    /**
     * Attempts to reserve the specified quantity for an order.
     *
     * @param quantity The quantity to reserve
     * @return true if reservation successful, false otherwise
     */
    public boolean reserveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reservation quantity must be positive");
        }
        
        if (getAvailableForSale() >= quantity) {
            this.reservedQuantity += quantity;
            return true;
        }
        return false;
    }

    /**
     * Releases the specified quantity from reservation.
     *
     * @param quantity The quantity to release from reservation
     */
    public void releaseReservation(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Release quantity must be positive");
        }
        
        this.reservedQuantity = Math.max(0, this.reservedQuantity - quantity);
    }

    /**
     * Checks if the inventory level is below the reorder threshold.
     *
     * @return true if below reorder level, false otherwise
     */
    public boolean isBelowReorderLevel() {
        return getAvailableForSale() <= reorderLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Inventory inventory = (Inventory) obj;
        return inventoryId == inventory.inventoryId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(inventoryId);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", quantityAvailable=" + quantityAvailable +
                ", reservedQuantity=" + reservedQuantity +
                ", availableForSale=" + getAvailableForSale() +
                ", reorderLevel=" + reorderLevel +
                ", isBelowReorderLevel=" + isBelowReorderLevel() +
                '}';
    }
}