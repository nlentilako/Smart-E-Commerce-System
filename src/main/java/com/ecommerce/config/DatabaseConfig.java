package com.ecommerce.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Database configuration class that handles connection pooling and database settings.
 * This class follows the Singleton pattern to ensure a single database connection pool.
 */
public class DatabaseConfig {
    private static DatabaseConfig instance;
    private HikariDataSource dataSource;
    private Properties properties;

    // Database configuration properties
    private String driver;
    private String url;
    private String username;
    private String password;
    private int maximumPoolSize;
    private int minimumIdle;
    private long connectionTimeout;
    private long idleTimeout;
    private long maxLifetime;

    /**
     * Private constructor to enforce Singleton pattern.
     * Loads configuration from application.properties file.
     */
    private DatabaseConfig() {
        loadProperties();
        initializeDataSource();
    }

    /**
     * Gets the singleton instance of DatabaseConfig.
     *
     * @return The singleton instance
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Loads database configuration properties from application.properties file.
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties file");
            }
            properties.load(input);

            // Load database configuration
            driver = properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            url = properties.getProperty("db.url", "jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC");
            username = properties.getProperty("db.username", "root");
            password = properties.getProperty("db.password", "password");
            
            // Load connection pool settings
            maximumPoolSize = Integer.parseInt(properties.getProperty("db.pool.maximumPoolSize", "20"));
            minimumIdle = Integer.parseInt(properties.getProperty("db.pool.minimumIdle", "5"));
            connectionTimeout = Long.parseLong(properties.getProperty("db.pool.connectionTimeout", "30000"));
            idleTimeout = Long.parseLong(properties.getProperty("db.pool.idleTimeout", "600000"));
            maxLifetime = Long.parseLong(properties.getProperty("db.pool.maxLifetime", "1800000"));

        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    /**
     * Initializes the HikariCP connection pool with configured settings.
     */
    private void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        
        // Connection pool settings
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        
        // Additional performance settings
        config.setLeakDetectionThreshold(60000); // 1 minute
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Gets the HikariCP data source for database connections.
     *
     * @return The configured data source
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Closes the data source and releases all connections.
     * This should be called when the application shuts down.
     */
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    // Getters for configuration values
    public String getDriver() { return driver; }
    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getMaximumPoolSize() { return maximumPoolSize; }
    public int getMinimumIdle() { return minimumIdle; }
    public long getConnectionTimeout() { return connectionTimeout; }
    public long getIdleTimeout() { return idleTimeout; }
    public long getMaxLifetime() { return maxLifetime; }
}