package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class SignUpController {

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
    private boolean initialized = false; // Prevent double initialization

    // Setter to pass the Stage from SignInController
    public void setSignUpStage(Stage stage) {
        this.signUpStage = stage;
        System.out.println("SignUpController: Stage set with title: " + (stage != null ? stage.getTitle() : "null"));
    }

    // Setter to pass the sign-in Scene and SignInController
    public void setSignInScene(Scene signInScene, SignInController signInController) {
        this.signInScene = signInScene;
        this.signInController = signInController;
        System.out.println("SignUpController: Sign-in Scene and Controller set.");
    }

    @FXML
    private void initialize() {
        if (initialized) {
            System.out.println("SignUpController: Already initialized, skipping.");
            return;
        }
        initialized = true;

        System.out.println("SignUpController: Initializing...");
        if (signUpStage != null) {
            System.out.println("SignUpController: Initialized with stage title: " + signUpStage.getTitle());
        } else {
            System.out.println("SignUpController: Stage is null during initialization.");
        }

        // Clear any existing handlers to prevent duplicates
        signupButton.setOnAction(null);
        backButton.setOnAction(null);

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
            System.err.println("SignUpController: Exception while restoring sign-in scene");
            e.printStackTrace();
        }
    }


    private void handleSignupButton() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String studyGoal = studyGoalField.getText().trim();

        // Basic validation
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long.");
            return;
        }

        if (studyGoal.isEmpty() || !studyGoal.matches("\\d+")) {
            showAlert("Error", "Please enter a valid study time goal (in minutes).");
            return;
        }

        // Process signup (e.g., save to database or pass to another service)
        System.out.println("Signup successful: Email=" + email + ", Study Goal=" + studyGoal + " minutes");

        // Optionally, navigate to another scene (e.g., dashboard)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/view/dashboard.fxml"));
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load dashboard.");
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}