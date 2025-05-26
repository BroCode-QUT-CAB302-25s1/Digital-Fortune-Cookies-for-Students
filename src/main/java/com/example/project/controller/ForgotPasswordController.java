package com.example.project.controller;

import com.example.project.dao.SecurityQuestionDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import com.example.project.util.SuccessAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Optional;

public class ForgotPasswordController {

    @FXML
    private VBox container;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField emailField;

    @FXML
    private TextField securityQuestionField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private Stage forgotPasswordStage;
    private Scene signInScene;
    private SignInController signInController;
    private SqliteUserDAO userDAO;
    private SecurityQuestionDAO securityQuestionDAO;
    private boolean isEmailVerified = false; // Track if email has been verified

    public ForgotPasswordController() {
        userDAO = new SqliteUserDAO();
        securityQuestionDAO = new SecurityQuestionDAO();
    }

    // Setter to pass the Stage
    public void setStage(Stage stage) {
        this.forgotPasswordStage = stage;
    }

    // Setter to pass the sign-in Scene and SignInController
    public void setSignInScene(Scene signInScene, SignInController signInController) {
        this.signInScene = signInScene;
        this.signInController = signInController;
    }

    @FXML
    private void initialize() {
        // Initially disable security question and answer fields
        securityQuestionField.setDisable(true);
        securityAnswerField.setDisable(true);

        // Set up event handlers
        confirmButton.setOnAction(this::handleConfirmButton);
        cancelButton.setOnAction(this::handleCancelButton);

        // Add Enter key handler to emailField and securityAnswerField
        emailField.setOnKeyPressed(this::handleKeyPressed);
        securityAnswerField.setOnKeyPressed(this::handleKeyPressed);

        // Add visual feedback for interactivity
        confirmButton.setStyle("-fx-cursor: hand;");
        cancelButton.setStyle("-fx-cursor: hand;");

        // Add listener to emailField to highlight confirmButton when email is not empty
        DropShadow highlightEffect = new DropShadow();
        highlightEffect.setRadius(20.0);
        highlightEffect.setColor(Color.rgb(0, 47, 255)); // Blue highlight color

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                confirmButton.setEffect(null); // Remove effect when email is empty
            } else {
                confirmButton.setEffect(highlightEffect); // Apply effect when email is not empty
            }
        });
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleConfirmButton(new ActionEvent(confirmButton, null));
        }
    }

    @FXML
    private void handleConfirmButton(ActionEvent event) {
        if (!isEmailVerified) {
            // Step 1: Validate email and fetch security question
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                ErrorAlert.show("Invalid Email", "Please enter your email address.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                ErrorAlert.show("Invalid Email", "Please enter a valid email address.");
                return;
            }

            // Check if user exists
            User user = userDAO.getUser(email);
            if (user == null) {
                ErrorAlert.show("User Not Found", "No user found with email: " + email);
                return;
            }

            // Fetch security question
            try {
                String[] securityData = securityQuestionDAO.getSecurityQuestion(email);
                if (securityData == null || securityData[0] == null) {
                    ErrorAlert.show("Security Question Error", "No security question found for this email.");
                    return;
                }

                // Display security question and enable answer field
                securityQuestionField.setText(securityData[0]);
                securityQuestionField.setDisable(true); // Keep it non-editable
                securityAnswerField.setDisable(false);
                emailField.setDisable(true); // Prevent changing email after verification
                isEmailVerified = true;
                confirmButton.setText("Verify Answer");
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to fetch security question: " + e.getMessage());
            }
        } else {
            // Step 2: Verify security answer and reset password
            String email = emailField.getText().trim();
            String userAnswer = securityAnswerField.getText().trim();

            if (userAnswer.isEmpty()) {
                ErrorAlert.show("Invalid Answer", "Please enter your security answer.");
                return;
            }

            try {
                String[] securityData = securityQuestionDAO.getSecurityQuestion(email);
                if (securityData == null || securityData[1] == null) {
                    ErrorAlert.show("Security Answer Error", "No security answer found for this email.");
                    return;
                }

                // Verify the answer
                String storedAnswer = securityData[1];
                if (!userAnswer.equals(storedAnswer)) {
                    ErrorAlert.show("Verification Failed", "The security answer is incorrect.");
                    return;
                }

                // Show success message before prompting for new password
                SuccessAlert.show("Success", "Security answer verified successfully! Please dismiss this notification to proceed with setting a new password.");

                // Proceed to prompt for new password after the success message is dismissed
                promptAndUpdatePassword(email);
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to verify security answer: " + e.getMessage());
            }
        }
    }

    private void promptAndUpdatePassword(String email) {
        // Create a custom dialog for password input
        Dialog<String> passwordDialog = new Dialog<>();
        passwordDialog.setTitle("Reset Password");
        passwordDialog.setHeaderText("Enter your new password");

        // Set custom stage icon
        Stage dialogStage = (Stage) passwordDialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/shieldIcon.png")));

        // Set custom content icon
        ImageView contentIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/project/symbol/passwordChangeIcon.png")));
        contentIcon.setFitWidth(60);
        contentIcon.setFitHeight(60);
        passwordDialog.setGraphic(contentIcon);

        // Increase font size and window size
        passwordDialog.getDialogPane().setStyle("-fx-font-size: 16px;");
        passwordDialog.getDialogPane().setPrefSize(400, 200);

        // Add PasswordField for masked input
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("New Password:"), passwordField);
        passwordDialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle Enter key for the PasswordField
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ((Button) passwordDialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
            }
        });

        // Set the result converter to return the password
        passwordDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = passwordDialog.showAndWait();

        if (result.isPresent()) {
            String newPassword = result.get().trim();
            if (newPassword.isEmpty() || newPassword.length() < 6) {
                ErrorAlert.show("Invalid Password", "New password must be at least 6 characters long.");
                return;
            }

            // Update password in the database
            User user = userDAO.getUser(email);
            if (user != null) {
                user.setPassword(newPassword);
                userDAO.updateUser(user);
                SuccessAlert.show("Success", "Password reset successfully!");
                forgotPasswordStage.close(); // Close the modal stage
            } else {
                ErrorAlert.show("Database Error", "User not found during password reset.");
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        forgotPasswordStage.close(); // Close the modal stage
    }
}