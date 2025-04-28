package com.example.project.dao;

import com.example.project.database.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileImageDAO implements IProfileImageDAO {
    private static final String INSERT_PROFILE_IMAGE =
            "INSERT OR REPLACE INTO preferences (email, profile_image) VALUES (?, ?)";
    private static final String SELECT_PROFILE_IMAGE =
            "SELECT profile_image FROM preferences WHERE email = ?";

    @Override
    public void saveProfileImage(String email, String profileImage) throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        // Check if preferences exist for the email
        String checkSql = "SELECT COUNT(*) FROM preferences WHERE email = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Update existing preferences
                String updateSql = "UPDATE preferences SET profile_image = ? WHERE email = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, profileImage);
                    updateStmt.setString(2, email);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new preferences
                try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_PROFILE_IMAGE)) {
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, profileImage);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public String getProfileImage(String email) throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            throw new SQLException("Failed to establish database connection.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_PROFILE_IMAGE)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("profile_image");
            }
            return null;
        }
    }
}