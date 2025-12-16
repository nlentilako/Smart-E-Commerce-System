package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object for Category entity.
 * Handles all database operations related to categories.
 */
public class CategoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);

    /**
     * Finds a category by its unique ID.
     *
     * @param categoryId The ID of the category to find
     * @return The Category object if found, null otherwise
     */
    public Category findById(int categoryId) {
        String query = "SELECT * FROM categories WHERE category_id = ?";
        return DatabaseUtil.queryForObject(query, new Object[]{categoryId}, this::mapResultSetToCategory);
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of all categories
     */
    public List<Category> findAll() {
        String query = "SELECT * FROM categories ORDER BY name";
        return DatabaseUtil.queryForList(query, null, this::mapResultSetToCategory);
    }

    /**
     * Finds categories by name (case-insensitive partial match).
     *
     * @param name The name to search for
     * @return A list of matching categories
     */
    public List<Category> findByName(String name) {
        String query = "SELECT * FROM categories WHERE name LIKE ? ORDER BY name";
        return DatabaseUtil.queryForList(query, new Object[]{"%" + name + "%"}, this::mapResultSetToCategory);
    }

    /**
     * Creates a new category in the database.
     *
     * @param category The category to create
     * @return The ID of the newly created category
     */
    public int create(Category category) {
        String query = "INSERT INTO categories (name, description, parent_category_id) VALUES (?, ?, ?)";
        
        Object[] params = {
            category.getName(),
            category.getDescription(),
            category.getParentCategoryId()
        };
        
        int categoryId = DatabaseUtil.executeInsert(query, params);
        logger.info("Created new category with ID: {}", categoryId);
        return categoryId;
    }

    /**
     * Updates an existing category in the database.
     *
     * @param category The category with updated information
     * @return The number of affected rows
     */
    public int update(Category category) {
        String query = "UPDATE categories SET name = ?, description = ?, parent_category_id = ?, updated_at = ? " +
                      "WHERE category_id = ?";
        
        Object[] params = {
            category.getName(),
            category.getDescription(),
            category.getParentCategoryId(),
            category.getUpdatedAt(),
            category.getCategoryId()
        };
        
        int result = DatabaseUtil.executeUpdate(query, params);
        logger.info("Updated category with ID: {}, {} rows affected", category.getCategoryId(), result);
        return result;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId The ID of the category to delete
     * @return The number of affected rows
     */
    public int delete(int categoryId) {
        String query = "DELETE FROM categories WHERE category_id = ?";
        int result = DatabaseUtil.executeUpdate(query, new Object[]{categoryId});
        logger.info("Deleted category with ID: {}, {} rows affected", categoryId, result);
        return result;
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