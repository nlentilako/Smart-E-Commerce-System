# Smart E-Commerce System - Database Design Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Database Schema Overview](#database-schema-overview)
3. [Entity Relationship Diagram](#entity-relationship-diagram)
4. [Detailed Table Descriptions](#detailed-table-descriptions)
5. [Normalization Analysis](#normalization-analysis)
6. [Indexing Strategy](#indexing-strategy)
7. [Performance Considerations](#performance-considerations)

## Introduction

This document provides a comprehensive overview of the Smart E-Commerce System database design. The schema has been normalized to Third Normal Form (3NF) and includes appropriate indexes for performance optimization.

### Design Principles
- **Normalization**: Schema is normalized to 3NF to eliminate redundancy
- **Data Integrity**: Foreign key constraints ensure referential integrity
- **Performance**: Strategic indexing on frequently queried columns
- **Scalability**: Designed to handle growth in users, products, and orders

## Database Schema Overview

The database consists of 7 core tables:

| Table Name | Description |
|------------|-------------|
| `users` | Customer and admin user information |
| `categories` | Product categories (hierarchical) |
| `products` | Product catalog information |
| `products_categories` | Many-to-many relationship between products and categories |
| `inventory` | Stock level tracking for products |
| `orders` | Order header information |
| `order_items` | Order line items |
| `reviews` | Customer product reviews |

## Entity Relationship Diagram

```
┌─────────────────┐       ┌─────────────────────┐       ┌─────────────────┐
│     users       │       │    products         │       │   categories    │
│─────────────────│       │─────────────────────│       │─────────────────│
│ user_id (PK)    │       │ product_id (PK)     │       │ category_id (PK)│
│ username        │       │ name                │       │ name            │
│ email           │       │ description         │       │ description     │
│ password_hash   │       │ price               │       │ parent_category_│
│ first_name      │       │ sku                 │       │ id (FK)         │
│ last_name       │       │ weight              │       │ created_at      │
│ phone           │       │ dimensions          │       │ updated_at      │
│ address         │       │ brand               │       └─────────────────┘
│ user_type       │       │ created_at          │                │
│ created_at      │       │ updated_at          │                │
│ updated_at      │       │ is_active           │                │
│ is_active       │       └─────────────────────┘                │
└─────────────────┘                │                              │
         │                         │                              │
         │                         │                              │
         │                         │                              │
         │                         │                              │
         │                         ▼                              │
         │         ┌──────────────────────────────────────────────┤
         │         │         products_categories                │
         │         │──────────────────────────────────────────────┤
         │         │ product_id (FK, PK)                        │
         │         │ category_id (FK, PK)                       │
         │         └──────────────────────────────────────────────┘
         │
         │
         ▼
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│    orders       │       │  order_items    │       │   inventory     │
│─────────────────│       │─────────────────│       │─────────────────│
│ order_id (PK)   │──┐    │ order_item_id   │       │ inventory_id    │
│ user_id (FK)    │  │    │ (PK)            │       │ (PK)            │
│ order_status    │  │    │ order_id (FK)   │◄──────│ product_id (FK) │
│ total_amount    │  │    │ product_id (FK) │       │ quantity_avail  │
│ shipping_addr   │  │    │ quantity        │       │ reserved_quant  │
│ billing_addr    │  │    │ unit_price      │       │ reorder_level   │
│ payment_method  │  │    │ total_price     │       │ last_updated    │
│ order_date      │  │    └─────────────────┘       └─────────────────┘
│ shipped_date    │  │
│ delivered_date  │  │
│ notes           │  │
└─────────────────┘  │
                     │
                     ▼
            ┌─────────────────┐
            │    reviews      │
            │─────────────────│
            │ review_id (PK)  │
            │ product_id (FK) │
            │ user_id (FK)    │
            │ rating          │
            │ title           │
            │ comment         │
            │ created_at      │
            │ is_verified_pur │
            └─────────────────┘
```

## Detailed Table Descriptions

### 1. users table
Stores customer and admin user information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each user |
| username | VARCHAR(50) | UNIQUE, NOT NULL | User login name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | User email address |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |
| first_name | VARCHAR(50) | NOT NULL | User's first name |
| last_name | VARCHAR(50) | NOT NULL | User's last name |
| phone | VARCHAR(20) | | User's phone number |
| address | TEXT | | User's address |
| user_type | ENUM | DEFAULT 'CUSTOMER' | Type of user (CUSTOMER, ADMIN) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When the user was created |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | When the user was last updated |
| is_active | BOOLEAN | DEFAULT TRUE | Whether the account is active |

### 2. categories table
Stores product categories in a hierarchical structure.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| category_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each category |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Category name |
| description | TEXT | | Category description |
| parent_category_id | INT | FOREIGN KEY | Reference to parent category (for hierarchy) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When the category was created |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | When the category was last updated |

### 3. products table
Stores product catalog information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| product_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each product |
| name | VARCHAR(255) | NOT NULL | Product name |
| description | TEXT | | Product description |
| price | DECIMAL(10,2) | NOT NULL, CHECK (price >= 0) | Product price |
| sku | VARCHAR(100) | UNIQUE, NOT NULL | Stock Keeping Unit identifier |
| weight | DECIMAL(8,2) | | Product weight |
| dimensions | VARCHAR(50) | | Product dimensions (LxWxH) |
| brand | VARCHAR(100) | | Product brand |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When the product was created |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | When the product was last updated |
| is_active | BOOLEAN | DEFAULT TRUE | Whether the product is available |

### 4. products_categories table
Junction table for the many-to-many relationship between products and categories.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| product_id | INT | FOREIGN KEY, PRIMARY KEY | Reference to product |
| category_id | INT | FOREIGN KEY, PRIMARY KEY | Reference to category |

### 5. inventory table
Tracks stock levels for products.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| inventory_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for inventory record |
| product_id | INT | FOREIGN KEY, NOT NULL | Reference to product |
| quantity_available | INT | NOT NULL, DEFAULT 0, CHECK (quantity >= 0) | Available quantity |
| reserved_quantity | INT | NOT NULL, DEFAULT 0, CHECK (quantity >= 0) | Quantity reserved for orders |
| reorder_level | INT | DEFAULT 10 | Quantity threshold for reorder alerts |
| last_updated | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | When inventory was last updated |

### 6. orders table
Stores order header information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each order |
| user_id | INT | FOREIGN KEY, NOT NULL | Reference to user who placed order |
| order_status | ENUM | DEFAULT 'PENDING' | Current status of the order |
| total_amount | DECIMAL(10,2) | NOT NULL, CHECK (amount >= 0) | Total order amount |
| shipping_address | TEXT | NOT NULL | Shipping address |
| billing_address | TEXT | NOT NULL | Billing address |
| payment_method | VARCHAR(50) | | Payment method used |
| order_date | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When the order was placed |
| shipped_date | TIMESTAMP | | When the order was shipped |
| delivered_date | TIMESTAMP | | When the order was delivered |
| notes | TEXT | | Additional order notes |

### 7. order_items table
Stores individual items within orders.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_item_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each order item |
| order_id | INT | FOREIGN KEY, NOT NULL | Reference to order |
| product_id | INT | FOREIGN KEY, NOT NULL | Reference to product |
| quantity | INT | NOT NULL, CHECK (quantity > 0) | Quantity ordered |
| unit_price | DECIMAL(10,2) | NOT NULL, CHECK (price >= 0) | Price per unit at time of order |
| total_price | DECIMAL(10,2) | GENERATED ALWAYS AS (quantity * unit_price) | Calculated total for item |

### 8. reviews table
Stores customer reviews for products.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| review_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each review |
| product_id | INT | FOREIGN KEY, NOT NULL | Reference to reviewed product |
| user_id | INT | FOREIGN KEY, NOT NULL | Reference to reviewing user |
| rating | INT | NOT NULL, CHECK (rating BETWEEN 1 AND 5) | Rating (1-5 stars) |
| title | VARCHAR(255) | | Review title |
| comment | TEXT | | Review comment |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When the review was created |
| is_verified_purchase | BOOLEAN | DEFAULT FALSE | Whether reviewer purchased the product |

## Normalization Analysis

### First Normal Form (1NF)
All tables have atomic values in each column, with no repeating groups or arrays.

### Second Normal Form (2NF)
All non-key attributes are fully functionally dependent on the entire primary key. This is achieved by:
- Eliminating partial dependencies in composite keys
- Using junction tables for many-to-many relationships (products_categories)

### Third Normal Form (3NF)
All attributes are functionally dependent only on the primary key, with no transitive dependencies. This is achieved by:
- Separating inventory from products (inventory depends on product_id)
- Separating order items from orders (order_items depend on both order_id and product_id)
- Using foreign keys to maintain relationships without duplicating data

## Indexing Strategy

The following indexes have been created for performance optimization:

### Primary Indexes
- `PRIMARY KEY` on all ID columns (auto-created)

### Secondary Indexes
- `idx_products_name`: ON products(name) - For product searches
- `idx_products_sku`: ON products(sku) - For unique product lookups
- `idx_products_price`: ON products(price) - For price range queries
- `idx_orders_user_id`: ON orders(user_id) - For user order history
- `idx_orders_status`: ON orders(order_status) - For status-based queries
- `idx_orders_date`: ON orders(order_date) - For date range queries
- `idx_order_items_order_id`: ON order_items(order_id) - For order detail retrieval
- `idx_order_items_product_id`: ON order_items(product_id) - For product sales analysis
- `idx_reviews_product_id`: ON reviews(product_id) - For product review retrieval
- `idx_inventory_product_id`: ON inventory(product_id) - For inventory lookups

## Performance Considerations

### Query Optimization
- Use parameterized queries to prevent SQL injection
- Implement connection pooling with HikariCP
- Use appropriate indexes for common query patterns
- Consider read replicas for reporting queries

### Scalability
- Partition large tables by date (orders, order_items)
- Implement caching for frequently accessed data
- Use asynchronous processing for non-critical operations
- Consider NoSQL for unstructured data (reviews, logs)

### Maintenance
- Regular database backups
- Index maintenance and optimization
- Query performance monitoring
- Data archiving for old records