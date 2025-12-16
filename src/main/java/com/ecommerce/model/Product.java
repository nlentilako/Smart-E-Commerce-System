package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a product in the e-commerce system.
 * This class encapsulates all product-related information and business logic.
 */
public class Product {
    private final int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private BigDecimal weight;
    private String dimensions;
    private String brand;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private List<Category> categories;

    /**
     * Constructor for creating a new Product object.
     *
     * @param productId   The unique identifier for the product
     * @param name        The name of the product
     * @param description The description of the product
     * @param price       The price of the product
     * @param sku         The stock keeping unit identifier
     * @param weight      The weight of the product
     * @param dimensions  The dimensions of the product (format: "LxWxH")
     * @param brand       The brand of the product
     * @param createdAt   The timestamp when the product was created
     * @param updatedAt   The timestamp when the product was last updated
     * @param isActive    Whether the product is active and available
     * @param categories  The list of categories this product belongs to
     */
    public Product(int productId, String name, String description, BigDecimal price, String sku,
                   BigDecimal weight, String dimensions, String brand, LocalDateTime createdAt,
                   LocalDateTime updatedAt, boolean isActive, List<Category> categories) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.weight = weight;
        this.dimensions = dimensions;
        this.brand = brand;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        this.isActive = isActive;
        this.categories = categories != null ? categories : List.of();
    }

    // Getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getSku() { return sku; }
    public BigDecimal getWeight() { return weight; }
    public String getDimensions() { return dimensions; }
    public String getBrand() { return brand; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return isActive; }
    public List<Category> getCategories() { return categories; }

    // Setters (with update timestamp tracking)
    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void setSku(String sku) {
        this.sku = sku;
        this.updatedAt = LocalDateTime.now();
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
        this.updatedAt = LocalDateTime.now();
    }

    public void setBrand(String brand) {
        this.brand = brand;
        this.updatedAt = LocalDateTime.now();
    }

    public void setActive(boolean active) {
        isActive = active;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Product product = (Product) obj;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", brand='" + brand + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}