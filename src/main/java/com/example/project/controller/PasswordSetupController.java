package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PasswordSetupController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private User currentUser;
    private final SqliteUserDAO userDAO;
    private Stage passwordSetupStage;
    private Scene userDisplayScene;
    private UserDisplayController userDisplayController;

    public PasswordSetupController() {
        userDAO = new SqliteUserDAO();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setStage(Stage stage) {
        this.passwordSetupStage = stage;
    }

    public void setScene(Scene userDisplayScene, UserDisplayController userDisplayController) {
        this.userDisplayScene = userDisplayScene;
        this.userDisplayController = userDisplayController;
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(this::handleSaveButton);
        cancelButton.setOnAction(this::handleCancelButton);

        // Add Enter key handler to input field
        currentPasswordField.setOnKeyPressed(this::handleKeyPressed);
        newPasswordField.setOnKeyPressed(this::handleKeyPressed);
        confirmPasswordField.setOnKeyPressed(this::handleKeyPressed);

        // Add visual feedback for interactivity
        saveButton.setStyle("-fx-cursor: hand;");
        cancelButton.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSaveButton(new ActionEvent(saveButton, null));
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (currentUser == null) {
            ErrorAlert.show("Error", "No user data available to update password.");
            return;
        }

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs
        if (!validatePasswordInput(currentPassword, newPassword, confirmPassword)) {
            return;
        }

        // Verify current password
        try {
            User dbUser = userDAO.fetchUserByEmail(currentUser.getEmail());
            if (dbUser == null || !dbUser.getPassword().equals(currentPassword)) {
                ErrorAlert.show("Validation Error", "Current password is incorrect.");
                return;
            }

            // Update password
            currentUser.setPassword(newPassword);
            if (saveToDB(currentUser)) {
                ErrorAlert.show("Success", "Password updated successfully.");
                // Switch back to display scene
                if (userDisplayController != null) {
                    userDisplayController.updateUser(currentUser);
                }
                handleCancelButton(event);
            }
        } catch (Exception e) {
            ErrorAlert.show("Database Error", "Failed to update password: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
//        if (userDisplayController != null) {
//            passwordSetupStage.setScene(userDisplayScene);
//        }
        passwordSetupStage.close();
    }

    private boolean saveToDB(User user) {
        try {
            User existingUser = userDAO.getUser(user.getEmail());
            if (existingUser != null) {
                userDAO.updateUser(user);
            } else {
                ErrorAlert.show("Database Error", "User not found in database.");
                return false;
            }
            return true;
        } catch (Exception e) {
            ErrorAlert.show("Database Error", "Failed to save user: " + e.getMessage());
            return false;
        }
    }

    private boolean validatePasswordInput(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            ErrorAlert.show("Validation Error", "Current password cannot be empty.");
            return false;
        }
        if (currentPassword.equals(newPassword)) {
            ErrorAlert.show("Validation Error", "The new password should not be the same as the current password.");
            return false;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            ErrorAlert.show("Validation Error", "New password cannot be empty.");
            return false;
        }
        if (newPassword.length() < 6) {
            ErrorAlert.show("Validation Error", "New password must be at least 6 characters long.");
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            ErrorAlert.show("Validation Error", "New password and confirmation do not match.");
            return false;
        }
        return true;
    }
}