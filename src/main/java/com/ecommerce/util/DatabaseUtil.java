package com.ecommerce.util;

import com.ecommerce.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for common database operations.
 * Provides methods for executing queries, managing connections, and handling results.
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    /**
     * Executes a query that returns a single result.
     *
     * @param query The SQL query to execute
     * @param params The parameters for the query
     * @param mapper The mapper function to convert ResultSet to object
     * @param <T> The type of object to return
     * @return The result object or null if not found
     */
    public static <T> T queryForObject(String query, Object[] params, ResultSetMapper<T> mapper) {
        try (Connection conn = DatabaseConfig.getInstance().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing query: {}", query, e);
            throw new RuntimeException("Database query failed", e);
        }
        return null;
    }

    /**
     * Executes a query that returns a list of results.
     *
     * @param query The SQL query to execute
     * @param params The parameters for the query
     * @param mapper The mapper function to convert ResultSet to object
     * @param <T> The type of objects in the list
     * @return A list of result objects
     */
    public static <T> List<T> queryForList(String query, Object[] params, ResultSetMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing query: {}", query, e);
            throw new RuntimeException("Database query failed", e);
        }
        return results;
    }

    /**
     * Executes an update statement (INSERT, UPDATE, DELETE).
     *
     * @param query The SQL update query to execute
     * @param params The parameters for the query
     * @return The number of affected rows
     */
    public static int executeUpdate(String query, Object[] params) {
        try (Connection conn = DatabaseConfig.getInstance().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            setParameters(stmt, params);
            int result = stmt.executeUpdate();
            logger.debug("Query executed successfully: {} rows affected", result);
            return result;
        } catch (SQLException e) {
            logger.error("Error executing update: {}", query, e);
            throw new RuntimeException("Database update failed", e);
        }
    }

    /**
     * Executes an insert statement and returns the generated key.
     *
     * @param query The SQL insert query to execute
     * @param params The parameters for the query
     * @return The generated key from the insert operation
     */
    public static int executeInsert(String query, Object[] params) {
        try (Connection conn = DatabaseConfig.getInstance().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            setParameters(stmt, params);
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    logger.debug("Insert executed successfully, generated key: {}", generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Failed to get generated key");
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing insert: {}", query, e);
            throw new RuntimeException("Database insert failed", e);
        }
    }

    /**
     * Sets parameters for a PreparedStatement.
     *
     * @param stmt The PreparedStatement to set parameters for
     * @param params The parameters to set
     * @throws SQLException if there's an error setting parameters
     */
    private static void setParameters(PreparedStatement stmt, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * Interface for mapping ResultSet to objects.
     * This allows for flexible conversion of database results to domain objects.
     *
     * @param <T> The type of object to map to
     */
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        /**
         * Maps a ResultSet row to an object.
         *
         * @param rs The ResultSet to map from
         * @return The mapped object
         * @throws SQLException if there's an error during mapping
         */
        T map(ResultSet rs) throws SQLException;
    }
}