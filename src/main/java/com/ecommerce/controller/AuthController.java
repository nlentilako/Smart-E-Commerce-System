package com.ecommerce.controller;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.util.JWTUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication controller that handles login requests.
 */
public class AuthController extends HttpServlet {
    
    private UserService userService;
    private Gson gson;
    
    public AuthController() {
        this.userService = new UserService();
        this.gson = new Gson();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if ("/login".equals(pathInfo)) {
            handleLogin(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
        }
    }
    
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        // Parse the JSON request
        Map<String, String> loginRequest = gson.fromJson(requestBody.toString(), Map.class);
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        // Validate inputs
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username and password are required");
            out.print(gson.toJson(errorResponse));
            out.flush();
            return;
        }
        
        // Authenticate user
        User authenticatedUser = userService.authenticate(username, password);
        
        if (authenticatedUser != null) {
            // Generate JWT token
            String token = JWTUtil.generateToken(authenticatedUser.getUsername());
            
            // Return success response with token
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", authenticatedUser.getUserId(),
                "username", authenticatedUser.getUsername(),
                "email", authenticatedUser.getEmail(),
                "firstName", authenticatedUser.getFirstName(),
                "lastName", authenticatedUser.getLastName(),
                "userType", authenticatedUser.getUserType()
            ));
            out.print(gson.toJson(response));
            out.flush();
        } else {
            // Invalid credentials
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            out.print(gson.toJson(errorResponse));
            out.flush();
        }
    }
}