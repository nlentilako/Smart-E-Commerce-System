package com.ecommerce.controller;

import com.ecommerce.MainApp;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label userDisplayName;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button logoutSidebarButton;
    
    @FXML
    private ListView<String> menuListView;
    
    @FXML
    private Label totalOrdersLabel;
    
    @FXML
    private Label totalRevenueLabel;
    
    @FXML
    private Label totalCustomersLabel;
    
    @FXML
    private Label totalProductsLabel;
    
    @FXML
    private TableView<Order> ordersTable;
    
    @FXML
    private TableColumn<Order, String> orderIdCol;
    
    @FXML
    private TableColumn<Order, String> customerCol;
    
    @FXML
    private TableColumn<Order, String> dateCol;
    
    @FXML
    private TableColumn<Order, String> amountCol;
    
    @FXML
    private TableColumn<Order, String> statusCol;
    
    @FXML
    private TableColumn<Order, String> actionsCol;
    
    @FXML
    private ListView<String> productsList;
    
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    public void initialize() {
        // Set up menu items
        ObservableList<String> menuItems = FXCollections.observableArrayList(
            "Dashboard", "Products", "Categories", "Orders", "Users"
        );
        menuListView.setItems(menuItems);
        
        // Set up table columns
        orderIdCol.setCellValueFactory(param -> {
            int id = param.getValue().getOrderId();
            return new javafx.beans.property.SimpleStringProperty("#ORD-" + String.format("%03d", id));
        });
        customerCol.setCellValueFactory(param -> {
            // In a real implementation, this would come from the user info
            // For now, we'll use a placeholder
            return new javafx.beans.property.SimpleStringProperty("Customer " + param.getValue().getUserId());
        });
        dateCol.setCellValueFactory(param -> {
            LocalDateTime date = param.getValue().getOrderDate();
            return new javafx.beans.property.SimpleStringProperty(date.toLocalDate().toString());
        });
        amountCol.setCellValueFactory(param -> {
            BigDecimal amount = param.getValue().getTotalAmount();
            return new javafx.beans.property.SimpleStringProperty("$" + amount.toString());
        });
        statusCol.setCellValueFactory(param -> {
            OrderStatus status = param.getValue().getOrderStatus();
            return new javafx.beans.property.SimpleStringProperty(status.name());
        });
        actionsCol.setCellValueFactory(param -> {
            return new javafx.beans.property.SimpleStringProperty("View/Edit");
        });
        
        // Load user info
        loadUserInfo();
        
        // Load dashboard data
        loadDashboardData();
    }
    
    private void loadUserInfo() {
        // For now, we'll use a placeholder - in a real implementation, this would come from the token
        userDisplayName.setText("Admin");
    }
    
    private void loadDashboardData() {
        // In a real implementation, these would come from API calls
        // For now, we'll use sample data
        totalProductsLabel.setText("156");
        totalOrdersLabel.setText("42");
        totalRevenueLabel.setText("$12,450");
        totalCustomersLabel.setText("89");
        
        // Load sample orders data
        ObservableList<Order> orders = FXCollections.observableArrayList(
            new Order(1, 101, OrderStatus.DELIVERED, new BigDecimal("299.99"), 
                "123 Main St", "123 Main St", "Credit Card", 
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1), 
                LocalDateTime.now().minusDays(1), "Delivered", List.of()),
            new Order(2, 102, OrderStatus.SHIPPED, new BigDecimal("149.50"), 
                "456 Oak Ave", "456 Oak Ave", "Credit Card", 
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 
                null, "Shipped", List.of()),
            new Order(3, 103, OrderStatus.PROCESSING, new BigDecimal("89.99"), 
                "789 Pine Rd", "789 Pine Rd", "PayPal", 
                LocalDateTime.now().minusDays(2), null, 
                null, "Processing", List.of())
        );
        ordersTable.setItems(orders);
        
        // Load sample products data
        ObservableList<String> products = FXCollections.observableArrayList(
            "Smartphone XYZ - $699.99 [Electronics]",
            "Laptop ABC - $1,299.99 [Electronics]", 
            "Programming Book - $49.99 [Books]",
            "Running Shoes - $89.99 [Clothing]",
            "Coffee Maker - $79.99 [Home & Garden]"
        );
        productsList.setItems(products);
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
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}