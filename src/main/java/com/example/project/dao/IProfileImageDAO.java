package com.example.project.dao;

import java.sql.SQLException;

public interface IProfileImageDAO {

    /**
     * Saves or updates the profile image for a user.
     * If preferences already exist, they will be updated.
     * If not, new preferences will be inserted.
     *
     * @param email The email of the user whose profile image is being saved.
     * @param profileImage The URL or path to the user's profile image.
     * @throws SQLException If there is an issue with the database operation.
     */
    void saveProfileImage(String email, String profileImage) throws SQLException;

    /**
     * Retrieves the profile image of a user.
     *
     * @param email The email of the user whose profile image is being fetched.
     * @return The profile image URL or path, or null if no preferences found.
     * @throws SQLException If there is an issue with the database operation.
     */
    String getProfileImage(String email) throws SQLException;
}