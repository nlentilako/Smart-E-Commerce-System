-- Smart E-Commerce System Database Schema
-- Normalized to 3NF

-- Drop existing tables if they exist (for development)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS products_categories;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    user_type ENUM('CUSTOMER', 'ADMIN') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Categories table
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    parent_category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);

-- Products table
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    sku VARCHAR(100) UNIQUE NOT NULL,
    weight DECIMAL(8, 2),
    dimensions VARCHAR(50), -- Format: "LxWxH"
    brand VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Products-Categories many-to-many relationship
CREATE TABLE products_categories (
    product_id INT,
    category_id INT,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- Inventory table
CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity_available INT NOT NULL DEFAULT 0 CHECK (quantity_available >= 0),
    reserved_quantity INT NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0),
    reorder_level INT DEFAULT 10,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    shipping_address TEXT NOT NULL,
    billing_address TEXT NOT NULL,
    payment_method VARCHAR(50),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shipped_date TIMESTAMP NULL,
    delivered_date TIMESTAMP NULL,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Order Items table
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Reviews table
CREATE TABLE reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create indexes for performance optimization
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
CREATE INDEX idx_reviews_product_id ON reviews(product_id);
CREATE INDEX idx_inventory_product_id ON inventory(product_id);

-- Insert sample data

-- Sample categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Books', 'Physical and digital books'),
('Clothing', 'Apparel and fashion items'),
('Home & Garden', 'Home improvement and gardening supplies'),
('Sports & Outdoors', 'Sports equipment and outdoor gear');

-- Sample users
INSERT INTO users (username, email, password_hash, first_name, last_name, phone, address, user_type) VALUES
('admin', 'admin@ecommerce.com', '$2a$10$NQzWyYJ4qM6.lFkXwvQoIuTlKtB6VrG9UOYs3R5p6n8z1c2v4w5x6', 'Admin', 'User', '+1234567890', '123 Admin St, City, Country', 'ADMIN'),
('john_doe', 'john@example.com', '$2a$10$NQzWyYJ4qM6.lFkXwvQoIuTlKtB6VrG9UOYs3R5p6n8z1c2v4w5x6', 'John', 'Doe', '+1987654321', '456 Main St, City, Country', 'CUSTOMER'),
('jane_smith', 'jane@example.com', '$2a$10$NQzWyYJ4qM6.lFkXwvQoIuTlKtB6VrG9UOYs3R5p6n8z1c2v4w5x6', 'Jane', 'Smith', '+1555123456', '789 Oak Ave, City, Country', 'CUSTOMER');

-- Sample products
INSERT INTO products (name, description, price, sku, weight, dimensions, brand) VALUES
('Smartphone XYZ', 'Latest smartphone with advanced features', 699.99, 'PHONE-XYZ-001', 0.18, '145x70x8mm', 'TechBrand'),
('Laptop ABC', 'High-performance laptop for professionals', 1299.99, 'LAPTOP-ABC-002', 1.5, '320x220x18mm', 'TechBrand'),
('Programming Book', 'Learn programming fundamentals', 49.99, 'BOOK-PROG-003', 0.5, '20x15x3cm', 'KnowledgePress'),
('Running Shoes', 'Comfortable running shoes for athletes', 89.99, 'SHOE-RUN-004', 0.3, '30x20x12cm', 'SportGear'),
('Coffee Maker', 'Automatic coffee maker with timer', 79.99, 'COFFEE-MKR-005', 2.5, '30x25x40cm', 'HomeEssentials');

-- Link products to categories
INSERT INTO products_categories (product_id, category_id) VALUES
(1, 1), -- Smartphone in Electronics
(2, 1), -- Laptop in Electronics
(3, 2), -- Book in Books
(4, 3), -- Shoes in Clothing
(5, 4); -- Coffee Maker in Home & Garden

-- Add inventory
INSERT INTO inventory (product_id, quantity_available, reserved_quantity, reorder_level) VALUES
(1, 50, 0, 10),
(2, 20, 2, 5),
(3, 100, 0, 20),
(4, 75, 3, 15),
(5, 30, 1, 5);

-- Sample orders
INSERT INTO orders (user_id, order_status, total_amount, shipping_address, billing_address, payment_method) VALUES
(2, 'DELIVERED', 789.98, '456 Main St, City, Country', '456 Main St, City, Country', 'Credit Card'),
(3, 'SHIPPED', 1389.98, '789 Oak Ave, City, Country', '789 Oak Ave, City, Country', 'PayPal'),
(2, 'PENDING', 89.99, '456 Main St, City, Country', '456 Main St, City, Country', 'Debit Card');

-- Sample order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 699.99), -- John bought 1 smartphone
(1, 3, 1, 49.99),  -- John bought 1 book
(2, 2, 1, 1299.99), -- Jane bought 1 laptop
(2, 5, 1, 79.99),   -- Jane bought 1 coffee maker
(3, 4, 1, 89.99);   -- John bought 1 pair of shoes

-- Sample reviews
INSERT INTO reviews (product_id, user_id, rating, title, comment, is_verified_purchase) VALUES
(1, 2, 5, 'Excellent phone!', 'Best smartphone I have ever owned. Great camera and battery life.', TRUE),
(2, 3, 4, 'Good laptop but heavy', 'Powerful machine but could be lighter for portability.', TRUE),
(3, 2, 5, 'Must read for beginners', 'Explained concepts very clearly. Highly recommend for new programmers.', TRUE),
(4, 3, 4, 'Comfortable for running', 'Great support and cushioning. Perfect for daily runs.', TRUE);