package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class UserDisplayController {


    private Stage homeStage;
    private HomeController homeController; // Store the SignInController instance

    public void setHomeStage(Stage stage, HomeController homeController) {
        this.homeStage = stage;
        this.homeController = homeController;
    }

    @FXML
    private void initialize() {
    }

}
