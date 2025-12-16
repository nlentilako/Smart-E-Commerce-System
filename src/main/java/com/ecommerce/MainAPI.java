package com.ecommerce;

import com.ecommerce.controller.AuthController;
import com.ecommerce.controller.CategoriesController;
import com.ecommerce.controller.ProductsController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main API application class that starts the Jetty server and registers all controllers.
 */
public class MainAPI {
    private static final Logger logger = LoggerFactory.getLogger(MainAPI.class);
    
    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default 8080");
            }
        }
        
        Server server = new Server(port);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // Register controllers
        context.addServlet(new ServletHolder(new AuthController()), "/api/auth/*");
        context.addServlet(new ServletHolder(new ProductsController()), "/api/products/*");
        context.addServlet(new ServletHolder(new CategoriesController()), "/api/categories/*");
        
        // Enable CORS
        org.eclipse.jetty.servlet.FilterHolder corsFilter = new org.eclipse.jetty.servlet.FilterHolder(new com.ecommerce.filter.CORSFilter());
        context.addFilter(corsFilter, "/*", null);
        
        try {
            server.start();
            logger.info("Server started on port {}", port);
            logger.info("Available endpoints:");
            logger.info("  POST   /api/auth/login     - User authentication");
            logger.info("  GET    /api/products       - Get all products");
            logger.info("  GET    /api/products/{id}  - Get product by ID");
            logger.info("  POST   /api/products       - Create new product (requires auth)");
            logger.info("  PUT    /api/products/{id}  - Update product (requires auth)");
            logger.info("  DELETE /api/products/{id}  - Delete product (requires auth)");
            logger.info("  GET    /api/categories     - Get all categories");
            logger.info("  GET    /api/categories/{id} - Get category by ID");
            server.join();
        } catch (Exception e) {
            logger.error("Error starting server", e);
        } finally {
            server.destroy();
        }
    }
}