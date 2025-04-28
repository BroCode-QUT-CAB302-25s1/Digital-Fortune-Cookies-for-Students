package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.ProfileImageDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button signupButton;

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    private final IUserDAO userDAO;
    private final UserPreferencesDAO preferencesDAO;
    private final ProfileImageDAO profileImageDAO;

    // Default constructor for production
    public SignUpController() {
        this.userDAO = new SqliteUserDAO();
        this.preferencesDAO = new UserPreferencesDAO();
        this.profileImageDAO = new ProfileImageDAO();
    }

    // Constructor for testing with dependency injection
    public SignUpController(IUserDAO userDAO) {
        this.userDAO = userDAO;
        this.preferencesDAO = new UserPreferencesDAO();
        this.profileImageDAO = new ProfileImageDAO();
    }

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
        signupButton.setOnAction(this::handleSignupButton);
        backButton.setOnAction(this::handleBackButton);

        // Add Enter key handler to input field
        emailField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);
        usernameField.setOnKeyPressed(this::handleKeyPressed);

        // Optional: Add visual feedback for interactivity
        signupButton.setStyle("-fx-cursor: hand;");
        backButton.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSignupButton(new ActionEvent(signupButton, null));
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
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

    @FXML
    private void handleSignupButton(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String username = usernameField.getText().trim();

        // Basic validation
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            ErrorAlert.show("Invalid Email", "Invalid email address.");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            ErrorAlert.show("Invalid Password", "Password must be at least 6 characters long.");
            return;
        }

        if (username.isEmpty()) {
            ErrorAlert.show("Invalid Username", "Invalid username input");
            return;
        }

        // Check if email or username already exists
        if (userDAO.getUser(email) != null) {
            ErrorAlert.show("Email Registered", "Email already registered.");
            return;
        }
        if (userDAO.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            ErrorAlert.show("Username Taken", "Username already taken.");
            return;
        }

        // Create new user
        User newUser = new User(email, password, username);

        // Save to database
        try {
            userDAO.addUser(newUser);
            // Save default preferences to user_preferences table
            preferencesDAO.savePreferences(email, "", "");
            // Save default profile image to preferences table
            profileImageDAO.saveProfileImage(email, "/com/example/project/symbol/digitalCookieMainIcon1.png");
            ErrorAlert.show("Congratulation!", "Your Email: " + email + " is registered successfully!");
            // Navigate back to sign-in scene
            if (signInScene != null && signUpStage != null) {
                signUpStage.setScene(signInScene);
            } else {
                System.err.println("SignUpController: Error: signInScene or signUpStage is null");
            }
        } catch (SQLException e) {
            ErrorAlert.show("Database Error", "Failed to save user or preferences: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            ErrorAlert.show("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}