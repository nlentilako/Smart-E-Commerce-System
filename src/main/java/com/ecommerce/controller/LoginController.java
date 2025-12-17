package com.ecommerce.controller;

import com.ecommerce.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Base64;

public class LoginController {

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label emailErrorLabel;
    
    @FXML
    private Label passwordErrorLabel;
    
    @FXML
    private CheckBox rememberMeCheckbox;
    
    @FXML
    private Button loginButton;
    
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        // Simple validation
        boolean isValid = true;
        
        if (email.isEmpty() || !email.contains("@")) {
            emailErrorLabel.setText("Please enter a valid email address");
            emailErrorLabel.setVisible(true);
            isValid = false;
        } else {
            emailErrorLabel.setVisible(false);
        }
        
        if (password.isEmpty() || password.length() < 6) {
            passwordErrorLabel.setText("Password must be at least 6 characters");
            passwordErrorLabel.setVisible(true);
            isValid = false;
        } else {
            passwordErrorLabel.setVisible(false);
        }
        
        if (!isValid) {
            return;
        }
        
        // Disable button and show loading state
        loginButton.setDisable(true);
        loginButton.setText("Signing in...");
        
        // Attempt login
        loginToServer(email, password);
    }
    
    private void loginToServer(String email, String password) {
        try {
            JsonObject loginRequest = new JsonObject();
            loginRequest.addProperty("username", email);
            loginRequest.addProperty("password", password);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(loginRequest)))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonObject responseData = gson.fromJson(response.body(), JsonObject.class);
                String token = responseData.get("token").getAsString();
                
                // Store token in a static variable or session manager
                MainApp.setAuthToken(token);
                
                // Navigate to dashboard
                navigateToDashboard();
            } else {
                JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);
                String errorMessage = errorResponse.has("error") ? 
                    errorResponse.get("error").getAsString() : 
                    "Login failed. Please check your credentials.";
                
                showAlert("Login Error", errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Connection Error", "An error occurred during login. Please try again.");
        } finally {
            // Re-enable button
            loginButton.setDisable(false);
            loginButton.setText("Sign In");
        }
    }
    
    private void navigateToDashboard() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            MainApp.loadFXML(stage, "/fxml/dashboard.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}