package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class UserDisplayController {

    @FXML
    private ImageView profileImage;

    @FXML
    private Pane profileCard;

    @FXML
    private Label statusOnline;

    @FXML
    private Label profileName;

    @FXML
    private Label githubLink;

    @FXML
    private Label emailLink;

    @FXML
    private Label phoneNumberLink;

    @FXML
    private Label currentJob;

    @FXML
    private Label currentPosition;

    @FXML
    private Label currentLocation;

    @FXML
    private Label footerLabel;

    @FXML
    private Pane infoPanel;

    @FXML
    private Label userName;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label preferredName;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Label userEmail;

    @FXML
    private Label userGender;

    @FXML
    private Label titleHeader;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editButton;

    @FXML
    private Button changePasswordButton;


    private Stage userDisplayStage;
    private Scene homeScene;
    private HomeController homeController; // Store the SignInController instance

    public void setHomeStage(Stage stage){
        this.userDisplayStage = stage;
    }

    public void setHomeScene(Scene homeScene, HomeController homeController) {
        this.homeScene = homeScene;
        this.homeController = homeController;

        // Clip the image into a circle
        Circle clip = new Circle(75,75,75); // x, y, radius
        profileImage.setClip(clip);
    }

    @FXML
    private void initialize() {
        userDataDisplay();
        cancelButton.setOnAction(this::handleCancelButton);

    }

    // Temporarily
    private void userDataDisplay(){
        // Set text fields with values from FXML
        profileName.setText("BroCode");
        githubLink.setText("@BroCode-QUT");
        emailLink.setText("brocode.QUT@gmail.com");
        phoneNumberLink.setText("0000 000 0001");
        currentJob.setText("Backend Developer");
        currentPosition.setText("Software Developer");
        currentLocation.setText("Australia");
        footerLabel.setText("Joined from April 2025");
        userName.setText("brocodeTest01");
        firstName.setText("BroCode");
        lastName.setText("QUT");
        preferredName.setText("BroCode");
        dateOfBirth.setText("01/01/2025");
        userEmail.setText("brocode.QUT@gmail.com");
        userGender.setText("Male");
        statusOnline.setText("Online");
        titleHeader.setText("My Profile");
    }

    @FXML
    private void handleChangePasswordButton(ActionEvent event) {
    }

    @FXML
    private void handleEditButton(ActionEvent event) {
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        if (homeController != null) {
            userDisplayStage.setScene(homeScene);
        }
    }

}
