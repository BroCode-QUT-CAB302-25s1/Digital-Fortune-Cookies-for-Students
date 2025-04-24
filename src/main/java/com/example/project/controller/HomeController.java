package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Label welcomeTitle;

    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView fortuneCookieImage;

    @FXML
    private Button newFortuneButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressText;

    private Stage homeStage;
    private SignInController signInController; // Store the SignInController instance

    public void setHomeStage(Stage stage, SignInController signInController) {
        this.homeStage = stage;
        this.signInController = signInController;
        System.out.println("SignUpController: Sign-in Scene and Controller set.");
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleNewFortuneButton(ActionEvent event) {
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
    }

    @FXML
    private void handleHistoryButton(ActionEvent event) {
    }

    @FXML
    private void handleProfileButton(ActionEvent event) {
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
    }
}