package com.ecommerce;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserService functionality.
 * Tests user creation, retrieval, update, and deletion operations.
 */
class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }
    
    @Test
    @DisplayName("Should create a new user successfully")
    void shouldCreateNewUserSuccessfully() {
        // Arrange
        User user = new User(0, "testuser", "test@example.com", "Test", "User", 
                           "+1234567890", "123 Test St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        
        // Act
        int userId = userService.registerUser(user);
        
        // Assert
        assertTrue(userId > 0, "User ID should be positive");
    }
    
    @Test
    @DisplayName("Should find user by ID after creation")
    void shouldFindUserByIdAfterCreation() {
        // Arrange
        User user = new User(0, "finduser", "find@example.com", "Find", "User", 
                           "+1234567890", "123 Find St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        int userId = userService.registerUser(user);
        
        // Act
        User foundUser = userService.getUserById(userId);
        
        // Assert
        assertNotNull(foundUser, "User should be found");
        assertEquals(userId, foundUser.getUserId(), "User IDs should match");
        assertEquals("finduser", foundUser.getUsername(), "Usernames should match");
    }
    
    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        // Arrange
        User user = new User(0, "searchuser", "search@example.com", "Search", "User", 
                           "+1234567890", "123 Search St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        userService.registerUser(user);
        
        // Act
        User foundUser = userService.getUserByUsername("searchuser");
        
        // Assert
        assertNotNull(foundUser, "User should be found");
        assertEquals("searchuser", foundUser.getUsername(), "Usernames should match");
    }
    
    @Test
    @DisplayName("Should validate user creation with valid data")
    void shouldValidateUserCreationWithValidData() {
        // Arrange
        User user = new User(0, "validuser", "valid@example.com", "Valid", "User", 
                           "+1234567890", "123 Valid St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        
        // Act & Assert
        assertDoesNotThrow(() -> userService.registerUser(user), 
                          "Valid user should be registered without exception");
    }
    
    @Test
    @DisplayName("Should throw exception for invalid email")
    void shouldThrowExceptionForInvalidEmail() {
        // Arrange
        User user = new User(0, "invaliduser", "not-an-email", "Invalid", "User", 
                           "+1234567890", "123 Invalid St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user),
                    "Should throw exception for invalid email");
    }
    
    @Test
    @DisplayName("Should throw exception for invalid username")
    void shouldThrowExceptionForInvalidUsername() {
        // Arrange
        User user = new User(0, "ab", "valid@example.com", "Invalid", "User", 
                           "+1234567890", "123 Invalid St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user),
                    "Should throw exception for invalid username (too short)");
    }
    
    @Test
    @DisplayName("Should update user information successfully")
    void shouldUpdateUserInformationSuccessfully() {
        // Arrange
        User user = new User(0, "updateuser", "update@example.com", "Update", "User", 
                           "+1234567890", "123 Update St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        int userId = userService.registerUser(user);
        
        User userToUpdate = userService.getUserById(userId);
        userToUpdate.setFirstName("Updated");
        userToUpdate.setLastName("Name");
        
        // Act
        int result = userService.updateUser(userToUpdate);
        
        // Assert
        assertEquals(1, result, "Update should affect 1 row");
        
        User updatedUser = userService.getUserById(userId);
        assertEquals("Updated", updatedUser.getFirstName(), "First name should be updated");
        assertEquals("Name", updatedUser.getLastName(), "Last name should be updated");
    }
    
    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Arrange
        User user = new User(0, "deleteuser", "delete@example.com", "Delete", "User", 
                           "+1234567890", "123 Delete St", User.UserType.CUSTOMER, 
                           LocalDateTime.now(), LocalDateTime.now(), true);
        int userId = userService.registerUser(user);
        
        // Act
        int result = userService.deleteUser(userId);
        
        // Assert
        assertEquals(1, result, "Delete should affect 1 row");
        
        User deletedUser = userService.getUserById(userId);
        assertNull(deletedUser, "User should not be found after deletion");
    }
}