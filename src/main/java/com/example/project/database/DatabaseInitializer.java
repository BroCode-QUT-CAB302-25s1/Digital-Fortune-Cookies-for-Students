package com.example.project.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username VARCHAR NOT NULL," +
            "preferred_name VARCHAR," +
            "first_name VARCHAR NOT NULL," +
            "last_name VARCHAR NOT NULL," +
            "email VARCHAR UNIQUE NOT NULL," +
            "github VARCHAR," +
            "phone VARCHAR," +
            "location VARCHAR," +
            "job VARCHAR," +
            "gender VARCHAR," +
            "dob VARCHAR," +
            "password VARCHAR NOT NULL" +
            ")";

    private static final String INSERT_INITIAL_USER = "INSERT INTO users (username, preferred_name, first_name, last_name, email, github, phone, location, job, gender, dob, password) VALUES " +
            "('brocodeTest01', 'BroCode', 'BroCode', 'QUT', 'brocode.QUT@gmail.com', '@BroCode-QUT', '0000 000 0001', 'Australia', 'Backend Developer', 'Male', '01/01/2025', '12345')";

    private static final String CHECK_USER_EXISTS = "SELECT COUNT(*) FROM users WHERE username = ?";

    public static void initializeDatabase() {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            System.err.println("Failed to establish database connection.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Create the users table
            statement.execute(USERS_TABLE);
            System.out.println("Users table created successfully.");

            // Check if initial user already exists
            try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_USER_EXISTS)) {
                checkStmt.setString(1, "brocodeTest01");
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    // Insert initial data
                    statement.execute(INSERT_INITIAL_USER);
                    System.out.println("Initial user data inserted successfully.");
                } else {
                    System.out.println("Initial user already exists. Skipping insertion.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Failed to initialize database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";

    public static void dropUsersTable() {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            System.err.println("Failed to establish database connection.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Drop the users table
            statement.execute(DROP_USERS_TABLE);
            System.out.println("Users table dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Failed to drop users table: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}