package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import com.example.project.util.SuccessAlert;
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
import javafx.stage.Modality;
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

    private final IUserDAO userDAO;

    // Default constructor for production
    public SignInController() {
        this.userDAO = new SqliteUserDAO();
    }

    // Constructor for testing with dependency injection
    public SignInController(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
        forgotLabel.setOnMouseClicked(this::handleForgotLabel);

        // Add Enter key handler to emailField and passwordField
        emailField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);

        // Add visual feedback for interactivity
        signupLink.setStyle("-fx-cursor: hand;"); // Makes cursor a hand on hover
        forgotLabel.setStyle("-fx-cursor: hand;");
        loginButton.setStyle("-fx-cursor: hand;");

        // Add hover effects for signupLink and forgotLabel
        // Hover effect for signupLink (Create Account)
        signupLink.setOnMouseEntered(event -> {
            signupLink.setStyle("-fx-cursor: hand; -fx-text-fill: #001675; -fx-underline: true;");
        });
        signupLink.setOnMouseExited(event -> {
            signupLink.setStyle("-fx-cursor: hand; -fx-text-fill: black; -fx-underline: false;");
        });

        // Hover effect for forgotLabel (Forgot Password)
        forgotLabel.setOnMouseEntered(event -> {
            forgotLabel.setStyle("-fx-cursor: hand; -fx-text-fill: #00802d; -fx-font-size: 13px;");
        });
        forgotLabel.setOnMouseExited(event -> {
            forgotLabel.setStyle("-fx-cursor: hand; -fx-text-fill: black; -fx-font-size: 12px;");
        });
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
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
            Stage signUpStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene signUpScene = new Scene(root);
            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(signUpScene);
            signUpStage.setResizable(false);
            signUpStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/digitalCookieMainIcon2.png")));

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
    private void handleForgotLabel(MouseEvent mouseEvent) {
        try {
            // Load forgot password FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/forgotPassword-view.fxml"));
            Parent forgotPasswordRoot = loader.load();

            // Create a new modal stage for the forgot password dialog
            Stage forgotPasswordStage = new Stage();
            forgotPasswordStage.initOwner(signInStage); // Set signInStage as the owner
            forgotPasswordStage.setTitle("Forgot Password");
            Scene forgotPasswordScene = new Scene(forgotPasswordRoot);
            forgotPasswordStage.setScene(forgotPasswordScene);
            forgotPasswordStage.initModality(Modality.WINDOW_MODAL); // Make it modal
            forgotPasswordStage.setResizable(false);
            forgotPasswordStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/digitalCookieMainIcon2.png")));

            // Pass the stage and sign-in scene to ForgotPasswordController
            ForgotPasswordController forgotPasswordController = loader.getController();
            forgotPasswordController.setStage(forgotPasswordStage);
            forgotPasswordController.setSignInScene(signInStage.getScene(), this);

            forgotPasswordStage.showAndWait(); // Show modal dialog, sign-in remains in background
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load forgot password screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            ErrorAlert.show("Input Error", "Please enter both email and password.");
            return;
        }

        // Authenticate user
        User user = userDAO.getUser(email);
        if (user == null || !user.getPassword().equals(password)) {
            ErrorAlert.show("Authentication Error", "Invalid email or password.");
            return;
        }

        // Successful login - Show success alert
//        SuccessAlert.show("Success", "Login successful!");

        // Proceed to home screen
        try {
            // Load home FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/home-view.fxml"));
            root = loader.load();
            Stage homeStage = (Stage) ((Node) loginButton).getScene().getWindow();
            Scene homeScene = new Scene(root);
            homeStage.setTitle("Home");
            homeStage.setScene(homeScene);
            homeStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/digitalCookieMainIcon2.png")));

            // Pass the stage, sign-in controller, and authenticated user to HomeController
            HomeController homeController = loader.getController();
            homeController.setHomeStage(homeStage, this);
            homeController.setCurrentUser(user);

            // Check user passing
//            System.out.println(user.toString());

            homeStage.show();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Error loading dashboard.");
            e.printStackTrace();
        }
    }
}