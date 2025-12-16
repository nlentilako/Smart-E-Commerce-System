package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Represents a category in the e-commerce system.
 * This class provides hierarchical categorization for products.
 */
public class Category {
    private final int categoryId;
    private String name;
    private String description;
    private Integer parentCategoryId; // Null if this is a top-level category
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor for creating a new Category object.
     *
     * @param categoryId       The unique identifier for the category
     * @param name             The name of the category
     * @param description      The description of the category
     * @param parentCategoryId The ID of the parent category (null for top-level categories)
     * @param createdAt        The timestamp when the category was created
     * @param updatedAt        The timestamp when the category was last updated
     */
    public Category(int categoryId, String name, String description, Integer parentCategoryId,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    // Getters
    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Integer getParentCategoryId() { return parentCategoryId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters (with update timestamp tracking)
    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Category category = (Category) obj;
        return categoryId == category.categoryId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(categoryId);
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                '}';
    }
}