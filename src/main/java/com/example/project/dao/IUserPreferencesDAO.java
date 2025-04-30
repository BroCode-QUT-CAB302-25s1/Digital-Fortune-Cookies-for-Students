package com.example.project.dao;

import java.sql.SQLException;

public interface IUserPreferencesDAO {

    /**
     * Saves or updates the preferences for a user.
     * If preferences already exist, they will be updated.
     * If not, new preferences will be inserted.
     *
     * @param email The email of the user whose preferences are being saved.
     * @param languages The languages the user prefers.
     * @param cookiesType The type of cookies the user prefers.
     * @throws SQLException If there is an issue with the database operation.
     */
    void savePreferences(String email, String languages, String cookiesType) throws SQLException;

    /**
     * Retrieves the preferences of a user, including languages, cookies type, theme, and run-on-startup settings.
     *
     * @param email The email of the user whose preferences are being fetched.
     * @return An array containing the user's preferences, or null if no preferences found.
     * @throws SQLException If there is an issue with the database operation.
     */
    String[] getPreferences(String email) throws SQLException;
}