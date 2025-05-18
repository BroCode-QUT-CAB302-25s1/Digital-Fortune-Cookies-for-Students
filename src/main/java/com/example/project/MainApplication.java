package com.example.project;

import com.example.project.controller.MainController;
import com.example.project.database.DatabaseInitializer;
import com.example.project.util.StyleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseInitializer.dropUsersTable(); // For testing
        DatabaseInitializer.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        // Apply theme before showing the stage
//        StyleManager.applyTheme(scene, "main");

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

