package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an order in the e-commerce system.
 * This class encapsulates all order-related information and business logic.
 */
public class Order {
    private final int orderId;
    private final int userId;
    private OrderStatus orderStatus;
    private final BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private final LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private String notes;
    private List<OrderItem> orderItems;

    /**
     * Constructor for creating a new Order object.
     *
     * @param orderId        The unique identifier for the order
     * @param userId         The ID of the user who placed the order
     * @param orderStatus    The current status of the order
     * @param totalAmount    The total amount of the order
     * @param shippingAddress The shipping address for the order
     * @param billingAddress  The billing address for the order
     * @param paymentMethod   The payment method used for the order
     * @param orderDate       The date when the order was placed
     * @param shippedDate     The date when the order was shipped (null if not shipped)
     * @param deliveredDate   The date when the order was delivered (null if not delivered)
     * @param notes          Additional notes about the order
     * @param orderItems     The list of items in the order
     */
    public Order(int orderId, int userId, OrderStatus orderStatus, BigDecimal totalAmount,
                 String shippingAddress, String billingAddress, String paymentMethod,
                 LocalDateTime orderDate, LocalDateTime shippedDate, LocalDateTime deliveredDate,
                 String notes, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.shippedDate = shippedDate;
        this.deliveredDate = deliveredDate;
        this.notes = notes;
        this.orderItems = orderItems != null ? orderItems : List.of();
    }

    // Getters
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public String getBillingAddress() { return billingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getShippedDate() { return shippedDate; }
    public LocalDateTime getDeliveredDate() { return deliveredDate; }
    public String getNotes() { return notes; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    // Setters (with business logic validation)
    public void setOrderStatus(OrderStatus orderStatus) {
        if (!this.orderStatus.canTransitionTo(orderStatus)) {
            throw new IllegalArgumentException(
                "Cannot transition from " + this.orderStatus + " to " + orderStatus
            );
        }
        
        this.orderStatus = orderStatus;
        
        // Update timestamps based on status changes
        if (orderStatus == OrderStatus.SHIPPED && this.shippedDate == null) {
            this.shippedDate = LocalDateTime.now();
        } else if (orderStatus == OrderStatus.DELIVERED && this.deliveredDate == null) {
            this.deliveredDate = LocalDateTime.now();
        }
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * Updates the order status to the next status in the lifecycle if possible.
     *
     * @return true if status was updated, false if no next status exists
     */
    public boolean advanceStatus() {
        OrderStatus nextStatus = orderStatus.getNextStatus();
        if (nextStatus != null) {
            setOrderStatus(nextStatus);
            return true;
        }
        return false;
    }

    /**
     * Cancels the order if it's not already in a final state.
     *
     * @return true if the order was cancelled, false if already in final state
     */
    public boolean cancelOrder() {
        if (orderStatus.isFinalState()) {
            return false; // Cannot cancel an order that is already delivered or cancelled
        }
        setOrderStatus(OrderStatus.CANCELLED);
        return true;
    }

    /**
     * Checks if the order can be cancelled.
     *
     * @return true if the order can be cancelled, false otherwise
     */
    public boolean canBeCancelled() {
        return !orderStatus.isFinalState() && orderStatus != OrderStatus.SHIPPED;
    }

    /**
     * Gets the total quantity of items in the order.
     *
     * @return Total quantity of items
     */
    public int getTotalItemCount() {
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Order order = (Order) obj;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderStatus=" + orderStatus +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                ", totalItems=" + getTotalItemCount() +
                '}';
    }
}