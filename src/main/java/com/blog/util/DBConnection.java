package com.blog.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing database connections.
 *
 * Connection parameters are read from environment variables so that
 * the application can be deployed to Azure App Service without
 * hard-coding credentials.
 *
 * Environment variables:
 *   DB_URL      - full JDBC URL  (overrides individual host/db vars when set)
 *   DB_HOST     - database host  (default: localhost)
 *   DB_PORT     - database port  (default: 3306)
 *   DB_NAME     - database name  (default: blog_db)
 *   DB_USER     - database user  (default: root)
 *   DB_PASSWORD - database password (default: empty)
 *   DB_DRIVER   - JDBC driver class (default: com.mysql.cj.jdbc.Driver)
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    // Read configuration from environment variables with sensible defaults
    private static final String DB_HOST     = getEnv("DB_HOST", "localhost");
    private static final String DB_PORT     = getEnv("DB_PORT", "3306");
    private static final String DB_NAME     = getEnv("DB_NAME", "blog_db");
    private static final String DB_USER     = getEnv("DB_USER", "root");
    private static final String DB_PASSWORD = getEnv("DB_PASSWORD", "");
    private static final String DB_DRIVER   = getEnv("DB_DRIVER", "com.mysql.cj.jdbc.Driver");

    // Allow a full URL override (useful for Azure SQL connection strings)
    private static final String DB_URL_OVERRIDE = System.getenv("DB_URL");

    private static final String JDBC_URL;

    static {
        if (DB_URL_OVERRIDE != null && !DB_URL_OVERRIDE.isEmpty()) {
            JDBC_URL = DB_URL_OVERRIDE;
        } else {
            JDBC_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                    + "?useSSL=true&requireSSL=false&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true";
        }

        // Load the JDBC driver class
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "JDBC driver not found: " + DB_DRIVER, e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Opens and returns a new database connection.
     * The caller is responsible for closing the connection.
     *
     * @return an open {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Reads an environment variable, returning {@code defaultValue} when
     * the variable is not set or is empty.
     */
    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    // Prevent instantiation
    private DBConnection() {}
}
