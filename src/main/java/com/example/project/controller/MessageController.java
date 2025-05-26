package com.example.project.controller;

import com.example.project.api.GrokResponseFetcher;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import javafx.animation.*;
import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageController {
    @FXML
    private StackPane fortuneContainer; // Added: From friend's FXML for animations
    @FXML
    private VBox paperContainer; // Added: From friend's FXML for animations
    @FXML
    private ImageView crackedCookieImage;
    @FXML
    private Label fortuneMessage;
    @FXML
    private Button closeButton;
    @FXML
    private Button newFortuneButton; // Assumed: For handleNewFortune

    private HomeController homeController;
    private UserPreferencesDAO preferencesDAO;
    private User currentUser;
    private double progress;
    private double remainingHours;
    private double totalStudyHours;

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
        this.preferencesDAO = new UserPreferencesDAO();
    }

    public void setFortune(String fortune) {
        // Animation setup (from master, incorporates Dark-mode-cont' animations)
        crackedCookieImage.setOpacity(0);
        fortuneMessage.setOpacity(0);
        if (paperContainer != null) { // Check to avoid NullPointerException if FXML lacks paperContainer
            paperContainer.setScaleY(0);
        }

        // Crack animation (from master, matches Dark-mode-cont')
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

        // Paper unfold animation (from master, conditional, matches Dark-mode-cont')
        Timeline paperUnfold = new Timeline();
        if (paperContainer != null) {
            paperUnfold.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(600), new KeyValue(paperContainer.scaleYProperty(), 0)),
                new KeyFrame(Duration.millis(1200), new KeyValue(paperContainer.scaleYProperty(), 1))
            );
        }

        // Fortune text reveal (from master, matches Dark-mode-cont')
        FadeTransition textReveal = new FadeTransition(Duration.millis(800), fortuneMessage);
        textReveal.setFromValue(0);
        textReveal.setToValue(1);

        // Set the fortune text (from master)
        fortuneMessage.setText(fortune);

        // Master animation sequence (from master, matches Dark-mode-cont')
        SequentialTransition masterSequence = new SequentialTransition(
            crackAnimation,
            paperUnfold,
            textReveal
        );

        // Trigger sparkle effect (from master, matches Dark-mode-cont')
        masterSequence.setOnFinished(e -> addSparkleEffect());

        masterSequence.play();
    }

    @FXML
    private void initialize() {
        // Original (from master): Hide the message initially for the animation
        fortuneMessage.setOpacity(0.0);
    }

    public void fetchFortune(User user, double progress, double remainingHours, double totalStudyHours) {
        // Original (from master): Store parameters for re-fetching in handleNewFortune
        this.currentUser = user;
        this.progress = progress;
        this.remainingHours = remainingHours;
        this.totalStudyHours = totalStudyHours;

        // Original (from master): Fetch the latest user data
        String name = user.getPreferredName() != null && !user.getPreferredName().isEmpty() ? user.getPreferredName() : user.getUsername();
        String location = user.getLocation();
        String job = user.getJob();
        String gender = user.getGender();

        // Original (from master): Fetch preferences for language and cookie type
        String language = null;
        String cookieType = null;
        try {
            String[] preferences = preferencesDAO.getPreferences(user.getEmail());
            if (preferences != null) {
                language = preferences[0];
                cookieType = preferences[1];
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch user preferences: " + e.getMessage());
        }

        // Original (from master): Calculate learning progress with new formatting
        String learningProgress = null;
        if (remainingHours >= 0 && totalStudyHours > 0) {
            if (remainingHours < 1.0) {
                // Convert remaining hours to minutes if under 1 hour
                int remainingMinutes = (int) Math.round(remainingHours * 60);
                String minutesText = remainingMinutes == 1 ? "minute" : "minutes";
                learningProgress = String.format("%d %s remaining out of %.2f hours chosen", remainingMinutes, minutesText, totalStudyHours);
            } else {
                // Display in hours and minutes if 1 hour or more
                int hours = (int) remainingHours;
                int minutes = (int) Math.round((remainingHours - hours) * 60);
                String hoursText = hours == 1 ? "hour" : "hours";
                String minutesText = minutes == 1 ? "minute" : "minutes";
                if (minutes > 0) {
                    learningProgress = String.format("%d %s %d %s remaining out of %.2f hours chosen", hours, hoursText, minutes, minutesText, totalStudyHours);
                } else {
                    learningProgress = String.format("%d %s remaining out of %.2f hours chosen", hours, hoursText, totalStudyHours);
                }
            }
        }

        // Original (from master): Build the prompt dynamically
        StringBuilder promptBuilder = new StringBuilder("Generate a single motivational sentence (no yapping)");

        // Original (from master): Randomly decide whether to include the name (50% chance)
        Random random = new Random();
        boolean includeName = random.nextBoolean();
        if (includeName && name != null) {
            promptBuilder.append(" named ").append(name);
        }
        if (language != null) {
            promptBuilder.append(" speaking ").append(language);
        }
        if (cookieType != null) {
            promptBuilder.append(" preferring ").append(cookieType).append(" fortune cookies");
        }
        if (learningProgress != null) {
            promptBuilder.append(" with current learning progress of ").append(learningProgress);
        }
        if (location != null) {
            promptBuilder.append(" located in ").append(location);
        }
        if (job != null) {
            promptBuilder.append(" working as ").append(job);
        }
        if (gender != null) {
            promptBuilder.append(" identifying as ").append(gender);
        }
        promptBuilder.append(", balancing academics and personal growth with potential for fatigue or burnout.");

        // Original (from master): Collect available topics for random selection
        List<String> availableTopics = new ArrayList<>();
        if (name != null) availableTopics.add("name");
        if (language != null) availableTopics.add("language");
        if (cookieType != null) availableTopics.add("fortune cookie preference");
        if (learningProgress != null) availableTopics.add("learning progress");
        if (location != null) availableTopics.add("location");
        if (job != null) availableTopics.add("job");
        if (gender != null) availableTopics.add("gender");

        // Original (from master): Randomly select a topic to focus on
        String focusTopic = "academic journey"; // Default focus if no topics are available
        if (!availableTopics.isEmpty()) {
            focusTopic = availableTopics.get(random.nextInt(availableTopics.size()));
        }

        // Original (from master): Append focus instruction and additional instructions
        promptBuilder.append("Highly focus the sentence on their ").append(focusTopic).append(". ");
        promptBuilder.append("Randomly vary the sentence length, minimum is 7 words. ");
        promptBuilder.append("Do not always include the student's name in the sentence, even if provided; use it occasionally for variety.");

        String prompt = promptBuilder.toString();

        // Original (from master): Fetch fortune synchronously
        Platform.runLater(() -> {
            GrokResponseFetcher fetcher = new GrokResponseFetcher("https://api.x.ai/v1/chat/completions", System.getenv("GROK_API_KEY"));
            String fortune = fetcher.fetchGrokResponse(prompt);
            setFortune(fortune);
        });
    }

    @FXML
    private void handleNewFortune() {
        // Original (from master): Fetch a new fortune using the same parameters
        if (homeController != null) {
            fetchFortune(currentUser, progress, remainingHours, totalStudyHours);
        }
    }

    @FXML
    private void handleClose() {
        // Original (from master): Close the fortune window
        Stage stage = (Stage) fortuneMessage.getScene().getWindow();
        stage.close();
    }

    private void addSparkleEffect() {
        // From master: Includes null check, incorporates Dark-mode-cont' logic
        if (fortuneContainer != null) { // Check to avoid NullPointerException if FXML lacks fortuneContainer
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

                sparkleAnimation.setCycleCount(1);
                sparkleAnimation.play();
            }
        }
    }
}