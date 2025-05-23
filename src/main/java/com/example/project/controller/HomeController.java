package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import com.example.project.util.StyleManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.animation.Interpolator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {

    @FXML
    private Label welcomeTitle;

    @FXML
    private ImageView fortuneCookieImage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressText;

    @FXML
    private ImageView userDisplayButton;

    @FXML
    private ImageView appSettingButton;

    @FXML
    private ChoiceBox<String> hourChoiceBox;

    @FXML
    private ImageView playModeButton;

    private User currentUser;
    private Parent root;
    private Stage homeStage;
    private SignInController signInController;

    private Timeline studyTimer;
    private double currentSectionLearnedHours;
    private boolean isStudyActive;
    private double totalStudyHours;
    private double elapsedStudyHours;

    // Hour options: 30-minute steps from 0.5 to 10 hours
    private final List<Double> hourOptions;

    public HomeController() {
        // Initialize hour options (0.5, 1, 1.5, ..., 10)
        hourOptions = new ArrayList<>();
        for (double hours = 0.5; hours <= 10.0; hours += 0.5) {
            hourOptions.add(hours);
        }
    }

    public void setHomeStage(Stage stage, SignInController signInController) {
        this.homeStage = stage;
        this.signInController = signInController;
        resetProgress();
        applyTheme();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        resetProgress();
    }

    private void resetProgress() {
        currentSectionLearnedHours = 0.0;
        elapsedStudyHours = 0.0;
        totalStudyHours = 0.0;
        progressBar.setProgress(0.0);
        isStudyActive = false;
        updateHourChoiceBoxOptions();
    }

    public void applyTheme() {
        if (homeStage != null && homeStage.getScene() != null) {
            StyleManager.applyTheme(homeStage.getScene(), "home");
        }
    }

    @FXML
    private void initialize() {
        fortuneCookieImage.setOnMouseClicked(this::openFortuneScreen);
        userDisplayButton.setOnMouseClicked(this::handleProfileButton);
        appSettingButton.setOnMouseClicked(this::handleSettingsButton);
        playModeButton.setOnMouseClicked(this::handlePlayModeButton);

        userDisplayButton.setStyle("-fx-cursor: hand;");
        fortuneCookieImage.setStyle("-fx-cursor: hand;");
        appSettingButton.setStyle("-fx-cursor: hand;");
        playModeButton.setStyle("-fx-cursor: hand;");

        fortuneCookieImage.getStyleClass().add("fortune-image-container");
        userDisplayButton.getStyleClass().add("nav-button");
        appSettingButton.getStyleClass().add("nav-button");
        playModeButton.getStyleClass().add("nav-button");

        // Initialize hour choice box
        updateHourChoiceBoxOptions();
        hourChoiceBox.setValue(formatHours(1.0)); // Default to 1 hour

        // Initialize progress bar
        progressBar.setProgress(0.0);
        isStudyActive = false;
        currentSectionLearnedHours = 0.0;

        // Create and style tooltips for user and settings buttons with custom font and background
        String toolTipStyle = "-fx-background-color: #FFFFE0;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-font-family: 'Arial';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: black;";

        Tooltip forUserDisplay = new Tooltip("User");
        Tooltip forAppSetting = new Tooltip("Setting");
        Tooltip forProgressBar = new Tooltip("Your learning progress");
        Tooltip forCookieImg = new Tooltip("Let's open your cookie!");
        Tooltip forPlaymodeBtn = new Tooltip("Run/Pause");

        forUserDisplay.setStyle(toolTipStyle);
        forAppSetting.setStyle(toolTipStyle);
        forProgressBar.setStyle(toolTipStyle);
        forCookieImg.setStyle(toolTipStyle);
        forPlaymodeBtn.setStyle(toolTipStyle);

        Tooltip.install(userDisplayButton, forUserDisplay);
        Tooltip.install(appSettingButton, forAppSetting);
        Tooltip.install(progressBar, forProgressBar);
        Tooltip.install(fortuneCookieImage, forCookieImg);
        Tooltip.install(playModeButton, forPlaymodeBtn);
    }

    private void updateHourChoiceBoxOptions() {
        String previousSelection = hourChoiceBox.getValue(); // Store current selection
        hourChoiceBox.getItems().clear();
        // Filter options to include only those >= totalStudyHours or all if no progress
        List<String> availableOptions = hourOptions.stream()
                .filter(hours -> totalStudyHours == 0.0 || hours >= totalStudyHours)
                .map(this::formatHours)
                .collect(Collectors.toList());
        hourChoiceBox.getItems().addAll(availableOptions);

        // Restore previous selection if still valid, otherwise select totalStudyHours
        if (previousSelection != null && availableOptions.contains(previousSelection)) {
            hourChoiceBox.setValue(previousSelection);
        } else if (totalStudyHours > 0.0) {
            hourChoiceBox.setValue(formatHours(totalStudyHours));
        }
    }

    private String formatHours(double hours) {
        int wholeHours = (int) hours;
        int minutes = (int) ((hours - wholeHours) * 60);
        if (wholeHours == 0 && minutes > 0) {
            return String.format("%d minute%s", minutes, minutes == 1 ? "" : "s");
        } else if (minutes == 0) {
            return String.format("%d hour%s", wholeHours, wholeHours == 1 ? "" : "s");
        } else {
            return String.format("%d hour%s %d minute%s", wholeHours, wholeHours == 1 ? "" : "s",
                    minutes, minutes == 1 ? "" : "s");
        }
    }

    private double parseHours(String formattedHours) {
        // Parse formatted string back to double (e.g., "1 hour 30 minutes" -> 1.5)
        String[] parts = formattedHours.split(" ");
        double hours = 0.0;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches("\\d+")) {
                double value = Double.parseDouble(parts[i]);
                if (i + 1 < parts.length && parts[i + 1].startsWith("hour")) {
                    hours += value;
                } else if (i + 1 < parts.length && parts[i + 1].startsWith("minute")) {
                    hours += value / 60.0;
                }
            }
        }
        return hours;
    }

private void loadFortuneScreen() {
    try {
        if (isStudyActive) {
            currentSectionLearnedHours = elapsedStudyHours;
            double remainingHours = totalStudyHours - elapsedStudyHours;
            System.out.println("Remaining study time: " + String.format("%.2f", remainingHours) + " hours");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/fortune-screen.fxml"));
        Parent fortuneRoot = loader.load();

        MessageController fortuneController = loader.getController();
        fortuneController.setHomeController(this);
        fortuneController.setFortune(getRandomFortune());

        Scene fortuneScene = new Scene(fortuneRoot);
        Stage fortuneStage = new Stage();
        fortuneStage.initOwner(homeStage);
        fortuneStage.initModality(Modality.WINDOW_MODAL);
        fortuneStage.setTitle("Your Fortune");
        fortuneStage.setScene(fortuneScene);
        fortuneStage.setResizable(false);
        fortuneStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/digitalCookieMainIcon2.png")));

        StyleManager.applyTheme(fortuneScene, "fortune");

        // Use show() instead of showAndWait()
        fortuneStage.show();
    } catch (IOException e) {
        e.printStackTrace();
        ErrorAlert.show("Navigation Error", "Failed to load fortune screen: " + e.getMessage());
    }
}

private void openFortuneScreen(Event event) {
    try {
        ParallelTransition shakeEffect = createShakeAnimation(fortuneCookieImage);
        
        // Initial pause for anticipation
        PauseTransition initialPause = new PauseTransition(Duration.millis(100));
        
        // Hover effect (moving slightly up)
        TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), fortuneCookieImage);
        moveUp.setByY(-10);
        
        // Scale effect with slight bounce
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), fortuneCookieImage);
        scaleUp.setToX(1.15);
        scaleUp.setToY(1.15);
        
        ScaleTransition scaleBounce = new ScaleTransition(Duration.millis(100), fortuneCookieImage);
        scaleBounce.setToX(1.1);
        scaleBounce.setToY(1.1);
        
        // Rotation with easing
        RotateTransition rotateForward = new RotateTransition(Duration.millis(300), fortuneCookieImage);
        rotateForward.setByAngle(15);
        rotateForward.setInterpolator(Interpolator.EASE_BOTH);
        
        // Return animations
        TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), fortuneCookieImage);
        moveDown.setByY(10);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), fortuneCookieImage);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        RotateTransition rotateBack = new RotateTransition(Duration.millis(200), fortuneCookieImage);
        rotateBack.setByAngle(-15);
        rotateBack.setInterpolator(Interpolator.EASE_OUT);
        
        // Combine all animations in sequence
        SequentialTransition sequence = new SequentialTransition(
            initialPause,
            new ParallelTransition(moveUp, scaleUp),
            scaleBounce,
            shakeEffect,
            rotateForward,
            new ParallelTransition(moveDown, scaleDown, rotateBack)
        );
        
        sequence.setOnFinished(e -> {
            try {
                // Add a small flash effect before opening fortune screen
                FadeTransition flash = new FadeTransition(Duration.millis(100), fortuneCookieImage);
                flash.setFromValue(1.0);
                flash.setToValue(0.8);
                flash.setCycleCount(2);
                flash.setAutoReverse(true);
                flash.setOnFinished(f -> {
                    javafx.application.Platform.runLater(this::loadFortuneScreen);
                });
                flash.play();
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorAlert.show("Error", "Failed to load fortune screen: " + ex.getMessage());
            }
        });
        
        sequence.play();
    } catch (Exception e) {
        e.printStackTrace();
        ErrorAlert.show("Animation Error", "Failed to animate fortune cookie: " + e.getMessage());
    }
}

private ParallelTransition createShakeAnimation(ImageView node) {
    // Create subtle shake effect
    Timeline shakeTimeline = new Timeline(
        new KeyFrame(Duration.ZERO, new KeyValue(node.rotateProperty(), 0)),
        new KeyFrame(Duration.millis(50), new KeyValue(node.rotateProperty(), 3)),
        new KeyFrame(Duration.millis(100), new KeyValue(node.rotateProperty(), -3)),
        new KeyFrame(Duration.millis(150), new KeyValue(node.rotateProperty(), 2)),
        new KeyFrame(Duration.millis(200), new KeyValue(node.rotateProperty(), -2)),
        new KeyFrame(Duration.millis(250), new KeyValue(node.rotateProperty(), 1)),
        new KeyFrame(Duration.millis(300), new KeyValue(node.rotateProperty(), -1)),
        new KeyFrame(Duration.millis(350), new KeyValue(node.rotateProperty(), 0))
    );
    
    // Add subtle scaling during shake
    ScaleTransition scaleShake = new ScaleTransition(Duration.millis(350), node);
    scaleShake.setFromX(1.1);
    scaleShake.setFromY(1.1);
    scaleShake.setToX(1.1);
    scaleShake.setToY(1.1);
    
    return new ParallelTransition(node, shakeTimeline, scaleShake);
}

    private SequentialTransition getSequentialTransition() {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), fortuneCookieImage);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), fortuneCookieImage);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        RotateTransition rotate = new RotateTransition(Duration.millis(200), fortuneCookieImage);
        rotate.setByAngle(10);

        SequentialTransition sequence = new SequentialTransition(scaleUp, rotate, scaleDown);
        return sequence;
    }

    public String getRandomFortune() {
        // Return a random congratulatory quote if progress is 100%
        if (progressBar.getProgress() >= 1.0) {
            String[] congrats = {
                    "Congratulations! 'Success is not the absence of obstacles, but the courage to push through them.'",
                    "Well done! 'The only limit to our realization of tomorrow is our doubts of today.'",
                    "Fantastic work! 'Success is the sum of small efforts, repeated day in and day out.'",
                    "You did it! 'The future belongs to those who believe in the beauty of their dreams.'",
                    "Amazing effort! 'Perseverance turns dreams into achievements.'"
            };
            return congrats[(int) (Math.random() * congrats.length)];
        }

        String[] fortunes = {
                "Good things come to those who wait... but better things come to those who work for it.",
                "Your creativity will lead you to success.",
                "New opportunities await you this week.",
                "A smile is your passport into the hearts of others.",
                "Your hard work is about to pay off. Remember, dreams don't work unless you do.",
                "Adventure can be real happiness.",
                "The greatest risk is not taking one.",
                "Today it's up to you to create the peacefulness you long for.",
                "Your ability to accomplish tasks will follow with success.",
                "You will be rewarded for your patience and persistence."
        };
        return fortunes[(int) (Math.random() * fortunes.length)];
    }

    @FXML
    private void handleSettingsButton(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/appsetting-view.fxml"));
            Parent settingsRoot = loader.load();

            Stage settingsStage = new Stage();
            settingsStage.initOwner(homeStage);
            settingsStage.setTitle("Settings");
            Scene settingsScene = new Scene(settingsRoot);
            settingsStage.setScene(settingsScene);
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.setResizable(false);
            settingsStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/createIcon1.png")));

            // Apply theme to settings screen
            StyleManager.applyTheme(settingsScene, "appsetting");

            AppSettingController settingController = loader.getController();
            settingController.setStage(settingsStage);
            settingController.setScene(homeStage.getScene(), this);
            settingController.setUser(currentUser);

            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorAlert.show("Navigation Error", "Failed to load settings screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleProfileButton(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/userdisplay-view.fxml"));
            Parent userDisplayRoot = loader.load();

            Stage userDisplayStage = new Stage();
            userDisplayStage.initOwner(homeStage);
            userDisplayStage.setTitle("Profile");
            Scene userDisplayScene = new Scene(userDisplayRoot);
            userDisplayStage.setScene(userDisplayScene);
            userDisplayStage.initModality(Modality.WINDOW_MODAL);
            userDisplayStage.setResizable(false);
            userDisplayStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/userIcon1.png")));

            // Apply theme to the user display screen
            StyleManager.applyTheme(userDisplayScene, "userdisplay");

            UserDisplayController userDisplayController = loader.getController();
            userDisplayController.setStage(userDisplayStage);
            userDisplayController.setScene(homeStage.getScene(), this);
            userDisplayController.setUser(currentUser);

            userDisplayStage.showAndWait(); // Show modal dialog, home remains open
        } catch (IOException e) {
            e.printStackTrace();
            ErrorAlert.show("Navigation Error", "Failed to load profile screen: " + e.getMessage());
        }
    }

    @FXML
    private void handlePlayModeButton(Event event) {
        String selectedHours = hourChoiceBox.getValue();
        if (selectedHours == null) {
            ErrorAlert.show("Input Error", "Please select a study duration.");
            return;
        }

        if (!isStudyActive) {
            // Start study session
            try {
                totalStudyHours = parseHours(selectedHours);
                elapsedStudyHours = currentSectionLearnedHours; // Preserve learned hours
                hourChoiceBox.setDisable(true);

                // Switch to pause icon
                playModeButton.setImage(new Image(getClass().getResourceAsStream("/com/example/project/symbol/pauseIcon.png")));

                // Start timer
                studyTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    elapsedStudyHours += 1.0 / 3600; // Increment by 1 second in hours
                    double progress = elapsedStudyHours / totalStudyHours;
                    progressBar.setProgress(Math.min(progress, 1.0));

                    if (progress >= 1.0) {
                        stopStudySession();
                        openFortuneScreen(null); // Auto-trigger fortune screen
                    }
                }));
                studyTimer.setCycleCount(Timeline.INDEFINITE);
                studyTimer.play();
                isStudyActive = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ErrorAlert.show("Input Error", "Invalid study duration format: " + selectedHours);
            }
        } else {
            // Pause study session
            stopStudySession();
            updateHourChoiceBoxOptions();
        }
    }

    private void stopStudySession() {
        if (studyTimer != null) {
            studyTimer.stop();
        }
        isStudyActive = false;
        hourChoiceBox.setDisable(false);
        // Switch back to play icon
        playModeButton.setImage(new Image(getClass().getResourceAsStream("/com/example/project/symbol/playIcon.png")));
    }
}