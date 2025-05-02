package com.example.project.dao;

import com.example.project.database.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityQuestionDAO implements ISecurityQuestionDAO {
    private static final String INSERT_OR_UPDATE =
            "INSERT OR REPLACE INTO security_questions (email, security_question, security_answer) VALUES (?, ?, ?)";
    private static final String SELECT_BY_EMAIL =
            "SELECT security_question, security_answer FROM security_questions WHERE email = ?";
    private static final String CHECK_USER_EXISTS =
            "SELECT COUNT(*) FROM users WHERE email = ?";

    @Override
    public void saveSecurityQuestion(String email, String question, String answer) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) throw new SQLException("Failed to connect to database.");

        try (PreparedStatement checkUser = connection.prepareStatement(CHECK_USER_EXISTS)) {
            checkUser.setString(1, email);
            ResultSet rs = checkUser.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("User does not exist with email: " + email);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(INSERT_OR_UPDATE)) {
            stmt.setString(1, email);
            stmt.setString(2, question);
            stmt.setString(3, answer);
            stmt.executeUpdate();
            System.out.println("Saved security question for: " + email);
        }
    }

    @Override
    public String[] getSecurityQuestion(String email) throws SQLException {
        Connection connection = ensureConnection();
        if (connection == null) throw new SQLException("Failed to connect to database.");

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("security_question"), rs.getString("security_answer")};
            }
            return null;
        }
    }

    private Connection ensureConnection() throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null || connection.isClosed()) {
            connection = SqliteConnection.reconnect();
            if (connection == null) {
                throw new SQLException("Unable to reconnect to database.");
            }
        }
        return connection;
    }
}
