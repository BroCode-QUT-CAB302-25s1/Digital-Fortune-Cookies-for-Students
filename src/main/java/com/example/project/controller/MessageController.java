package com.example.project.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;


public class MessageController {
    @FXML
    private StackPane fortuneContainer;
    @FXML
    private VBox paperContainer;
    @FXML
    private ImageView crackedCookieImage;
    @FXML
    private Label fortuneMessage;
    @FXML
    private Button closeButton;
    @FXML
    private Button newFortuneButton;

    private HomeController homeController;

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) fortuneMessage.getScene().getWindow();
        stage.close();
    }

    public void setFortune(String fortune) {
        // Hide elements initially
        crackedCookieImage.setOpacity(0);
        fortuneMessage.setOpacity(0);
        paperContainer.setScaleY(0);
        
        // 1. Crack Animation
        Timeline crackAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(crackedCookieImage.scaleXProperty(), 0.8)),
            new KeyFrame(Duration.ZERO, new KeyValue(crackedCookieImage.scaleYProperty(), 0.8)),
            new KeyFrame(Duration.ZERO, new KeyValue(crackedCookieImage.opacityProperty(), 0)),
            new KeyFrame(Duration.millis(400), new KeyValue(crackedCookieImage.scaleXProperty(), 1.1)),
            new KeyFrame(Duration.millis(400), new KeyValue(crackedCookieImage.scaleYProperty(), 1.1)),
            new KeyFrame(Duration.millis(400), new KeyValue(crackedCookieImage.opacityProperty(), 1)),
            new KeyFrame(Duration.millis(600), new KeyValue(crackedCookieImage.scaleXProperty(), 1)),
            new KeyFrame(Duration.millis(600), new KeyValue(crackedCookieImage.scaleYProperty(), 1))
        );
        
        // 2. Paper Unfold Animation
        Timeline paperUnfold = new Timeline(
            new KeyFrame(Duration.millis(600), new KeyValue(paperContainer.scaleYProperty(), 0)),
            new KeyFrame(Duration.millis(1200), new KeyValue(paperContainer.scaleYProperty(), 1))
        );
        
        // 3. Fortune Text Reveal
        FadeTransition textReveal = new FadeTransition(Duration.millis(800), fortuneMessage);
        textReveal.setFromValue(0);
        textReveal.setToValue(1);
        
        // Set the fortune text
        fortuneMessage.setText(fortune);
        
        // Play all animations in sequence
        SequentialTransition masterSequence = new SequentialTransition(
            crackAnimation,
            paperUnfold,
            textReveal
        );
        
        // Add some particles for extra effect
        masterSequence.setOnFinished(e -> addSparkleEffect());
        
        masterSequence.play();
    }
    
    private void addSparkleEffect() {
        // Clear any existing sparkles first
        fortuneContainer.getChildren().removeIf(node -> node instanceof Circle);

        // Add subtle sparkle particles around the fortune
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Circle sparkle = new Circle(2, Color.GOLD);
            fortuneContainer.getChildren().add(sparkle);

            double startX = random.nextDouble() * fortuneContainer.getWidth();
            double startY = random.nextDouble() * fortuneContainer.getHeight();

            Timeline sparkleAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(sparkle.layoutXProperty(), startX),
                    new KeyValue(sparkle.layoutYProperty(), startY),
                    new KeyValue(sparkle.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(1000),
                    new KeyValue(sparkle.layoutYProperty(), startY - 20),
                    new KeyValue(sparkle.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(2000),
                    new KeyValue(sparkle.layoutYProperty(), startY - 40),
                    new KeyValue(sparkle.opacityProperty(), 0))
            );
            // Remove the sparkle when animation is done
            sparkleAnimation.setOnFinished(e -> fortuneContainer.getChildren().remove(sparkle));

            // Don't set to indefinite, let it play once and remove
            sparkleAnimation.setCycleCount(1);
            sparkleAnimation.play();
        }
    }
}