package com.example.project.util;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ErrorAlert {
    public static void show(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set custom content icon (already present)
        try {
            Image icon = new Image(ErrorAlert.class.getResourceAsStream("/com/example/project/symbol/errorIcon1.png"));
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            alert.setGraphic(imageView);
        } catch (Exception e) {
            System.err.println("Failed to load error content icon: " + e.getMessage());
        }

        // Set custom window icon
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(ErrorAlert.class.getResourceAsStream("/com/example/project/symbol/errorIcon.png")));
        } catch (Exception e) {
            System.err.println("Failed to load error window icon: " + e.getMessage());
        }

        // Increase font size of the message text
        alert.getDialogPane().setStyle("-fx-font-size: 14px;");

        alert.showAndWait();
    }
}