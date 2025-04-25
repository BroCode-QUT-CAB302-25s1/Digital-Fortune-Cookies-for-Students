package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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


    // Setter to pass the Stage from MainController or SignUpController
    public void setSignInStage(Stage stage) {
        this.signInStage = stage;
    }

    @FXML
    public void initialize() {
        loginButton.setOnAction(null);
        signupLink.setOnMouseClicked(null);

        // Initialize UI components if needed
        loginButton.setOnAction(this::handleLoginButton);
        signupLink.setOnMouseClicked(this::handleSignupLink);

        // Add Enter key handler to emailField and passwordField
        emailField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);

        // Optional: Add visual feedback for interactivity
        signupLink.setStyle("-fx-cursor: hand;"); // Makes cursor a hand on hover
        forgotLabel.setStyle("-fx-cursor: hand;");
        loginButton.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
//            System.out.println("SignInController: Enter key pressed, triggering login");
            handleLoginButton(new ActionEvent(loginButton, null));
        }
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/home-view.fxml"));
                Stage homeStage = (Stage) ((Node)loginButton).getScene().getWindow();
                root = loader.load();
                Scene homeScene = new Scene(root);
                homeStage.setTitle("Home");
                homeStage.setScene(homeScene);


                // Pass the new stage and sign-in scene to SignUpController
                HomeController homeController = loader.getController();
                homeController.setHomeStage(homeStage, this);

                homeStage.show();
            } catch (IOException e) {
                descriptionLabel.setText("Error loading dashboard.");
                descriptionLabel.setStyle("-fx-text-fill: red;");
                e.printStackTrace();
            }
        }

    }


}
