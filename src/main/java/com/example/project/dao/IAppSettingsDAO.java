package com.example.project.dao;

import java.sql.SQLException;

public interface IAppSettingsDAO {

    /**
     * Saves or updates the app settings for a user.
     * If settings already exist, they will be updated.
     * If not, new settings will be inserted.
     *
     * @param email The email of the user whose settings are being saved.
     * @param theme The theme preference (e.g., Light or Dark).
     * @param runOnStartup Whether the app should run on system startup.
     * @throws SQLException If there is an issue with the database operation.
     */
    void saveAppSettings(String email, String theme, boolean runOnStartup) throws SQLException;

    /**
     * Retrieves the app settings of a user, including theme and run-on-startup settings.
     *
     * @param email The email of the user whose settings are being fetched.
     * @return An array containing the user's theme and run-on-startup settings, or null if no settings found.
     * @throws SQLException If there is an issue with the database operation.
     */
    String[] getAppSettings(String email) throws SQLException;
}