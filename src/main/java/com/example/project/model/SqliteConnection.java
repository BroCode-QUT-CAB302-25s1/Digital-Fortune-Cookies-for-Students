package com.example.project.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection() {
        // Guide: Create userData.db follow this path: C:\\userData.db
        //
        String url = "jdbc:sqlite:userData.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " - " + ex.toString());
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}