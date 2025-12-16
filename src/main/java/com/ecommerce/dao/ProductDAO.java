package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product entity.
 * Handles all database operations related to products.
 */
public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    /**
     * Finds a product by its unique ID.
     *
     * @param productId The ID of the product to find
     * @return The Product object if found, null otherwise
     */
    public Product findById(int productId) {
        String query = "SELECT p.*, i.quantity_available, i.reserved_quantity " +
                      "FROM products p " +
                      "LEFT JOIN inventory i ON p.product_id = i.product_id " +
                      "WHERE p.product_id = ?";
        return DatabaseUtil.queryForObject(query, new Object[]{productId}, this::mapResultSetToProduct);
    }

    /**
     * Finds all active products.
     *
     * @return A list of all active products
     */
    public List<Product> findAllActive() {
        String query = "SELECT p.*, i.quantity_available, i.reserved_quantity " +
                      "FROM products p " +
                      "LEFT JOIN inventory i ON p.product_id = i.product_id " +
                      "WHERE p.is_active = TRUE " +
                      "ORDER BY p.created_at DESC";
        return DatabaseUtil.queryForList(query, null, this::mapResultSetToProduct);
    }

    /**
     * Finds products by name (case-insensitive partial match).
     *
     * @param name The name to search for
     * @return A list of matching products
     */
    public List<Product> findByName(String name) {
        String query = "SELECT p.*, i.quantity_available, i.reserved_quantity " +
                      "FROM products p " +
                      "LEFT JOIN inventory i ON p.product_id = i.product_id " +
                      "WHERE p.name LIKE ? AND p.is_active = TRUE " +
                      "ORDER BY p.name";
        return DatabaseUtil.queryForList(query, new Object[]{"%" + name + "%"}, this::mapResultSetToProduct);
    }

    /**
     * Finds products by category.
     *
     * @param categoryId The ID of the category to filter by
     * @return A list of products in the specified category
     */
    public List<Product> findByCategory(int categoryId) {
        String query = "SELECT p.*, i.quantity_available, i.reserved_quantity " +
                      "FROM products p " +
                      "JOIN products_categories pc ON p.product_id = pc.product_id " +
                      "LEFT JOIN inventory i ON p.product_id = i.product_id " +
                      "WHERE pc.category_id = ? AND p.is_active = TRUE " +
                      "ORDER BY p.name";
        return DatabaseUtil.queryForList(query, new Object[]{categoryId}, this::mapResultSetToProduct);
    }

    /**
     * Finds products by price range.
     *
     * @param minPrice The minimum price (inclusive)
     * @param maxPrice The maximum price (inclusive)
     * @return A list of products within the price range
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String query = "SELECT p.*, i.quantity_available, i.reserved_quantity " +
                      "FROM products p " +
                      "LEFT JOIN inventory i ON p.product_id = i.product_id " +
                      "WHERE p.price >= ? AND p.price <= ? AND p.is_active = TRUE " +
                      "ORDER BY p.price";
        return DatabaseUtil.queryForList(query, new Object[]{minPrice, maxPrice}, this::mapResultSetToProduct);
    }

    /**
     * Creates a new product in the database.
     *
     * @param product The product to create
     * @return The ID of the newly created product
     */
    public int create(Product product) {
        String query = "INSERT INTO products (name, description, price, sku, weight, dimensions, brand, is_active) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object[] params = {
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getSku(),
            product.getWeight(),
            product.getDimensions(),
            product.getBrand(),
            product.isActive()
        };
        
        int productId = DatabaseUtil.executeInsert(query, params);
        logger.info("Created new product with ID: {}", productId);
        
        // Create inventory record for the new product
        createInventoryRecord(productId);
        
        // Associate with categories if provided
        if (product.getCategories() != null && !product.getCategories().isEmpty()) {
            associateWithCategories(productId, product.getCategories());
        }
        
        return productId;
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The product with updated information
     * @return The number of affected rows
     */
    public int update(Product product) {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, sku = ?, " +
                      "weight = ?, dimensions = ?, brand = ?, is_active = ?, updated_at = ? " +
                      "WHERE product_id = ?";
        
        Object[] params = {
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getSku(),
            product.getWeight(),
            product.getDimensions(),
            product.getBrand(),
            product.isActive(),
            product.getUpdatedAt(),
            product.getProductId()
        };
        
        int result = DatabaseUtil.executeUpdate(query, params);
        logger.info("Updated product with ID: {}, {} rows affected", product.getProductId(), result);
        return result;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param productId The ID of the product to delete
     * @return The number of affected rows
     */
    public int delete(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        int result = DatabaseUtil.executeUpdate(query, new Object[]{productId});
        logger.info("Deleted product with ID: {}, {} rows affected", productId, result);
        return result;
    }

    /**
     * Creates an inventory record for a new product.
     *
     * @param productId The ID of the product
     */
    private void createInventoryRecord(int productId) {
        String query = "INSERT INTO inventory (product_id, quantity_available, reserved_quantity, reorder_level) " +
                      "VALUES (?, 0, 0, 10)";
        DatabaseUtil.executeUpdate(query, new Object[]{productId});
    }

    /**
     * Associates a product with multiple categories.
     *
     * @param productId The ID of the product
     * @param categories The list of categories to associate with
     */
    private void associateWithCategories(int productId, List<Category> categories) {
        String deleteQuery = "DELETE FROM products_categories WHERE product_id = ?";
        DatabaseUtil.executeUpdate(deleteQuery, new Object[]{productId});
        
        String insertQuery = "INSERT INTO products_categories (product_id, category_id) VALUES (?, ?)";
        for (Category category : categories) {
            DatabaseUtil.executeUpdate(insertQuery, new Object[]{productId, category.getCategoryId()});
        }
    }

    /**
     * Gets categories associated with a product.
     *
     * @param productId The ID of the product
     * @return A list of categories associated with the product
     */
    public List<Category> getCategoriesForProduct(int productId) {
        String query = "SELECT c.* FROM categories c " +
                      "JOIN products_categories pc ON c.category_id = pc.category_id " +
                      "WHERE pc.product_id = ?";
        return DatabaseUtil.queryForList(query, new Object[]{productId}, this::mapResultSetToCategory);
    }

    /**
     * Maps a ResultSet row to a Product object.
     *
     * @param rs The ResultSet to map from
     * @return The mapped Product object
     * @throws SQLException if there's an error during mapping
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        BigDecimal price = rs.getBigDecimal("price");
        String sku = rs.getString("sku");
        BigDecimal weight = rs.getBigDecimal("weight");
        String dimensions = rs.getString("dimensions");
        String brand = rs.getString("brand");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        boolean isActive = rs.getBoolean("is_active");

        // Get associated categories
        List<Category> categories = getCategoriesForProduct(productId);

        return new Product(productId, name, description, price, sku, weight, dimensions, brand,
                          createdAt, updatedAt, isActive, categories);
    }

    /**
     * Maps a ResultSet row to a Category object.
     *
     * @param rs The ResultSet to map from
     * @return The mapped Category object
     * @throws SQLException if there's an error during mapping
     */
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        int categoryId = rs.getInt("category_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Integer parentCategoryId = rs.getObject("parent_category_id", Integer.class);
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        return new Category(categoryId, name, description, parentCategoryId, createdAt, updatedAt);
    }
}