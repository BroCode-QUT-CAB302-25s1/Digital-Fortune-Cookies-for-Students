package com.example.project.controller;

import com.example.project.api.GrokResponseFetcher;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.StyleManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FortuneController {
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
    private UserPreferencesDAO preferencesDAO;
    private User currentUser;
    private double progress;
    private double remainingHours;
    private double totalStudyHours;
    private boolean isFetchingFortune;
    private Stage fortuneStage;
    private Scene homeScene;

    public void setStage(Stage stage) {
        this.fortuneStage = stage;
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
        this.preferencesDAO = new UserPreferencesDAO();
        this.isFetchingFortune = false;
        System.out.println("FortuneController: HomeController set");
    }

    public void setScene(Scene homeScene, HomeController homeController) {
        this.homeScene = homeScene;
        this.homeController = homeController;
//        StyleManager.applyTheme(homeScene, "fortune"); // Apply fortune theme
    }

    public void setFortune(String fortune) {
        System.out.println("FortuneController: Setting fortune: " + fortune);
        crackedCookieImage.setOpacity(0);
        fortuneMessage.setOpacity(0);
        if (fortuneMessage != null) {
            fortuneMessage.setTranslateX(-200); // Start off-screen to the left
        }

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

        Timeline messageSlide = new Timeline();
        if (fortuneMessage != null) {
            messageSlide.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(600), new KeyValue(fortuneMessage.translateXProperty(), -200)),
                    new KeyFrame(Duration.millis(1200), new KeyValue(fortuneMessage.translateXProperty(), 0))
            );
        }

        FadeTransition textReveal = new FadeTransition(Duration.millis(800), fortuneMessage);
        textReveal.setFromValue(0);
        textReveal.setToValue(1);

        fortuneMessage.setText(fortune);

        SequentialTransition masterSequence = new SequentialTransition(
                crackAnimation,
                messageSlide,
                textReveal
        );

        masterSequence.setOnFinished(e -> {
            System.out.println("FortuneController: Animation sequence finished");
            addSparkleEffect();
        });

        masterSequence.play();
    }

    @FXML
    private void initialize() {
        fortuneMessage.setOpacity(0.0);
        // Apply rounded corner clip to crackedCookieImage
        Rectangle clip = new Rectangle(550, 500);
        clip.setArcWidth(20); // 2 * 10px radius
        clip.setArcHeight(20);
        crackedCookieImage.setClip(clip);
        System.out.println("FortuneController: Initialized with ImageView clip");
    }

    public void fetchFortune(User user, double progress, double remainingHours, double totalStudyHours) {
        if (isFetchingFortune) {
            System.out.println("FortuneController: Fetch blocked, already fetching");
            return;
        }
        isFetchingFortune = true;
        System.out.println("FortuneController: Fetching fortune for user: " + user.getUsername());

        this.currentUser = user;
        this.progress = progress;
        this.remainingHours = remainingHours;
        this.totalStudyHours = totalStudyHours;

        String name = user.getPreferredName() != null && !user.getPreferredName().isEmpty() ? user.getPreferredName() : user.getUsername();
        String location = user.getLocation();
        String job = user.getJob();
        String gender = user.getGender();

        String language = null;
        String cookieType = null;
        try {
            String[] preferences = preferencesDAO.getPreferences(user.getEmail());
            if (preferences != null) {
                language = preferences[0];
                cookieType = preferences[1];
            }
        } catch (Exception e) {
            System.err.println("FortuneController: Failed to fetch user preferences: " + e.getMessage());
        }

        String learningProgress = null;
        if (remainingHours >= 0 && totalStudyHours > 0) {
            if (remainingHours < 1.0) {
                int remainingMinutes = (int) Math.round(remainingHours * 60);
                String minutesText = remainingMinutes == 1 ? "minute" : "minutes";
                learningProgress = String.format("%d %s remaining out of %.2f hours chosen", remainingMinutes, minutesText, totalStudyHours);
            } else {
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

        StringBuilder promptBuilder = new StringBuilder("Generate a single motivational sentence (no yapping)");
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

        List<String> availableTopics = new ArrayList<>();
        if (name != null) availableTopics.add("name");
        if (language != null) availableTopics.add("language");
        if (cookieType != null) availableTopics.add("fortune cookie preference");
        if (learningProgress != null) availableTopics.add("learning progress");
        if (location != null) availableTopics.add("location");
        if (job != null) availableTopics.add("job");
        if (gender != null) availableTopics.add("gender");

        String focusTopic = "academic journey";
        if (!availableTopics.isEmpty()) {
            focusTopic = availableTopics.get(random.nextInt(availableTopics.size()));
        }

        promptBuilder.append("Highly focus the sentence on their ").append(focusTopic).append(". ");
        promptBuilder.append("Randomly vary the sentence length, minimum is 7 words. ");
        promptBuilder.append("Only use one type of language in a sentence, except it's their username or information. ");
        promptBuilder.append("The output is a sentence without adding extra things like explanation or number of words. ");
        promptBuilder.append("However in situations where the language is other than english\n" +
                "should add a note about the language type behind (for example vietnamese, chinese, or japanese, ...). ");
        promptBuilder.append("Do not always include the student's name in the sentence, even if provided; use it occasionally for variety.");

        String prompt = promptBuilder.toString();

        GrokResponseFetcher fetcher = new GrokResponseFetcher("https://api.x.ai/v1/chat/completions", System.getenv("GROK_API_KEY"));
        new Thread(() -> {
            String fortune = fetcher.fetchGrokResponse(prompt);
            if (fortune == null || fortune.trim().isEmpty()) {
                fortune = homeController.getRandomFortune();
                System.out.println("FortuneController: Using random fortune: " + fortune);
            }
            String finalFortune = fortune;
            Platform.runLater(() -> {
                setFortune(finalFortune);
                isFetchingFortune = false;
                System.out.println("FortuneController: Fortune fetch completed");
            });
        }).start();
    }

    @FXML
    private void handleNewFortune() {
        if (homeController != null && !isFetchingFortune) {
            System.out.println("FortuneController: Handling new fortune request");
            fetchFortune(currentUser, progress, remainingHours, totalStudyHours);
        } else {
            System.out.println("FortuneController: New fortune blocked: homeController=" + (homeController != null) + ", isFetchingFortune=" + isFetchingFortune);
        }
    }

    @FXML
    private void handleClose() {
        // Original: Close the fortune window
        Stage stage = (Stage) fortuneMessage.getScene().getWindow();
        stage.close();
    }

    private void addSparkleEffect() {
        if (fortuneContainer != null) {
            fortuneContainer.getChildren().removeIf(node -> node instanceof Circle);

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
                sparkleAnimation.setOnFinished(e -> fortuneContainer.getChildren().remove(sparkle));

                sparkleAnimation.setCycleCount(1);
                sparkleAnimation.play();
            }
            System.out.println("FortuneController: Sparkle effect added");
        }
    }
}