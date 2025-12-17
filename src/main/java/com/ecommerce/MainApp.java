package com.ecommerce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the Smart E-Commerce System.
 * This class initializes the JavaFX application and sets up the main window.
 */
public class MainApp extends Application {
    
    private static String authToken;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Check if user is logged in and load appropriate FXML
        if (authToken != null && !authToken.isEmpty()) {
            // User is logged in, load dashboard
            loadFXML(primaryStage, "/fxml/dashboard.fxml");
        } else {
            // User is not logged in, load login
            loadFXML(primaryStage, "/fxml/login.fxml");
        }
    }
    
    /**
     * Load an FXML file and set it to the stage
     * 
     * @param stage The stage to set the scene to
     * @param fxmlPath The path to the FXML file
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void loadFXML(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Smart E-Commerce System");
        stage.show();
    }

    /**
     * Get the authentication token
     * 
     * @return The authentication token
     */
    public static String getAuthToken() {
        return authToken;
    }
    
    /**
     * Set the authentication token
     * 
     * @param token The authentication token to set
     */
    public static void setAuthToken(String token) {
        authToken = token;
    }

    /**
     * Main method to launch the application
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}