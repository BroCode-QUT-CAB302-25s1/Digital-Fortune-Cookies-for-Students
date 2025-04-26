package com.example.project.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController {

    @FXML
    private ImageView fortuneCookieImage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressText;

    @FXML
    private Button userDisplayButton;

    private Parent root;
    private Stage homeStage;
    private SignInController signInController; // Store the SignInController instance

    public void setHomeStage(Stage stage, SignInController signInController) {
        this.homeStage = stage;
        this.signInController = signInController;
    }

    @FXML
    private void initialize() {
        // Add click event to the fortune cookie image
        fortuneCookieImage.setOnMouseClicked(this::openFortuneScreen);

        userDisplayButton.setOnAction(this::handleProfileButton);
    }

    /**
     * Opens a new window displaying a fortune with a cracked cookie image
     */
    private void openFortuneScreen(Event event) {
        try {
            // Load the cracked fortune cookie screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/fortune-screen.fxml"));
            Parent fortuneRoot = loader.load();

            // Get the controller and pass a random fortune
            MessageController fortuneController = loader.getController();
            fortuneController.setFortune(getRandomFortune());

            // Create a new scene and stage
            Scene fortuneScene = new Scene(fortuneRoot);
            Stage fortuneStage = new Stage();
            fortuneStage.setTitle("Your Fortune");
            fortuneStage.setScene(fortuneScene);
            fortuneStage.initStyle(StageStyle.UTILITY);

            // Show the new stage
            fortuneStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Placeholder fortune messages - REMOVE LATER
    public String getRandomFortune() {
        // Array of fortune messages
        String[] fortunes = {
                "Good things come to those who wait... but better things come to those who work for it.",
                "Your creativity will lead you to success.",
                "New opportunities await you this week.",
                "A smile is your passport into the hearts of others.",
                "Your hard work is about to pay off. Remember, dreams don't work unless you do.",
                "Adventure can be real happiness.",
                "The greatest risk is not taking one.",
                "Today it's up to you to create the peacefulness you long for.",
                "Your ability to accomplish tasks will follow with success.",
                "You will be rewarded for your patience and persistence."
        };

        // Return a random fortune
        return fortunes[(int) (Math.random() * fortunes.length)];
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        // implement later
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        // implement later
    }

    @FXML
    private void handleProfileButton(ActionEvent event) {
        try {
            // Load signup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/userdisplay-view.fxml"));
            root = loader.load();
            Stage userDisplayStage = (Stage) ((Node)event.getSource()).getScene().getWindow(); // New stage for signup
            Scene userDisplayScene = new Scene(root);
            userDisplayStage.setTitle("Profile");
            userDisplayStage.setScene(userDisplayScene);

            // Pass the new stage and sign-in scene to userDisplayController
            UserDisplayController userDisplayController = loader.getController();
            userDisplayController.setStage(userDisplayStage);
            userDisplayController.setScene(userDisplayButton.getScene(), this);

            userDisplayStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}