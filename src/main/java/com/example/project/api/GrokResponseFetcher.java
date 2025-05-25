package com.example.project.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GrokResponseFetcher {

    private static final String USERAGENT = "GROK FETCHER";
    public static final Logger logger = Logger.getLogger(GrokResponseFetcher.class.getName());
    private final String apiUrl;
    private final String apiKey;
    private final Gson gson;

    public GrokResponseFetcher(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl != null ? apiUrl : "https://api.x.ai/v1/chat/completions"; // Updated endpoint
        this.apiKey = apiKey != null ? apiKey : System.getenv("GROK_API_KEY");
        this.gson = new Gson();
    }

    protected HttpURLConnection getConnection() {
        HttpURLConnection conn = null;

        try {
            URL urlObj = new URI(apiUrl).toURL();
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestProperty("User-Agent", USERAGENT);
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error getting connection", ex);
        }
        return conn;
    }

    public String fetchGrokResponse(String prompt) {
        HttpURLConnection conn = null;
        String output = null;
        OutputStream os = null;

        try {
            logger.info("Attempting POST on " + apiUrl);

            // Construct the JSON request body using Gson
            Map<String, Object> requestBodyMap = new HashMap<>();
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a motivational assistant providing concise fortunes.");
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(systemMessage);
            messages.add(userMessage);

            requestBodyMap.put("messages", messages);
            requestBodyMap.put("model", "grok-3-latest");
            requestBodyMap.put("stream", false);
            requestBodyMap.put("temperature", 0);

            String simpleJsonObj = gson.toJson(requestBodyMap);

            conn = getConnection();
            conn.setRequestMethod("POST");

            // Send JSON to server
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            os.write(simpleJsonObj.getBytes());
            os.flush();
            os.close();
            os = null;

            // Read response
            int code = conn.getResponseCode();
            logger.info("Response: " + code);

            if (code == HttpURLConnection.HTTP_OK) {
                output = readConnInput(conn);
                // Parse the JSON response (format: {"choices": [{"message": {"content": "fortune"}}]})
                JsonObject jsonResponse = gson.fromJson(output, JsonObject.class);
                if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                    JsonObject choice = jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject();
                    if (choice.has("message")) {
                        JsonObject message = choice.getAsJsonObject("message");
                        if (message.has("content")) {
                            String fortune = message.get("content").getAsString();
                            return fortune != null ? fortune : "Keep pushing forward! Your hard work will pay off.";
                        }
                    }
                }
                return "Keep pushing forward! Your hard work will pay off.";
            } else {
                logger.warning("Grok API request failed with status code: " + code);
                return "API unavailable. Please try again later.";
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error performing POST", ex);
            return "API unavailable. Please try again later.";
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {}
            }
        }
    }

    protected String readConnInput(HttpURLConnection conn) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer(10000);
        try {
            is = conn.getInputStream();
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);

            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error reading response", ex);
        } finally {
            if (is != null) try { is.close(); } catch (Exception ex) {}
            if (isr != null) try { isr.close(); } catch (Exception ex) {}
            if (br != null) try { br.close(); } catch (Exception ex) {}
        }

        return sb.toString();
    }
}