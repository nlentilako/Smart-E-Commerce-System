#!/bin/bash

# Build and Run Script for Smart E-Commerce System
# This script provides easy commands to build, run, and manage the application

set -e  # Exit immediately if a command exits with a non-zero status

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}=======================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}=======================================${NC}"
}

# Function to check if docker is available
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed or not in PATH"
        exit 1
    fi
}

# Function to check if maven is available
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        exit 1
    fi
}

# Function to build the application
build_app() {
    print_header "Building Smart E-Commerce System"
    print_status "Checking Maven installation..."
    check_maven
    
    print_status "Cleaning previous build..."
    mvn clean
    
    print_status "Compiling and packaging application..."
    mvn package -DskipTests
    
    print_status "Build completed successfully!"
}

# Function to run with Docker
run_docker() {
    print_header "Starting Smart E-Commerce System with Docker"
    print_status "Checking Docker installation..."
    check_docker
    
    print_status "Building and starting containers..."
    docker-compose up -d --build
    
    print_status "Waiting for services to start..."
    sleep 10
    
    print_status "Checking container status..."
    docker-compose ps
    
    print_status "Application is now running!"
    echo ""
    echo -e "${GREEN}Services:${NC}"
    echo "  - MySQL Database: localhost:3306"
    echo "  - PostgreSQL Database: localhost:5432 (optional)"
    echo "  - Application: Check logs for JavaFX UI availability"
    echo ""
    echo -e "${YELLOW}To view logs: docker-compose logs -f${NC}"
    echo -e "${YELLOW}To stop services: docker-compose down${NC}"
}

# Function to stop Docker services
stop_docker() {
    print_header "Stopping Smart E-Commerce System"
    print_status "Stopping containers..."
    docker-compose down
    
    print_status "Services stopped successfully!"
}

# Function to run tests
run_tests() {
    print_header "Running Tests"
    print_status "Executing unit tests..."
    mvn test
    
    print_status "Tests completed!"
}

# Function to show help
show_help() {
    echo "Smart E-Commerce System - Build and Run Script"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build     - Build the application (compile and package)"
    echo "  run       - Run the application using Docker Compose"
    echo "  stop      - Stop the running Docker services"
    echo "  tests     - Run unit tests"
    echo "  clean     - Clean build artifacts"
    echo "  rebuild   - Clean and rebuild the application"
    echo "  help      - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build     # Build the application"
    echo "  $0 run       # Run with Docker (builds if necessary)"
    echo "  $0 stop      # Stop running services"
    echo ""
}

# Main script logic
case "${1:-help}" in
    build)
        build_app
        ;;
    run)
        run_docker
        ;;
    stop)
        stop_docker
        ;;
    tests)
        run_tests
        ;;
    clean)
        print_header "Cleaning Build Artifacts"
        mvn clean
        print_status "Clean completed!"
        ;;
    rebuild)
        print_header "Rebuilding Application"
        mvn clean package -DskipTests
        print_status "Rebuild completed!"
        ;;
    help|"")
        show_help
        ;;
    *)
        print_error "Unknown command: $1"
        show_help
        exit 1
        ;;
esac