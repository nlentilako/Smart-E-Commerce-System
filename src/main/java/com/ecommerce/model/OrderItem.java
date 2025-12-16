package com.ecommerce.model;

import java.math.BigDecimal;

/**
 * Represents an item in an order in the e-commerce system.
 * This class encapsulates the relationship between an order and a product.
 */
public class OrderItem {
    private final int orderItemId;
    private final int orderId;
    private final int productId;
    private int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal totalPrice;

    /**
     * Constructor for creating a new OrderItem object.
     *
     * @param orderItemId The unique identifier for the order item
     * @param orderId     The ID of the associated order
     * @param productId   The ID of the associated product
     * @param quantity    The quantity of the product in the order
     * @param unitPrice   The price per unit of the product at the time of order
     */
    public OrderItem(int orderItemId, int orderId, int productId, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters
    public int getOrderItemId() { return orderItemId; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    // Setter (with validation)
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
        // Recalculate total price when quantity changes
        // Note: In a real system, unit price might be fixed at order time
    }

    /**
     * Calculates the total price for this order item based on quantity and unit price.
     *
     * @return The total price for this item
     */
    public BigDecimal calculateTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Checks if the item quantity can be modified based on order status.
     * In a real system, this might check if the order is still modifiable.
     *
     * @return true if the quantity can be modified, false otherwise
     */
    public boolean canModifyQuantity() {
        // In a real system, this would check the order status
        // For now, we'll assume modification is allowed
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        OrderItem orderItem = (OrderItem) obj;
        return orderItemId == orderItem.orderItemId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderItemId);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}