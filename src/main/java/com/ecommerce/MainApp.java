package com.ecommerce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Smart E-Commerce System.
 * This class initializes the JavaFX application and sets up the main window.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        
        // Set up the scene and stage
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Smart E-Commerce System");
        primaryStage.setScene(scene);
        primaryStage.show();
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