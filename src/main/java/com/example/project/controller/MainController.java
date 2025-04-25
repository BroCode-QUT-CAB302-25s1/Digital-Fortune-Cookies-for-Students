package com.example.project.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage signinStage;

    private boolean firstOpeningFlag = true;

    // Setter to pass the Stage from MainApplication
    public void setSplashStage(Stage stage) {
        this.signinStage = stage;
    }

    @FXML
    private void initialize() {
        int time = firstOpeningFlag ? 2 : 0;
        // Debug: Check if loadingLabel is bound
        // Start the transition to sign-in screen after "time" seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        this.firstOpeningFlag = false;
        delay.setOnFinished(event -> {
            if (signinStage == null) {
                System.err.println("Error: signinStage is null. Stage must be set.");
                return;
            }
            signinStage.close();
            try {
                // Load sign-in FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/signin-view.fxml"));
                Scene signInScene = new Scene(loader.load());
                Stage signInStage = new Stage();
                signInStage.setTitle("Sign In");
                signInStage.initStyle(StageStyle.DECORATED);
                signInStage.setResizable(false);
                signInStage.setScene(signInScene);
                signInStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/digitalCookieMainIcon2.png")));

                // Pass the stage to SignInController
                SignInController signInController = loader.getController();
                signInController.setSignInStage(signinStage);

                signInStage.show();
            } catch (IOException e) {
                System.err.println("IOException while loading signin-view.fxml");
                e.printStackTrace();
            }
        });
        delay.play();
    }


}