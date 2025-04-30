package com.example.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;
    private static final String DB_URL = "jdbc:sqlite:userData.db";

    private SqliteConnection() {
        try {
            instance = DriverManager.getConnection(DB_URL);
            instance.setAutoCommit(true);
            System.out.println("Database connection established: " + DB_URL);
        } catch (SQLException ex) {
            System.err.println("Failed to initialize database connection: " + ex.getMessage());
            ex.printStackTrace();
            instance = null; // Ensure instance is null on failure
        }
    }

    public static synchronized Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                new SqliteConnection();
                if (instance == null) {
                    throw new SQLException("Failed to establish database connection.");
                }
            }
            return instance;
        } catch (SQLException ex) {
            System.err.println("Error checking connection state: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public static synchronized Connection reconnect() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("Closed existing database connection.");
            }
        } catch (SQLException ex) {
            System.err.println("Error closing existing connection: " + ex.getMessage());
            ex.printStackTrace();
        }
        new SqliteConnection();
        return instance;
    }
}