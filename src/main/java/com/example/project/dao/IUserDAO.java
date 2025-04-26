package com.example.project.dao;

import com.example.project.model.User;

import java.util.List;

public interface IUserDAO {
    /**
     * Adds a new user to the database.
     * @param user The user to add.
     */
    void addUser(User user);

    /**
     * Updates an existing user in the database.
     * @param user The user to update.
     */
    void updateUser(User user);

    /**
     * Deletes a user from the database.
     * @param user The user to delete.
     */
    void deleteUser(User user);

    /**
     * Retrieves a user from the database by email.
     * @param email The email of the user to retrieve.
     * @return The user with the given email, or null if not found.
     */
    User getUser(String email);

    /**
     * Retrieves all users from the database.
     * @return A list of all users in the database.
     */
    List<User> getAllUsers();
}