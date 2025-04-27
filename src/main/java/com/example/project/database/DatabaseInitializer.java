package com.example.project.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String USERS_TABLE = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username VARCHAR NOT NULL," +
            "preferred_name VARCHAR," +
            "first_name VARCHAR," +
            "last_name VARCHAR," +
            "email VARCHAR UNIQUE NOT NULL," +
            "github VARCHAR," +
            "phone VARCHAR," +
            "location VARCHAR," +
            "job VARCHAR," +
            "gender VARCHAR," +
            "dob VARCHAR," +
            "password VARCHAR NOT NULL" +
            ")";

    private static final String USER_PREFERENCES_TABLE = "CREATE TABLE user_preferences (" +
            "email VARCHAR NOT NULL," +
            "languages TEXT," +
            "cookies_type TEXT," +
            "FOREIGN KEY (email) REFERENCES users(email)" +
            ")";

    private static final String INSERT_INITIAL_USER = "INSERT INTO users (username, preferred_name, first_name, last_name, email, github, phone, location, job, gender, dob, password) VALUES " +
            "('brocodeTest01', 'BroCode', 'BroCode', 'QUT', 'brocode.QUT@gmail.com', '@BroCode-QUT', '0000 000 0001', 'Australia', 'Backend Developer', 'Male', '01/01/2025', '12345')";

    private static final String INSERT_INITIAL_PREFERENCES = "INSERT INTO user_preferences (email, languages, cookies_type) VALUES " +
            "('brocode.QUT@gmail.com', 'Java,Python,SQL', 'Session,Persistent')";

    private static final String CHECK_USER_EXISTS = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String CHECK_PREFERENCES_EXISTS = "SELECT COUNT(*) FROM user_preferences WHERE email = ?";

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

            // Create the user_preferences table
            statement.execute(USER_PREFERENCES_TABLE);
            System.out.println("User preferences table created successfully.");

            // Check if initial user already exists
            try (PreparedStatement checkUserStmt = connection.prepareStatement(CHECK_USER_EXISTS)) {
                checkUserStmt.setString(1, "brocodeTest01");
                ResultSet rs = checkUserStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    // Insert initial user data
                    statement.execute(INSERT_INITIAL_USER);
                    System.out.println("Initial user data inserted successfully.");
                } else {
                    System.out.println("Initial user already exists. Skipping user insertion.");
                }
            }

            // Check if initial preferences already exist
            try (PreparedStatement checkPrefStmt = connection.prepareStatement(CHECK_PREFERENCES_EXISTS)) {
                checkPrefStmt.setString(1, "brocode.QUT@gmail.com");
                ResultSet rs = checkPrefStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    // Insert initial preferences data
                    statement.execute(INSERT_INITIAL_PREFERENCES);
                    System.out.println("Initial preferences data inserted successfully.");
                } else {
                    System.out.println("Initial preferences already exist. Skipping preferences insertion.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Failed to initialize database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    private static final String DROP_USER_PREFERENCES_TABLE = "DROP TABLE IF EXISTS user_preferences";

    public static void dropUsersTable() {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            System.err.println("Failed to establish database connection.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Drop the user_preferences table first due to foreign key
            statement.execute(DROP_USER_PREFERENCES_TABLE);
            System.out.println("User preferences table dropped successfully.");
            // Drop the users table
            statement.execute(DROP_USERS_TABLE);
            System.out.println("Users table dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Failed to drop tables: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}