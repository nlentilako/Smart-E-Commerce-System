# Multi-stage Dockerfile for Smart E-Commerce System

# Build stage
FROM maven:3.9.4-openjdk-17-slim AS builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM openjdk:17-jdk-slim

# Install JavaFX dependencies
RUN apt-get update && \
    apt-get install -y \
    libgtk-3-0 \
    libxtst6 \
    libxrender1 \
    libxrandr2 \
    libasound2 \
    libatk1.0-0 \
    libcairo-gobject2 \
    libgdk-pixbuf-2.0-0 \
    libpango-1.0-0 \
    libx11-xcb1 \
    libxss1 \
    libgconf-2-4 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxt6 \
    fonts-liberation && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/smart-ecommerce-system-1.0.0.jar ./app.jar

# Create a directory for configuration files
RUN mkdir -p /app/config

# Copy configuration files if they exist
COPY src/main/resources/application.properties /app/config/application.properties || echo "No application.properties found"

# Expose port for potential web interface
EXPOSE 8080

# Health check - just check if the process is running
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD pgrep -f "java.*app.jar" > /dev/null || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]