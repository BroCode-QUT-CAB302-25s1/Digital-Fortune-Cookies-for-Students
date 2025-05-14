package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ForgotPasswordController {

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private void handleResetButton() {
        String newPass = newPassword.getText();
        String confirm = confirmPassword.getText();

        if (newPass.isEmpty() || confirm.isEmpty()) {
            showAlert(AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (!newPass.equals(confirm)) {
            showAlert(AlertType.ERROR, "Passwords do not match.");
            return;
        }

        // Placeholder for reset logic (e.g., update password in DB)
        showAlert(AlertType.INFORMATION, "Your password has been reset.");
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Password Reset");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
