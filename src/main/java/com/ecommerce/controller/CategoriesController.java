package com.ecommerce.controller;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.model.Category;
import com.ecommerce.util.JWTUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Categories controller that handles category-related API requests.
 */
public class CategoriesController extends HttpServlet {
    
    private CategoryDAO categoryDAO;
    private Gson gson;
    
    public CategoriesController() {
        this.categoryDAO = new CategoryDAO();
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
            // Get all categories
            List<Category> categories = categoryDAO.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(categories));
            out.flush();
        } else {
            // Extract category ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int categoryId = Integer.parseInt(pathParts[1]);
                    Category category = categoryDAO.findById(categoryId);
                    if (category != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.setContentType("application/json");
                        PrintWriter out = resp.getWriter();
                        out.print(gson.toJson(category));
                        out.flush();
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\": \"Category not found\"}");
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid category ID\"}");
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
        
        // Only allow creating categories at root path
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Category creation not fully implemented in this example\"}");
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
            // Extract category ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int categoryId = Integer.parseInt(pathParts[1]);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Category update not fully implemented in this example\"}");
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid category ID\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Category ID required\"}");
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
            // Extract category ID from path
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    int categoryId = Integer.parseInt(pathParts[1]);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Category deletion not fully implemented in this example\"}");
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Invalid category ID\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Category ID required\"}");
        }
    }
}