package com.example.project.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MessageController {
    @FXML
    private ImageView crackedCookieImage;

    @FXML
    private Label fortuneMessage;

    @FXML
    private Button closeButton;

    private HomeController homeController;

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    public void setFortune(String fortune) {
        fortuneMessage.setText(fortune);

        // Add a simple fade-in animation for the fortune message
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), fortuneMessage);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    private void initialize() {
        // Hide the message initially for the animation
        fortuneMessage.setOpacity(0.0);
    }

    @FXML
    private void handleNewFortune() {
        // Get a new fortune from the home controller
        if (homeController != null) {
            setFortune(homeController.getRandomFortune());
        }
    }

    @FXML
    private void handleClose() {
        // Close the fortune window
        Stage stage = (Stage) fortuneMessage.getScene().getWindow();
        stage.close();
    }
}