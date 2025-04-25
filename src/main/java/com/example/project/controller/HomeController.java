package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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
        userDisplayButton.setOnAction(this::handleProfileButton);
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

    @FXML
    private void handleSettingsButton(ActionEvent event) {
    }
}