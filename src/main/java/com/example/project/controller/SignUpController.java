package com.example.project.controller;

import com.example.project.model.IUserDAO;
import com.example.project.model.SqliteUserDAO;
import com.example.project.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class SignUpController {
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField studyGoalField;

    @FXML
    private Button signupButton;

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label studygoalField; // Note: This is a Label, not the TextField

    private Stage signUpStage;
    private Scene signInScene; // Store the sign-in scene
    private SignInController signInController; // Store the SignInController instance

    // Setter to pass the Stage from SignInController
    public void setSignUpStage(Stage stage) {
        this.signUpStage = stage;
    }

    // Setter to pass the sign-in Scene and SignInController
    public void setSignInScene(Scene signInScene, SignInController signInController) {
        this.signInScene = signInScene;
        this.signInController = signInController;
    }

    @FXML
    private void initialize() {
        // Optional: Initialize UI components, e.g., set default values or styles
        signupButton.setOnAction(event -> handleSignupButton());
        backButton.setOnAction(event -> handleBackButton());
    }

    @FXML
    private void handleBackButton() {
        if (signInScene == null || signUpStage == null) {
            System.err.println("SignUpController: Error: signInScene or signUpStage is null");
            return;
        }
        try {
            // Update the stage in SignInController
            if (signInController != null) {
                signUpStage.setScene(signInScene);
            } else {
                System.err.println("SignUpController: Warning: signInController is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private IUserDAO userDAO;

    public SignUpController() {
        userDAO = new SqliteUserDAO();
    }

    @FXML
    private void handleSignupButton() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String studyGoal = studyGoalField.getText().trim();

        // Basic validation
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            LOGGER.warning("Invalid email address: " + email);
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            LOGGER.warning("Password must be at least 6 characters long.");
            return;
        }

        if (studyGoal.isEmpty() || !studyGoal.matches("\\d+")) {
            LOGGER.warning("Invalid study time goal: " + studyGoal);
            return;
        }

        int studyGoalValue;
        try {
            studyGoalValue = Integer.parseInt(studyGoal);
        } catch (NumberFormatException e) {
            LOGGER.warning("Study goal must be a valid number: " + studyGoal);
            return;
        }

        // Check if email (username) already exists
        if (userDAO.getUser(email) != null) {
            LOGGER.warning("Email already registered: " + email);
            return;
        }

        // Create new user
        User newUser = new User(email, password);

        // Save to database
        try {
            userDAO.addUser(newUser);
            LOGGER.info("Signup successful: Email=" + email);

            // Navigate to dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/view/dashboard.fxml"));
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            LOGGER.severe("Failed to load dashboard: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.severe("Failed to save user to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

}