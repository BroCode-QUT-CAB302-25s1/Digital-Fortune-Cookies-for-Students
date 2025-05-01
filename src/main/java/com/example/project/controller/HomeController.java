package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {

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

        // Initialize hour choice box
        updateHourChoiceBoxOptions();
        hourChoiceBox.setValue(formatHours(1.0)); // Default to 1 hours

        // Initialize progress bar
        progressBar.setProgress(0.0);
        isStudyActive = false;
        currentSectionLearnedHours = 0.0;

        // Create and style tooltips for user and settings buttons with custom font and background
        Tooltip userDisplay = new Tooltip("User");
        userDisplay.setStyle(
                "-fx-background-color: #FFFFE0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;"
        );
        Tooltip.install(userDisplayButton, userDisplay);

        Tooltip appSetting = new Tooltip("Setting");
        appSetting.setStyle(
                "-fx-background-color: #FFFFE0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;"
        );
        Tooltip.install(appSettingButton, appSetting);

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

    private void openFortuneScreen(Event event) {
        try {
            if (isStudyActive) {
                currentSectionLearnedHours = elapsedStudyHours;
                double remainingHours = totalStudyHours - elapsedStudyHours;
                System.out.println("Remaining study time: " + String.format("%.2f", remainingHours) + " hours");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/fortune-screen.fxml"));
            Parent fortuneRoot = loader.load();

            MessageController fortuneController = loader.getController();
            fortuneController.setFortune(getRandomFortune());

            Scene fortuneScene = new Scene(fortuneRoot);
            Stage fortuneStage = new Stage();
            fortuneStage.initOwner(homeStage); // Set homeStage as owner
            fortuneStage.initModality(Modality.WINDOW_MODAL); // Modal dialog
            fortuneStage.setTitle("Your Fortune");
            fortuneStage.setScene(fortuneScene);
            fortuneStage.setResizable(false); // Keep non-resizable as in original
            fortuneStage.showAndWait(); // Show modal dialog
        } catch (IOException e) {
            e.printStackTrace();
            ErrorAlert.show("Navigation Error", "Failed to load fortune screen: " + e.getMessage());
        }
    }

    public String getRandomFortune() {
        // Return random congratulatory quote if progress is 100%
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
    private void handleHomeButton(ActionEvent event) {
        // Implement later
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