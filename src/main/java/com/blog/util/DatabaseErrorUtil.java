package com.blog.util;

import java.sql.SQLException;

/**
 * Converts low-level SQL exceptions into clearer messages for end users.
 */
public final class DatabaseErrorUtil {

    private DatabaseErrorUtil() {
        // Utility class
    }

    public static String toUserMessage(SQLException e) {
        String sqlState = e.getSQLState() == null ? "" : e.getSQLState();
        String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();

        if (sqlState.startsWith("08")
                || message.contains("communications link failure")
                || message.contains("connection refused")
                || message.contains("login failed")
                || message.contains("access denied")
                || message.contains("unknown host")
                || message.contains("timed out")) {
            return "Couldn't connect to the database. Check your Azure database settings and try again.";
        }

        if (message.contains("doesn't exist")
                || message.contains("invalid object name")
                || message.contains("no such table")
                || message.contains("not found")) {
            return "The database is reachable, but the blog table hasn't been set up yet. Run the SQL setup script and try again.";
        }

        if (message.contains("syntax error") || message.contains("incorrect syntax")) {
            return "The database connected, but its SQL dialect doesn't match the app yet.";
        }

        return "A database error occurred while processing your request. Please try again shortly.";
    }
}
