# Smart E-Commerce System - Docker Configuration

## Overview

This document provides a complete guide to the Docker configuration for the Smart E-Commerce System. The configuration includes:

- Multi-stage Dockerfile for optimized image building
- Docker Compose setup with MySQL and PostgreSQL databases
- Configuration management
- Database initialization scripts
- Build and run scripts

## Files Included

### 1. Dockerfile
- Multi-stage build process (builder and runtime stages)
- Optimized for JavaFX applications with necessary GUI libraries
- Uses Maven for building in the first stage
- Creates a lightweight runtime image with only the required JAR

### 2. docker-compose.yml
- Defines services: MySQL, PostgreSQL (optional), and the application
- Includes environment variables for database configuration
- Sets up networking and volumes
- Configures health checks and dependencies

### 3. Configuration Files
- `config/app.properties`: Database connection properties with environment variable support
- `init.sql`: Database initialization script with full schema and sample data

### 4. Supporting Scripts
- `build-and-run.sh`: Comprehensive script for building and running the application
- `Docker-README.md`: Detailed Docker configuration guide

## Setup Instructions

### Prerequisites

1. Docker Engine (version 20.10 or higher)
2. Docker Compose (version 2.0 or higher)
3. At least 4GB of RAM available
4. Ports 8080, 3306, and 5432 available on your host machine

### Quick Start

#### Option 1: Using the Build Script (Recommended)
```bash
# Make sure the script is executable
chmod +x build-and-run.sh

# Build the application
./build-and-run.sh build

# Run with Docker
./build-and-run.sh run
```

#### Option 2: Direct Docker Commands
```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Configuration

The application supports environment variable configuration:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `DB_TYPE` | `mysql` | Database type (`mysql` or `postgresql`) |
| `DB_HOST` | `localhost` | Database host address |
| `DB_PORT` | `3306` | Database port number |
| `DB_NAME` | `ecommerce_db` | Database name |
| `DB_USER` | `ecommerce_user` | Database username |
| `DB_PASSWORD` | `ecommerce_password` | Database password |

These can be set in the docker-compose.yml file or through environment variables.

## Database Initialization

The MySQL container will automatically run the `init.sql` script on first startup, creating:
- All necessary tables with proper relationships
- Sample categories, products, and users
- Indexes for performance optimization
- Stored procedures and triggers

## Production Considerations

### Security
- Change default passwords in docker-compose.yml
- Use Docker Secrets or external vault solutions for production
- Enable SSL/TLS for database connections

### Performance
- Add resource limits to docker-compose.yml
- Use external volumes for database persistence
- Implement proper logging and monitoring

### Scaling
- The application can be scaled horizontally
- Ensure database can handle multiple connections
- Consider using a load balancer for multiple instances

## Troubleshooting

### Common Issues
1. **Port conflicts**: Check that ports 8080, 3306, and 5432 are available
2. **Memory issues**: Increase Docker's memory allocation
3. **Database connection failures**: Verify that the database service starts before the application

### Useful Commands
```bash
# Check container status
docker-compose ps

# View logs
docker-compose logs -f

# Execute commands in containers
docker-compose exec <service-name> <command>

# Clean up
docker-compose down -v
```

## Development Workflow

For development, you can use volume mounts to enable hot reloading:

1. Create a `docker-compose.dev.yml` with volume mounts
2. Use the override feature: `docker-compose -f docker-compose.yml -f docker-compose.dev.yml up`

## Updating the Application

To update to a new version:
1. Pull the latest code
2. Rebuild the images: `docker-compose build --no-cache`
3. Restart services: `docker-compose up -d`

## Summary

The Docker configuration provides:
- Easy deployment with a single command
- Isolated environment with proper dependencies
- Database initialization with sample data
- Production-ready architecture
- Comprehensive build and management scripts
- Proper security and performance considerations

For detailed information about any specific component, refer to the individual documentation files in the project.