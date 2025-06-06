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

    private static final String USER_PREFERENCES_TABLE = "CREATE TABLE IF NOT EXISTS user_preferences (" +
            "email VARCHAR NOT NULL," +
            "languages TEXT," +
            "cookies_type TEXT," +
            "FOREIGN KEY (email) REFERENCES users(email)" +
            ")";

    private static final String PREFERENCES_TABLE = "CREATE TABLE IF NOT EXISTS preferences (" +
            "email VARCHAR PRIMARY KEY," +
            "profile_image VARCHAR," +
            "FOREIGN KEY (email) REFERENCES users(email)" +
            ")";

    private static final String APP_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS app_settings (" +
            "email VARCHAR PRIMARY KEY," +
            "theme VARCHAR NOT NULL," +
            "run_on_startup BOOLEAN NOT NULL," +
            "FOREIGN KEY (email) REFERENCES users(email)" +
            ")";

    private static final String SECURITY_QUESTIONS_TABLE = "CREATE TABLE IF NOT EXISTS security_questions (" +
            "email VARCHAR PRIMARY KEY," +
            "security_question TEXT NOT NULL," +
            "security_answer TEXT NOT NULL," +
            "FOREIGN KEY (email) REFERENCES users(email)" +
            ")";

    private static final String INSERT_INITIAL_USER = "INSERT INTO users (" +
            "username, preferred_name, first_name, last_name, email, github, phone, " +
            "location, job, gender, dob, password" +
            ") VALUES (" +
            "'brocodeTest01', 'BroCode', 'BroCode', 'QUT', 'brocode.QUT@gmail.com', " +
            "'@BroCode-QUT', '0000 000 0001', 'Australia', 'Backend Developer', " +
            "'Male', '01/01/2025', '123456'" +
            ")";

    private static final String INSERT_INITIAL_PREFERENCES = "INSERT INTO user_preferences (" +
            "email, languages, cookies_type) VALUES (" +
            "'brocode.QUT@gmail.com', 'English, Japanese, Chinese', 'Mathematical, Love, Motivational'" +
            ")";

    private static final String INSERT_INITIAL_PROFILE_IMAGE = "INSERT INTO preferences (" +
            "email, profile_image) VALUES (" +
            "'brocode.QUT@gmail.com', '/com/example/project/symbol/BroCode.png'" +
            ")";

    private static final String INSERT_INITIAL_APP_SETTINGS = "INSERT INTO app_settings (" +
            "email, theme, run_on_startup) VALUES (" +
            "'brocode.QUT@gmail.com', 'Light', 0" +
            ")";

    private static final String INSERT_INITIAL_SECURITY_QUESTION = "INSERT INTO security_questions (" +
            "email, security_question, security_answer) VALUES (" +
            "'brocode.QUT@gmail.com', 'What is the name of your first pet?', 'Lily'" +
            ")";

    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    private static final String DROP_USER_PREFERENCES_TABLE = "DROP TABLE IF EXISTS user_preferences";
    private static final String DROP_PREFERENCES_TABLE = "DROP TABLE IF EXISTS preferences";
    private static final String DROP_APP_SETTINGS_TABLE = "DROP TABLE IF EXISTS app_settings";
    private static final String DROP_SECURITY_QUESTIONS_TABLE = "DROP TABLE IF EXISTS security_questions";

    public static void initializeDatabase() {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            System.err.println("Failed to establish database connection.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Create the users table
            statement.execute(USERS_TABLE);
            System.out.println("\nUsers table created successfully.");

            // Create the user_preferences table
            statement.execute(USER_PREFERENCES_TABLE);
            System.out.println("User preferences table created successfully.");

            // Create the preferences table
            statement.execute(PREFERENCES_TABLE);
            System.out.println("Preferences table created successfully.");

            // Create the app_settings table
            statement.execute(APP_SETTINGS_TABLE);
            System.out.println("App settings table created successfully.");

            // Create the security_questions table
            statement.execute(SECURITY_QUESTIONS_TABLE);
            System.out.println("Security questions table created successfully.");

            // Insert initial user data
            statement.execute(INSERT_INITIAL_USER);
            System.out.println("\nInitial user data inserted successfully.");

            // Insert initial user preferences data
            statement.execute(INSERT_INITIAL_PREFERENCES);
            System.out.println("Initial user preferences data inserted successfully.");

            // Insert initial profile image data
            statement.execute(INSERT_INITIAL_PROFILE_IMAGE);
            System.out.println("Initial profile image data inserted successfully.");

            // Insert initial app settings data
            statement.execute(INSERT_INITIAL_APP_SETTINGS);
            System.out.println("Initial app settings data inserted successfully.");

            // Insert initial security question data
            statement.execute(INSERT_INITIAL_SECURITY_QUESTION);
            System.out.println("Initial security question data inserted successfully.");
        } catch (SQLException ex) {
            System.err.println("Failed to initialize database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void dropUsersTable() {
        Connection connection = SqliteConnection.getInstance();
        if (connection == null) {
            System.err.println("Failed to establish database connection.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Drop all tables in reverse order due to foreign key constraints
            statement.execute(DROP_SECURITY_QUESTIONS_TABLE);
            System.out.println("Security questions table dropped successfully.");
            statement.execute(DROP_APP_SETTINGS_TABLE);
            System.out.println("App settings table dropped successfully.");
            statement.execute(DROP_PREFERENCES_TABLE);
            System.out.println("Preferences table dropped successfully.");
            statement.execute(DROP_USER_PREFERENCES_TABLE);
            System.out.println("User preferences table dropped successfully.");
            statement.execute(DROP_USERS_TABLE);
            System.out.println("Users table dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Failed to drop tables: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}