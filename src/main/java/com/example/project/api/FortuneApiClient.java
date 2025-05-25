package com.example.project.api;

import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FortuneApiClient {

    private final HttpClient httpClient;
    private final String apiUrl;
    private final String apiKey;
    private final UserPreferencesDAO preferencesDAO;
    private final Gson gson;

    // Constructor with configurable API URL and API key
    public FortuneApiClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl != null ? apiUrl : "https://api.x.ai/grok"; // Default URL
        this.apiKey = apiKey != null ? apiKey : System.getenv("GROK_API_KEY"); // Fallback to environment variable
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.preferencesDAO = new UserPreferencesDAO();
        this.gson = new Gson();
    }

    // Default constructor for backward compatibility
    public FortuneApiClient() {
        this(null, null);
    }

    public String fetchFortune(User user, double progress, double remainingHours, double totalStudyHours) {
        // Construct the prompt dynamically using user data
        String name = user.getUsername();
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

        // Calculate learning progress
        String learningProgress = (remainingHours >= 0 && totalStudyHours > 0) ?
                String.format("%.2f hours remaining out of %.2f hours chosen", remainingHours, totalStudyHours) :
                null;

        // Build the prompt dynamically
        StringBuilder promptBuilder = new StringBuilder("Generate a single motivational sentence (no yapping) for a university student studying Information Technology");
        if (name != null) {
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
        if (dob != null) {
            promptBuilder.append(" born on ").append(dob);
        }
        promptBuilder.append(", balancing academics and personal growth with potential for fatigue or burnout.");

        String prompt = promptBuilder.toString();

        try {
            // Prepare the JSON request body using Gson
            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("prompt", prompt);
            String requestBody = gson.toJson(requestBodyMap);

            // Build the HTTP request with API key authentication
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the HTTP status code
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Grok API request failed with status code: " + statusCode);
            }

            // Parse the JSON response using Gson (assuming the format is {"message": "motivational sentence"})
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            String fortune = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Keep pushing forward, " + (name != null ? name : "Student") + "!";
            return fortune;
        } catch (Exception e) {
            System.err.println("Failed to fetch fortune from Grok API: " + e.getMessage());
            // Fallback to a default fortune if API call fails
            return progress >= 1.0 ?
                    String.format("Congratulations, %s! Your hard work has paid off!", name != null ? name : "Student") :
                    String.format("%s, keep up the great effort!", name != null ? name : "Student");
        }
    }
}