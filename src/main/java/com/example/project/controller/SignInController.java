package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SignInController {
    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label footerLabel;

    @FXML
    private Label signupLink;

    @FXML
    private Label forgotLabel;


    private Scene scene;
    private Stage signInStage;
    private Parent root;
    private boolean initialized = false; // Prevent double initialization

    // Setter to pass the Stage from MainController or SignUpController
    public void setSignInStage(Stage stage) {
        this.signInStage = stage;
    }

    @FXML
    public void initialize() {
        if (initialized) {
            System.out.println("SignInController: Already initialized, skipping.");
            return;
        }
        initialized = true;

        System.out.println("SignInController: Initializing...");
        if (signInStage != null) {
            System.out.println("SignInController: Initialized with stage title: " + signInStage.getTitle());
        } else {
            System.out.println("SignInController: Stage is null during initialization.");
        }

        // Clear any existing handlers to prevent duplicates
        loginButton.setOnAction(null);
        signupLink.setOnMouseClicked(null);

        // Initialize UI components if needed
        loginButton.setOnAction(this::handleLoginButton);
        signupLink.setOnMouseClicked(this::handleSignupLink);

        // Optional: Add visual feedback for interactivity
        signupLink.setStyle("-fx-cursor: hand;"); // Makes cursor a hand on hover
        forgotLabel.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleSignupLink(MouseEvent mouseEvent) {
        try {
            // Load signup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/signup-view.fxml"));
            if (loader.getLocation() == null) {
                System.err.println("Error: signup-view.fxml not found at /com/example/project/signup-view.fxml");
                return;
            }
            root = loader.load();
            Stage signUpStage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow(); // New stage for signup
            Scene signUpScene = new Scene(root);
            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(signUpScene);

            // Pass the new stage and sign-in scene to SignUpController
            SignUpController signUpController = loader.getController();
            signUpController.setSignUpStage(signUpStage);
            signUpController.setSignInScene(signupLink.getScene(), this);

            // Close the sign-in stage
//            signInStage.close();
//            System.out.println("SignInController: Sign-in stage closed");

            signUpStage.show();

        } catch (IOException e) {
            descriptionLabel.setText("Error loading signup page.");
            descriptionLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            descriptionLabel.setText("Please enter both email and password.");
            descriptionLabel.setStyle("-fx-text-fill: red;");
        }else {
            // Placeholder for authentication logic
            descriptionLabel.setText("Attempting to sign in...");
            descriptionLabel.setStyle("-fx-text-fill: black;");
            // Add your authentication logic here (e.g., check credentials against a database)
            try {
                // Example: Load a new scene after successful login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                descriptionLabel.setText("Error loading dashboard.");
                descriptionLabel.setStyle("-fx-text-fill: red;");
                e.printStackTrace();
            }
        }

    }


}
