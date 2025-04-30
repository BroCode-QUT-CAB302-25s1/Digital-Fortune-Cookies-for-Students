package com.example.project.dao;

import com.example.project.database.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferencesDAO implements IUserPreferencesDAO {
    private static final String INSERT_PREFERENCES =
            "INSERT OR REPLACE INTO user_preferences (email, languages, cookies_type) VALUES (?, ?, ?)";
    private static final String SELECT_PREFERENCES =
            "SELECT languages, cookies_type FROM user_preferences WHERE email = ?";
    private static final String UPDATE_PREFERENCES =
            "UPDATE user_preferences SET languages = ?, cookies_type = ? WHERE email = ?";
    private static final String CHECK_PREFERENCES_EXISTS =
            "SELECT COUNT(*) FROM user_preferences WHERE email = ?";
    private static final String CHECK_USER_EXISTS =
            "SELECT COUNT(*) FROM users WHERE email = ?";

    @Override
    public void savePreferences(String email, String languages, String cookiesType) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        // Check if user exists to avoid foreign key constraint violation
        try (PreparedStatement checkUserStmt = connection.prepareStatement(CHECK_USER_EXISTS)) {
            checkUserStmt.setString(1, email);
            try (ResultSet rs = checkUserStmt.executeQuery()) {
                rs.next();
                int userCount = rs.getInt(1);
                if (userCount == 0) {
                    throw new SQLException("User with email " + email + " does not exist in users table.");
                }
            }
        }

        // Check if preferences exist for the email
        try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_PREFERENCES_EXISTS)) {
            checkStmt.setString(1, email);
            try (ResultSet rs = checkStmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                if (count > 0) {
                    // Update existing preferences
                    try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_PREFERENCES)) {
                        updateStmt.setString(1, languages);
                        updateStmt.setString(2, cookiesType);
                        updateStmt.setString(3, email);
                        System.out.println("Updating preferences - Email: " + email + ", Languages: " + languages + ", CookiesType: " + cookiesType);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insert new preferences
                    try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_PREFERENCES)) {
                        insertStmt.setString(1, email);
                        insertStmt.setString(2, languages);
                        insertStmt.setString(3, cookiesType);
                        System.out.println("Inserting preferences - Email: " + email + ", Languages: " + languages + ", CookiesType: " + cookiesType);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException in savePreferences: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String[] getPreferences(String email) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_PREFERENCES)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String[] preferences = new String[] {
                            rs.getString("languages"),
                            rs.getString("cookies_type")
                    };
//                    System.out.println("Retrieved preferences - Email: " + email + ", Languages: " + preferences[0] + ", CookiesType: " + preferences[1]);
                    return preferences;
                }
                System.out.println("No preferences found for email: " + email);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("SQLException in getPreferences: " + e.getMessage());
            throw e;
        }
    }

    private Connection ensureConnection() throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null || connection.isClosed()) {
            System.err.println("Database connection is null or closed. Attempting to reconnect...");
            connection = SqliteConnection.reconnect();
            if (connection == null) {
                throw new SQLException("Failed to establish database connection after reconnect attempt.");
            }
        }
        return connection;
    }
}