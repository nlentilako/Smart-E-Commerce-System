package com.ecommerce.controller;

import com.ecommerce.MainApp;
import com.ecommerce.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JavaFXProductsController {

    @FXML
    private Label userDisplayName;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button logoutSidebarButton;
    
    @FXML
    private ListView<String> menuListView;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button addProductButton;
    
    @FXML
    private TableView<Product> productsTable;
    
    @FXML
    private TableColumn<Product, String> productIdCol;
    
    @FXML
    private TableColumn<Product, String> nameCol;
    
    @FXML
    private TableColumn<Product, String> categoryCol;
    
    @FXML
    private TableColumn<Product, String> priceCol;
    
    @FXML
    private TableColumn<Product, String> stockCol;
    
    @FXML
    private TableColumn<Product, String> statusCol;
    
    @FXML
    private TableColumn<Product, String> actionsCol;
    
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    public void initialize() {
        // Set up menu items
        ObservableList<String> menuItems = FXCollections.observableArrayList(
            "Dashboard", "Products", "Categories", "Orders", "Users"
        );
        menuListView.setItems(menuItems);
        
        // Set up table columns
        productIdCol.setCellValueFactory(param -> {
            int id = param.getValue().getProductId();
            return new javafx.beans.property.SimpleStringProperty("#PROD-" + String.format("%03d", id));
        });
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(param -> {
            List<com.ecommerce.model.Category> categories = param.getValue().getCategories();
            String categoryNames = categories.isEmpty() ? "N/A" : 
                String.join(", ", categories.stream().map(c -> c.getName()).toArray(String[]::new));
            return new javafx.beans.property.SimpleStringProperty(categoryNames);
        });
        priceCol.setCellValueFactory(param -> {
            BigDecimal price = param.getValue().getPrice();
            return new javafx.beans.property.SimpleStringProperty("$" + price.toString());
        });
        stockCol.setCellValueFactory(param -> {
            // In a real implementation, stock would be part of the Product model
            // For now, we'll use a placeholder
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        statusCol.setCellValueFactory(param -> {
            boolean isActive = param.getValue().isActive();
            return new javafx.beans.property.SimpleStringProperty(isActive ? "Active" : "Inactive");
        });
        actionsCol.setCellValueFactory(param -> {
            return new javafx.beans.property.SimpleStringProperty("Edit/Delete");
        });
        
        // Load user info
        loadUserInfo();
        
        // Load products data
        loadProductsData();
    }
    
    private void loadUserInfo() {
        // For now, we'll use a placeholder - in a real implementation, this would come from the token
        userDisplayName.setText("Admin");
    }
    
    private void loadProductsData() {
        // In a real implementation, this would fetch from the API
        // For now, we'll use sample data
        ObservableList<Product> products = FXCollections.observableArrayList(
            new Product(1, "Smartphone XYZ", "Latest smartphone with advanced features", new BigDecimal("699.99"), "SP-001", 
                new BigDecimal("0.2"), "15x7x0.8 cm", "TechBrand", LocalDateTime.now(), LocalDateTime.now(), true, List.of()),
            new Product(2, "Laptop ABC", "High-performance laptop for professionals", new BigDecimal("1299.99"), "LP-001", 
                new BigDecimal("2.5"), "35x25x2 cm", "TechBrand", LocalDateTime.now(), LocalDateTime.now(), true, List.of()),
            new Product(3, "Programming Book", "Complete guide to programming", new BigDecimal("49.99"), "BK-001", 
                new BigDecimal("0.8"), "20x15x3 cm", "Publishing Co.", LocalDateTime.now(), LocalDateTime.now(), true, List.of()),
            new Product(4, "Running Shoes", "Comfortable running shoes", new BigDecimal("89.99"), "SH-001", 
                new BigDecimal("0.5"), "30x12x10 cm", "SportBrand", LocalDateTime.now(), LocalDateTime.now(), true, List.of()),
            new Product(5, "Coffee Maker", "Automatic coffee maker", new BigDecimal("79.99"), "CM-001", 
                new BigDecimal("3.0"), "25x20x30 cm", "HomeBrand", LocalDateTime.now(), LocalDateTime.now(), true, List.of())
        );
        productsTable.setItems(products);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Clear authentication token
        MainApp.setAuthToken(null);
        
        // Navigate back to login
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            MainApp.loadFXML(stage, "/fxml/login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate to login: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadProductsData(); // Reload all products
            return;
        }
        
        // In a real implementation, this would filter products based on the search term
        // For now, we'll just show an info message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search");
        alert.setHeaderText(null);
        alert.setContentText("Searching for: " + searchTerm + " (This would filter the products in a real implementation)");
        alert.showAndWait();
    }
    
    @FXML
    private void handleAddProduct(ActionEvent event) {
        // Show add product dialog or navigate to add product page
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Product");
        alert.setHeaderText(null);
        alert.setContentText("Add Product functionality would be implemented here");
        alert.showAndWait();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}