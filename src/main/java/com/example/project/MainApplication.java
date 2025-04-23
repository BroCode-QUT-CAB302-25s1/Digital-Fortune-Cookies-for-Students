package com.example.project;

import com.example.project.controller.MainController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        // Remove title bar
        stage.initStyle(StageStyle.UNDECORATED);

        // Set position
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();

        // Pass the stage to MainController
        MainController controller = fxmlLoader.getController();
        controller.setSplashStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}

//        // Wait 2 seconds, then open sign in window
//        PauseTransition delay = new PauseTransition(Duration.seconds(2));
//        delay.setOnFinished(event -> {
//            stage.close();
//            try {
//                // Load new FXML after splash
////                Parent signInRoot = FXMLLoader.load(getClass().getResource("signin-view.fxml"));
//                FXMLLoader signInRoot = new FXMLLoader(getClass().getResource("signin-view.fxml"));
//                Stage signInStage = (Stage) new Stage();
//                Scene signInScene = new Scene(signInRoot.load());
//                signInStage.setTitle("Sign in");
//                signInStage.setScene(signInScene);
//                signInStage.show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        delay.play();
