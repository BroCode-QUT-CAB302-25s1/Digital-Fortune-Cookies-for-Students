package com.example.project.dao;

import com.example.project.database.SqliteConnection;
import com.example.project.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteUserDAO implements IUserDAO {
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String ADD_USER = "INSERT INTO users (username, preferred_name, first_name, last_name, email, github, phone, location, job, gender, dob, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, preferred_name = ?, first_name = ?, last_name = ?, github = ?, phone = ?, location = ?, job = ?, gender = ?, dob = ?, password = ? WHERE email = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE email = ?";

    private Connection connection;

    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
    }

    @Override
    public void addUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPreferredName());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getGithub());
            statement.setString(7, user.getPhone());
            statement.setString(8, user.getLocation());
            statement.setString(9, user.getJob());
            statement.setString(10, user.getGender());
            statement.setString(11, user.getDob());
            statement.setString(12, user.getPassword());
            statement.executeUpdate();

            // Retrieve the generated id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            System.err.println("Failed to add user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPreferredName());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getGithub());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getLocation());
            statement.setString(8, user.getJob());
            statement.setString(9, user.getGender());
            statement.setString(10, user.getDob());
            statement.setString(11, user.getPassword());
            statement.setString(12, user.getEmail());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.err.println("No user found with email: " + user.getEmail());
            }
        } catch (SQLException ex) {
            System.err.println("Failed to update user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER)) {
            statement.setString(1, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Failed to delete user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public User getUser(String email) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return new User(
                        results.getString("username"),
                        results.getString("preferred_name"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email"),
                        results.getString("github"),
                        results.getString("phone"),
                        results.getString("location"),
                        results.getString("job"),
                        results.getString("gender"),
                        results.getString("dob"),
                        results.getString("password")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Failed to retrieve user: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(GET_ALL_USERS)) {
            while (results.next()) {
                users.add(new User(
                        results.getString("username"),
                        results.getString("preferred_name"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email"),
                        results.getString("github"),
                        results.getString("phone"),
                        results.getString("location"),
                        results.getString("job"),
                        results.getString("gender"),
                        results.getString("dob"),
                        results.getString("password")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Failed to retrieve all users: " + ex.getMessage());
            ex.printStackTrace();
        }
        return users;
    }
}