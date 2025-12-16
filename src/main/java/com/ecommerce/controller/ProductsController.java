package com.ecommerce.controller;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import com.ecommerce.util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Products controller that handles product-related API requests.
 */
public class ProductsController extends HttpServlet {
    
    private ProductDAO productDAO;
    private Gson gson;
    
    public ProductsController() {
        this.productDAO = new ProductDAO();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // Check for JWT token in Authorization header
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Authorization token required\"}");
            return;
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all products
            List<Product> products = productDAO.findAllActive();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(products));
            out.flush();
        } else {
            // Extract product ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.setContentType("application/json");
                        PrintWriter out = resp.getWriter();
                        out.print(gson.toJson(product));
                        out.flush();
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\": \"Product not found\"}");
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid product ID\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // Check for JWT token in Authorization header
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Authorization token required\"}");
            return;
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }
        
        // Only allow creating products at root path
        if (pathInfo == null || pathInfo.equals("/")) {
            handleCreateProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // Check for JWT token in Authorization header
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Authorization token required\"}");
            return;
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }
        
        if (pathInfo != null) {
            // Extract product ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    handleUpdateProduct(req, resp, productId);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid product ID\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Product ID required\"}");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // Check for JWT token in Authorization header
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Authorization token required\"}");
            return;
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }
        
        if (pathInfo != null) {
            // Extract product ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    handleDeleteProduct(resp, productId);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid product ID\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Product ID required\"}");
        }
    }
    
    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        Type productType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> productData = gson.fromJson(requestBody.toString(), productType);
        
        // Create a new product (simplified for this example)
        // In a real implementation, you'd need to properly map the data to a Product object
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\": \"Product creation not fully implemented in this example\"}");
    }
    
    private void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp, int productId) throws IOException {
        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        Type productType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> productData = gson.fromJson(requestBody.toString(), productType);
        
        // Update the product (simplified for this example)
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\": \"Product update not fully implemented in this example\"}");
    }
    
    private void handleDeleteProduct(HttpServletResponse resp, int productId) throws IOException {
        int result = productDAO.delete(productId);
        if (result > 0) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Product deleted successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Product not found\"}");
        }
    }
}