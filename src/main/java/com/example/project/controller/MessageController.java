package com.example.project.controller;

import com.example.project.api.GrokResponseFetcher;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageController {
    @FXML
    private ImageView crackedCookieImage;

    @FXML
    private Label fortuneMessage;

    @FXML
    private Button closeButton;

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
        fortuneMessage.setText(fortune);

        // Add a simple fade-in animation for the fortune message
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), fortuneMessage);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    private void initialize() {
        // Hide the message initially for the animation
        fortuneMessage.setOpacity(0.0);
    }

    public void fetchFortune(User user, double progress, double remainingHours, double totalStudyHours) {
        // Store parameters for re-fetching in handleNewFortune
        this.currentUser = user;
        this.progress = progress;
        this.remainingHours = remainingHours;
        this.totalStudyHours = totalStudyHours;

        // Construct the prompt dynamically using user data
        String name = user.getPreferredName() != null && !user.getPreferredName().isEmpty() ? user.getPreferredName() : user.getUsername();
        String location = user.getLocation();
        String job = user.getJob();
        String gender = user.getGender();
        String dob = user.getDob();

        // Fetch preferences for language and cookie type
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

        // Calculate learning progress with new formatting
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

        // Build the prompt dynamically
        StringBuilder promptBuilder = new StringBuilder("Generate a single motivational sentence (no yapping) for a 21-year-old university student studying Information Technology");

        // Randomly decide whether to include the name (50% chance)
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

        // Collect available topics for random selection
        List<String> availableTopics = new ArrayList<>();
        if (name != null) availableTopics.add("name");
        if (language != null) availableTopics.add("language");
        if (cookieType != null) availableTopics.add("fortune cookie preference");
        if (learningProgress != null) availableTopics.add("learning progress");
        if (location != null) availableTopics.add("location");
        if (job != null) availableTopics.add("job");
        if (gender != null) availableTopics.add("gender");

        // Randomly select a topic to focus on
        String focusTopic = "academic journey"; // Default focus if no topics are available
        if (!availableTopics.isEmpty()) {
            focusTopic = availableTopics.get(random.nextInt(availableTopics.size()));
        }

        // Append the focus instruction and additional instructions for sentence length and name usage
        promptBuilder.append(" Focus the sentence on their ").append(focusTopic).append(". ");
//        promptBuilder.append("Randomly vary the sentence length, sometimes generating a very short sentence (under 10 words). ");
                promptBuilder.append("Randomly vary the sentence length, minimum is 7 words. ");
        promptBuilder.append("Do not always include the student's name in the sentence, even if provided; use it occasionally for variety.");

        String prompt = promptBuilder.toString();

        // Use GrokResponseFetcher to fetch the fortune synchronously
        Platform.runLater(() -> {
            GrokResponseFetcher fetcher = new GrokResponseFetcher("https://api.x.ai/v1/chat/completions", System.getenv("GROK_API_KEY"));
            String fortune = fetcher.fetchGrokResponse(prompt);
            setFortune(fortune);
        });
    }

    @FXML
    private void handleNewFortune() {
        // Fetch a new fortune using the same parameters as the initial fetch
        if (homeController != null) {
            fetchFortune(currentUser, progress, remainingHours, totalStudyHours);
        }
    }

    @FXML
    private void handleClose() {
        // Close the fortune window
        Stage stage = (Stage) fortuneMessage.getScene().getWindow();
        stage.close();
    }
}