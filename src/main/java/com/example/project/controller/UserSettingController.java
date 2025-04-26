package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class UserSettingController {
    private static final Logger LOGGER = Logger.getLogger(UserSettingController.class.getName());

    @FXML
    private TextField usernameField;

    @FXML
    private TextField preferredNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField githubField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField jobField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField dobField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    public UserSettingController() {
        userDAO = new SqliteUserDAO();
    }

    private User currentUser;
    private IUserDAO userDAO;

    public void setUser(User user) {
        this.currentUser = user;
        displayUserData();
    }

    private Stage userSettingStage;
    private Scene userDisplayScene;
    private UserDisplayController userDisplayController;

    public void setStage(Stage stage){
        this.userSettingStage = stage;
    }

    public void setScene(Scene userDisplayScene, UserDisplayController userDisplayController) {
        this.userDisplayScene = userDisplayScene;
        this.userDisplayController = userDisplayController;
    }

    private void initialize() {
        displayUserData();
        cancelButton.setOnAction(this::handleCancelButton);

    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        if (userDisplayController != null) {
            userSettingStage.setScene(userDisplayScene);
        }
    }

    private void displayUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            preferredNameField.setText(currentUser.getPreferredName());
            firstNameField.setText(currentUser.getFirstName());
            lastNameField.setText(currentUser.getLastName());
            emailField.setText(currentUser.getEmail());
            githubField.setText(currentUser.getGithub());
            phoneField.setText(currentUser.getPhone());
            locationField.setText(currentUser.getLocation());
            jobField.setText(currentUser.getJob());
            genderField.setText(currentUser.getGender());
            dobField.setText(currentUser.getDob());
        }
    }

    @FXML
    private void handleSaveButton() {
        // Update the user object
        currentUser.setUsername(usernameField.getText());
        currentUser.setPreferredName(preferredNameField.getText());
        currentUser.setFirstName(firstNameField.getText());
        currentUser.setLastName(lastNameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setGithub(githubField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setLocation(locationField.getText());
        currentUser.setJob(jobField.getText());
        currentUser.setGender(genderField.getText());
        currentUser.setDob(dobField.getText());

        // Save to database
        saveToDB(currentUser);
    }

    private boolean saveToDB(User user) {
        // Input validation
        if (!validateUserInput(user)) {
            LOGGER.warning("Validation failed for user: " + user.getEmail());
            return false;
        }

        try {
            User existingUser = userDAO.getUser(user.getEmail());
            if (existingUser != null) {
                userDAO.updateUser(user);
                LOGGER.info("User updated successfully: " + user.getEmail());
            } else {
                userDAO.addUser(user);
                LOGGER.info("User added successfully: " + user.getEmail());
            }
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to save user: " + e.getMessage());
            return false;
        }
    }

    private boolean validateUserInput(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            return false;
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return false;
        }
        return true;
    }
}

