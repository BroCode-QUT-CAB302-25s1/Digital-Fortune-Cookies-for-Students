package com.example.project.dao;

import com.example.project.database.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppSettingsDAO implements IAppSettingsDAO {
    private static final String INSERT_APP_SETTINGS =
            "INSERT OR REPLACE INTO app_settings (email, theme, run_on_startup) VALUES (?, ?, ?)";
    private static final String SELECT_APP_SETTINGS =
            "SELECT theme, run_on_startup FROM app_settings WHERE email = ?";
    private static final String CHECK_APP_SETTINGS_EXISTS =
            "SELECT COUNT(*) FROM app_settings WHERE email = ?";
    private static final String CHECK_USER_EXISTS =
            "SELECT COUNT(*) FROM users WHERE email = ?";

    @Override
    public void saveAppSettings(String email, String theme, boolean runOnStartup) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        // Check if user exists to avoid foreign key constraint violation
        try (PreparedStatement checkUserStmt = connection.prepareStatement(CHECK_USER_EXISTS)) {
            checkUserStmt.setString(1, email);
            ResultSet rs = checkUserStmt.executeQuery();
            rs.next();
            int userCount = rs.getInt(1);
            if (userCount == 0) {
                throw new SQLException("User with email " + email + " does not exist in users table.");
            }
        }

        // Check if settings exist for the email
        try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_APP_SETTINGS_EXISTS)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Update existing settings
                String updateSql = "UPDATE app_settings SET theme = ?, run_on_startup = ? WHERE email = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, theme);
                    updateStmt.setInt(2, runOnStartup ? 1 : 0);
                    updateStmt.setString(3, email);
                    System.out.println("Updating app settings - Email: " + email + ", Theme: " + theme + ", RunOnStartup: " + runOnStartup);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new settings
                try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_APP_SETTINGS)) {
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, theme);
                    insertStmt.setInt(3, runOnStartup ? 1 : 0);
                    System.out.println("Inserting app settings - Email: " + email + ", Theme: " + theme + ", RunOnStartup: " + runOnStartup);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException in saveAppSettings: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String[] getAppSettings(String email) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_APP_SETTINGS)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String[] settings = new String[] {
                        rs.getString("theme"),
                        String.valueOf(rs.getBoolean("run_on_startup"))
                };
                System.out.println("Retrieved app settings - Email: " + email + ", Theme: " + settings[0] + ", RunOnStartup: " + settings[1]);
                return settings;
            }
            System.out.println("No app settings found for email: " + email);
            return null;
        } catch (SQLException e) {
            System.err.println("SQLException in getAppSettings: " + e.getMessage());
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