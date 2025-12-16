# Smart E-Commerce System - Docker Configuration Guide

## Overview

This document provides detailed instructions for configuring and running the Smart E-Commerce System using Docker and Docker Compose.

## Prerequisites

Before running the application with Docker, ensure you have:

- Docker Engine (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)
- At least 4GB of RAM available for containers
- Port 8080, 3306, and 5432 available on your host machine

## Project Structure

```
/workspace/
├── Dockerfile           # Multi-stage Dockerfile for building the application
├── docker-compose.yml   # Docker Compose configuration for services
├── config/              # Configuration directory
│   └── app.properties   # Database configuration file
├── src/                 # Source code directory
├── pom.xml             # Maven project configuration
└── ...
```

## Docker Configuration Steps

### 1. Build and Run with Docker Compose (Recommended)

The easiest way to run the application is using Docker Compose:

```bash
# Navigate to the project directory
cd /workspace

# Build and start all services (detached mode)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### 2. Individual Container Management

If you prefer to manage containers individually:

```bash
# Build the application image
docker build -t smart-ecommerce-system .

# Start MySQL database
docker run -d \
  --name ecommerce-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=ecommerce_db \
  -e MYSQL_USER=ecommerce_user \
  -e MYSQL_PASSWORD=ecommerce_password \
  -p 3306:3306 \
  mysql:8.0

# Start the application
docker run -d \
  --name smart-ecommerce-app \
  --link ecommerce-mysql:mysql-db \
  -e DB_HOST=mysql-db \
  -e DB_PORT=3306 \
  -e DB_NAME=ecommerce_db \
  -e DB_USER=ecommerce_user \
  -e DB_PASSWORD=ecommerce_password \
  -e DB_TYPE=mysql \
  -p 8080:8080 \
  -v $(pwd)/config:/app/config \
  smart-ecommerce-system
```

## Environment Variables

The application supports the following environment variables:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `DB_TYPE` | `mysql` | Database type (`mysql` or `postgresql`) |
| `DB_HOST` | `localhost` | Database host address |
| `DB_PORT` | `3306` | Database port number |
| `DB_NAME` | `ecommerce_db` | Database name |
| `DB_USER` | `ecommerce_user` | Database username |
| `DB_PASSWORD` | `ecommerce_password` | Database password |

## Database Initialization

### MySQL Initial Setup

On first run, the MySQL container will automatically create the database and user. You can initialize the database schema by running the following commands inside the application container:

```bash
# Execute database initialization script
docker exec -it smart-ecommerce-app java -cp app.jar com.ecommerce.util.DatabaseUtil --init-schema
```

### PostgreSQL Alternative

To use PostgreSQL instead of MySQL, modify the docker-compose.yml file:

1. Change the depends_on to use postgres-db
2. Update the environment variables to match PostgreSQL settings
3. Ensure your application code supports PostgreSQL connections

## Configuration Files

### app.properties

The application uses a configuration file located at `config/app.properties`:

```properties
# Database Configuration
database.type=${DB_TYPE:mysql}
database.host=${DB_HOST:localhost}
database.port=${DB_PORT:3306}
database.name=${DB_NAME:ecommerce_db}
database.username=${DB_USER:ecommerce_user}
database.password=${DB_PASSWORD:ecommerce_password}

# Connection Pool Settings
database.pool.minIdle=5
database.pool.maxPoolSize=20
database.pool.connectionTimeout=30000
database.pool.idleTimeout=600000
database.pool.maxLifetime=1800000

# Additional JDBC Properties
database.jdbc.additionalProperties.useSSL=false
database.jdbc.additionalProperties.allowPublicKeyRetrieval=true
database.jdbc.additionalProperties.serverTimezone=UTC
```

## Production Deployment Considerations

### Security

1. **Change default passwords**: Replace all default passwords in docker-compose.yml
2. **Use secrets management**: For production, consider using Docker Secrets or external vault solutions
3. **Network isolation**: The containers run on an isolated network by default

### Performance

1. **Resource limits**: Add resource limits to docker-compose.yml:
   ```yaml
   deploy:
     resources:
       limits:
         memory: 2G
         cpus: '1.0'
       reservations:
         memory: 512M
         cpus: '0.5'
   ```

2. **Persistent storage**: The database volumes ensure data persistence across container restarts

### Monitoring

1. **Health checks**: The Dockerfile includes health checks to monitor application status
2. **Logging**: Docker Compose captures all container logs
3. **Metrics**: Consider adding a monitoring solution like Prometheus

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8080, 3306, and 5432 are available
2. **Insufficient memory**: Increase Docker's memory allocation if experiencing OOM errors
3. **Database connection issues**: Check that the database service starts before the application

### Useful Commands

```bash
# Check running containers
docker ps

# View container logs
docker logs <container_name>

# Execute commands in running container
docker exec -it <container_name> sh

# Check container resource usage
docker stats

# Clean up unused containers/images
docker system prune -a
```

## Development Workflow

For development, you can mount your local source code into the container:

```bash
# Build with development override
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

Create a `docker-compose.dev.yml` file with volume mounts for hot reloading during development.

## Scaling

To scale the application horizontally:

```bash
# Scale application instances
docker-compose up -d --scale ecommerce-app=3
```

Note: When scaling, ensure your database can handle multiple connections and consider using a load balancer.

## Updating

To update to a new version:

1. Pull the latest code
2. Rebuild the images: `docker-compose build --no-cache`
3. Restart services: `docker-compose up -d`

---

## Quick Start Commands Summary

```bash
# Build and start everything
docker-compose up -d --build

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop everything
docker-compose down

# Clean up
docker-compose down -v
```

For any issues or questions, please refer to the main project documentation or contact the development team.