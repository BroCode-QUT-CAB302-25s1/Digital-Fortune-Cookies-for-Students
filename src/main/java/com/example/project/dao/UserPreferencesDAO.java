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
    private static final String UPDATE_USER =
            "UPDATE users SET languages = ?, cookies_type = ? WHERE email = ?";

    @Override
    public void savePreferences(String email, String languages, String cookiesType) throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        // Check if preferences exist for the email
        String checkSql = "SELECT COUNT(*) FROM user_preferences WHERE email = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Update existing preferences
                String updateSql = "UPDATE user_preferences SET languages = ?, cookies_type = ? WHERE email = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, languages);
                    updateStmt.setString(2, cookiesType);
                    updateStmt.setString(3, email);
                    int rowsAffected = updateStmt.executeUpdate();
                }
            } else {
                // Insert new preferences
                String insertSql = "INSERT INTO user_preferences (email, languages, cookies_type) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, languages);
                    insertStmt.setString(3, cookiesType);
                    int rowsAffected = insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public String[] getPreferences(String email) throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_PREFERENCES)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[] { rs.getString("languages"), rs.getString("cookies_type") };
            }
            return null;
        }
    }
}
