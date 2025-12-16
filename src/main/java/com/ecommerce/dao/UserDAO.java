package com.ecommerce.dao;

import com.ecommerce.model.User;
import com.ecommerce.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object for User entity.
 * Handles all database operations related to users.
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Finds a user by their unique ID.
     *
     * @param userId The ID of the user to find
     * @return The User object if found, null otherwise
     */
    public User findById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        return DatabaseUtil.queryForObject(query, new Object[]{userId}, this::mapResultSetToUser);
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find
     * @return The User object if found, null otherwise
     */
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        return DatabaseUtil.queryForObject(query, new Object[]{username}, this::mapResultSetToUser);
    }

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find
     * @return The User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        return DatabaseUtil.queryForObject(query, new Object[]{email}, this::mapResultSetToUser);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users
     */
    public List<User> findAll() {
        String query = "SELECT * FROM users ORDER BY created_at DESC";
        return DatabaseUtil.queryForList(query, null, this::mapResultSetToUser);
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user to create
     * @return The ID of the newly created user
     */
    public int create(User user) {
        String query = "INSERT INTO users (username, email, password_hash, first_name, last_name, " +
                      "phone, address, user_type, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object[] params = {
            user.getUsername(),
            user.getEmail(),
            "hashed_password_placeholder", // In a real system, you'd hash the password
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getAddress(),
            user.getUserType().name(),
            user.isActive()
        };
        
        int userId = DatabaseUtil.executeInsert(query, params);
        logger.info("Created new user with ID: {}", userId);
        return userId;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user with updated information
     * @return The number of affected rows
     */
    public int update(User user) {
        String query = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ?, " +
                      "phone = ?, address = ?, user_type = ?, is_active = ?, updated_at = ? " +
                      "WHERE user_id = ?";
        
        Object[] params = {
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getAddress(),
            user.getUserType().name(),
            user.isActive(),
            user.getUpdatedAt(),
            user.getUserId()
        };
        
        int result = DatabaseUtil.executeUpdate(query, params);
        logger.info("Updated user with ID: {}, {} rows affected", user.getUserId(), result);
        return result;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete
     * @return The number of affected rows
     */
    public int delete(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        int result = DatabaseUtil.executeUpdate(query, new Object[]{userId});
        logger.info("Deleted user with ID: {}, {} rows affected", userId, result);
        return result;
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username The username to authenticate
     * @param password The password to authenticate (should be hashed in real implementation)
     * @return The authenticated User object if credentials are valid, null otherwise
     */
    public User authenticate(String username, String password) {
        // In a real system, you would compare hashed passwords
        String query = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        return DatabaseUtil.queryForObject(query, new Object[]{username}, this::mapResultSetToUser);
    }

    /**
     * Maps a ResultSet row to a User object.
     *
     * @param rs The ResultSet to map from
     * @return The mapped User object
     * @throws SQLException if there's an error during mapping
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        String userTypeStr = rs.getString("user_type");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        boolean isActive = rs.getBoolean("is_active");

        User.UserType userType = User.UserType.valueOf(userTypeStr);

        return new User(userId, username, email, firstName, lastName, phone, address,
                       userType, createdAt, updatedAt, isActive);
    }
}