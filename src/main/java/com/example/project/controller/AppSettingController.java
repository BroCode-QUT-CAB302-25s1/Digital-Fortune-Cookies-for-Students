package com.example.project.controller;

import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AppSettingController {

    @FXML
    private Label titleLabel;

    @FXML
    private RadioButton lightButton;

    @FXML
    private RadioButton darkButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Button signOutButton;

    @FXML
    private CheckBox runOnStartupCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    private Stage settingsStage;
    private Scene homeScene;
    private HomeController homeController;
    private User currentUser;
    private final UserPreferencesDAO preferencesDAO;

    public AppSettingController() {
        preferencesDAO = new UserPreferencesDAO();
    }

    public void setStage(Stage stage) {
        this.settingsStage = stage;
    }

    public void setScene(Scene homeScene, HomeController homeController) {
        this.homeScene = homeScene;
        this.homeController = homeController;
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateUser(user);
    }

    private void updateUser(User user) {
        if (user != null && user.getEmail() != null) {
            emailLabel.setText("Logged in as " + user.getEmail());
        } else {
            emailLabel.setText("Logged in as Guest");
        }
    }

    @FXML
    private void initialize() {
        // Bind radio buttons to a ToggleGroup programmatically
        lightButton.setToggleGroup(new javafx.scene.control.ToggleGroup());
        darkButton.setToggleGroup(lightButton.getToggleGroup());

        // Set default theme (e.g., Light)
        lightButton.setSelected(true);

        // Load existing preferences if available
        if (currentUser != null && currentUser.getEmail() != null) {
            try {
                String[] preferences = preferencesDAO.getPreferences(currentUser.getEmail());
                if (preferences != null) {
                    String theme = preferences[0]; // Assuming theme is stored in preferences[0]
                    boolean runOnStartup = Boolean.parseBoolean(preferences[1]); // Assuming runOnStartup is stored in preferences[1]
                    lightButton.setSelected("Light".equals(theme));
                    darkButton.setSelected("Dark".equals(theme));
                    runOnStartupCheckBox.setSelected(runOnStartup);
                }
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to load preferences: " + e.getMessage());
            }
        }

        // Set up event handlers
        saveButton.setOnAction(this::handleSaveButton);
        backButton.setOnAction(this::handleBackButton);
        signOutButton.setOnAction(this::handleSignOutButton);

        // Add visual feedback for interactivity
        saveButton.setStyle("-fx-cursor: hand;");
        backButton.setStyle("-fx-cursor: hand;");
        signOutButton.setStyle("-fx-cursor: hand;");
        lightButton.setStyle("-fx-cursor: hand;");
        darkButton.setStyle("-fx-cursor: hand;");
        runOnStartupCheckBox.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (currentUser == null || currentUser.getEmail() == null) {
            ErrorAlert.show("Error", "No user data available to save settings.");
            return;
        }

        try {
            // Save theme preference
            String selectedTheme = lightButton.isSelected() ? "Light" : "Dark";
            preferencesDAO.saveTheme(currentUser.getEmail(), selectedTheme);

            // Save run on startup preference
            boolean runOnStartup = runOnStartupCheckBox.isSelected();
            preferencesDAO.saveRunOnStartup(currentUser.getEmail(), runOnStartup);

            ErrorAlert.show("Success", "Settings saved successfully.");
            settingsStage.close(); // Close the settings dialog
        } catch (Exception e) {
            ErrorAlert.show("Database Error", "Failed to save settings: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        settingsStage.close(); // Close the settings dialog
    }

    @FXML
    private void handleSignOutButton(ActionEvent event) {
        try {
            // Load sign-in screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/signin-view.fxml"));
            Parent signInRoot = loader.load();

            // Get the sign-in controller
            SignInController signInController = loader.getController();

            // Set up the new scene and stage
            Stage signInStage = new Stage();
            Scene signInScene = new Scene(signInRoot);
            signInStage.setScene(signInScene);
            signInStage.setTitle("Sign In");

            // Pass the stage to the sign-in controller
            signInController.setSignInStage(signInStage);

            // Close the settings stage and home stage
            settingsStage.close();
            if (homeController != null && homeScene != null) {
                Stage homeStage = (Stage) homeScene.getWindow();
                homeStage.close();
            }

            signInStage.show();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load sign-in screen: " + e.getMessage());
        }
    }
}