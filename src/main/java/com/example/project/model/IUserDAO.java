package com.example.project.model;

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
     * Retrieves a user from the database by username.
     * @param username The username of the user to retrieve.
     * @return The user with the given username, or null if not found.
     */
    User getUser(String username);

    /**
     * Retrieves all users from the database.
     * @return A list of all users in the database.
     */
    List<User> getAllUsers();
}