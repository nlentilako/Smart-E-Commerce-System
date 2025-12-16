package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Service class for user-related business logic.
 * Handles user operations with validation and business rules.
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user after validating the input data.
     *
     * @param user The user to register
     * @return The ID of the newly created user
     * @throws IllegalArgumentException if validation fails
     */
    public int registerUser(User user) {
        validateUserForCreation(user);
        
        // Check for duplicate username or email
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        int userId = userDAO.create(user);
        logger.info("Successfully registered new user with ID: {}", userId);
        return userId;
    }

    /**
     * Updates an existing user after validating the input data.
     *
     * @param user The user with updated information
     * @return The number of affected rows
     * @throws IllegalArgumentException if validation fails
     */
    public int updateUser(User user) {
        validateUserForUpdate(user);
        
        // Check for duplicate email (if different from current)
        User existingUser = userDAO.findById(user.getUserId());
        if (existingUser != null && !existingUser.getEmail().equals(user.getEmail())) {
            if (userDAO.findByEmail(user.getEmail()) != null) {
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
        }
        
        // Check for duplicate username (if different from current)
        if (existingUser != null && !existingUser.getUsername().equals(user.getUsername())) {
            if (userDAO.findByUsername(user.getUsername()) != null) {
                throw new IllegalArgumentException("Username already exists: " + user.getUsername());
            }
        }
        
        int result = userDAO.update(user);
        logger.info("Successfully updated user with ID: {}", user.getUserId());
        return result;
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param userId The ID of the user to find
     * @return The User object if found, null otherwise
     */
    public User getUserById(int userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            logger.debug("Retrieved user with ID: {}", userId);
        } else {
            logger.warn("User not found with ID: {}", userId);
        }
        return user;
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find
     * @return The User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            logger.debug("Retrieved user with username: {}", username);
        } else {
            logger.warn("User not found with username: {}", username);
        }
        return user;
    }

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find
     * @return The User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            logger.debug("Retrieved user with email: {}", email);
        } else {
            logger.warn("User not found with email: {}", email);
        }
        return user;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users
     */
    public List<User> getAllUsers() {
        List<User> users = userDAO.findAll();
        logger.debug("Retrieved {} users", users.size());
        return users;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete
     * @return The number of affected rows
     */
    public int deleteUser(int userId) {
        int result = userDAO.delete(userId);
        logger.info("Deleted user with ID: {}", userId);
        return result;
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return The authenticated User object if credentials are valid, null otherwise
     */
    public User authenticateUser(String username, String password) {
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            logger.info("Successful authentication for user: {}", username);
        } else {
            logger.warn("Failed authentication attempt for username: {}", username);
        }
        return user;
    }

    /**
     * Validates a user object for creation.
     *
     * @param user The user to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUserForCreation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validateName(user.getFirstName(), "First name");
        validateName(user.getLastName(), "Last name");
        
        // In a real system, you would validate the password here
        // For now, we'll just ensure the user has a password hash
    }

    /**
     * Validates a user object for update.
     *
     * @param user The user to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUserForUpdate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validateName(user.getFirstName(), "First name");
        validateName(user.getLastName(), "Last name");
    }

    /**
     * Validates a username according to business rules.
     *
     * @param username The username to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUsername(String username) {
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username: must be 3-20 alphanumeric characters or underscores");
        }
    }

    /**
     * Validates an email address according to business rules.
     *
     * @param email The email to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateEmail(String email) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    /**
     * Validates a name field according to business rules.
     *
     * @param name The name to validate
     * @param fieldName The name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    private void validateName(String name, String fieldName) {
        if (!ValidationUtil.isValidName(name)) {
            throw new IllegalArgumentException(fieldName + " is invalid: " + name);
        }
    }
}