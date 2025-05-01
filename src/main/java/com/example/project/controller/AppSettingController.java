package com.example.project.controller;

import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import com.example.project.util.ThemeManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class AppSettingController {

    @FXML
    private Label titleLabel;

    @FXML
    private RadioButton lightButton;

    @FXML
    private RadioButton darkButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Button signOutButton;

    @FXML
    private CheckBox runOnStartupCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    private Stage settingsStage;
    private Scene homeScene;
    private HomeController homeController;
    private User currentUser;
    private final UserPreferencesDAO preferencesDAO;

    public AppSettingController() {
        preferencesDAO = new UserPreferencesDAO();
    }

    public void setStage(Stage stage) {
        this.settingsStage = stage;
    }

    public void setScene(Scene homeScene, HomeController homeController) {
        this.homeScene = homeScene;
        this.homeController = homeController;
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateUser(user);
    }

    private void updateUser(User user) {
        if (user != null && user.getEmail() != null) {
            emailLabel.setText("Logged in as " + user.getEmail());
        } else {
            emailLabel.setText("Logged in as Guest");
        }
    }

    @FXML
    private void initialize() {
        // Bind radio buttons to a ToggleGroup programmatically
        lightButton.setToggleGroup(new javafx.scene.control.ToggleGroup());
        darkButton.setToggleGroup(lightButton.getToggleGroup());

        // Set default theme (e.g., Light)
        lightButton.setSelected(true);

        // Load existing preferences if available
        if (currentUser != null && currentUser.getEmail() != null) {
            try {
                String[] preferences = preferencesDAO.getPreferences(currentUser.getEmail());
                if (preferences != null) {
                    String theme = preferences[0]; // Assuming theme is stored in preferences[0]
                    boolean runOnStartup = Boolean.parseBoolean(preferences[1]); // Assuming runOnStartup is stored in preferences[1]
                    lightButton.setSelected("Light".equals(theme));
                    darkButton.setSelected("Dark".equals(theme));
                }
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to load preferences: " + e.getMessage());
            }
        }

        // Set up event handlers
        saveButton.setOnAction(this::handleSaveButton);
        backButton.setOnAction(this::handleBackButton);
        signOutButton.setOnAction(this::handleSignOutButton);
        runOnStartupCheckBox.setOnAction(this::handleRunOnStartup);

        // Add visual feedback for interactivity
        saveButton.setStyle("-fx-cursor: hand;");
        backButton.setStyle("-fx-cursor: hand;");
        signOutButton.setStyle("-fx-cursor: hand;");
        lightButton.setStyle("-fx-cursor: hand;");
        darkButton.setStyle("-fx-cursor: hand;");
        runOnStartupCheckBox.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (currentUser == null || currentUser.getEmail() == null) {
            ErrorAlert.show("Error", "No user data available to save settings.");
            return;
        }

        try {
            // Save theme preference
            String selectedTheme = lightButton.isSelected() ? "Light" : "Dark";
            preferencesDAO.saveTheme(currentUser.getEmail(), selectedTheme);

            // Save run on startup preference
            boolean runOnStartup = runOnStartupCheckBox.isSelected();
            preferencesDAO.saveRunOnStartup(currentUser.getEmail(), runOnStartup);

            ErrorAlert.show("Success", "Settings saved successfully.");
            settingsStage.close(); // Close the settings dialog
        } catch (Exception e) {
            ErrorAlert.show("Database Error", "Failed to save settings: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        settingsStage.close(); // Close the settings dialog
    }

    @FXML
    private void handleSignOutButton(ActionEvent event) {
        try {
            // Load sign-in screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/signin-view.fxml"));
            Parent signInRoot = loader.load();

            // Get the sign-in controller
            SignInController signInController = loader.getController();

            // Set up the new scene and stage
            Stage signInStage = new Stage();
            Scene signInScene = new Scene(signInRoot);
            signInStage.setScene(signInScene);
            signInStage.setTitle("Sign In");

            // Pass the stage to the sign-in controller
            signInController.setSignInStage(signInStage);

            // Close the settings stage and home stage
            settingsStage.close();
            if (homeController != null && homeScene != null) {
                Stage homeStage = (Stage) homeScene.getWindow();
                homeStage.close();
            }

            signInStage.show();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load sign-in screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleRunOnStartup(ActionEvent event) {
        boolean runOnStartup = runOnStartupCheckBox.isSelected();
        try {
            configureRunOnStartup(runOnStartup);
            // Optionally, save the preference immediately (or rely on handleSaveButton)
            if (currentUser != null && currentUser.getEmail() != null) {
                preferencesDAO.saveRunOnStartup(currentUser.getEmail(), runOnStartup);
            }
        } catch (Exception e) {
            ErrorAlert.show("Error", "Failed to configure run on startup: " + e.getMessage());
        }
    }

    private void configureRunOnStartup(boolean enable) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        String appPath = new File(System.getProperty("java.class.path")).getAbsolutePath();

        if (os.contains("win")) {
            // Windows: Modify registry to add/remove startup entry
            String regKey = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
            String appName = "MyJavaFXApp";

            if (enable) {
                String command = String.format("\"%s\" \"%s\"", System.getProperty("java.home") + "\\bin\\javaw.exe", appPath);
                Runtime.getRuntime().exec("reg add " + regKey + " /v " + appName + " /t REG_SZ /d " + command + " /f");
            } else {
                Runtime.getRuntime().exec("reg delete " + regKey + " /v " + appName + " /f");
            }
        } else if (os.contains("mac")) {
            // macOS: Create/remove a Launch Agent plist file
            String plistPath = System.getProperty("user.home") + "/Library/LaunchAgents/com.example.myapp.plist";
            if (enable) {
                String plistContent = String.format(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                                "<plist version=\"1.0\">\n" +
                                "<dict>\n" +
                                "    <key>Label</key>\n" +
                                "    <string>com.example.myapp</string>\n" +
                                "    <key>ProgramArguments</key>\n" +
                                "    <array>\n" +
                                "        <string>%s/bin/java</string>\n" +
                                "        <string>-jar</string>\n" +
                                "        <string>%s</string>\n" +
                                "    </array>\n" +
                                "    <key>RunAtLoad</key>\n" +
                                "    <true/>\n" +
                                "</dict>\n" +
                                "</plist>",
                        System.getProperty("java.home"), appPath
                );
                Files.write(Paths.get(plistPath), plistContent.getBytes());
            } else {
                Files.deleteIfExists(Paths.get(plistPath));
            }
        } else if (os.contains("linux")) {
            // Linux: Create/remove a .desktop file in ~/.config/autostart
            String desktopPath = System.getProperty("user.home") + "/.config/autostart/myapp.desktop";
            if (enable) {
                String desktopContent = String.format(
                        "[Desktop Entry]\n" +
                                "Type=Application\n" +
                                "Name=MyJavaFXApp\n" +
                                "Exec=%s/bin/java -jar %s\n" +
                                "Hidden=false\n" +
                                "NoDisplay=false\n" +
                                "X-GNOME-Autostart-enabled=true\n",
                        System.getProperty("java.home"), appPath
                );
                Files.write(Paths.get(desktopPath), desktopContent.getBytes());
            } else {
                Files.deleteIfExists(Paths.get(desktopPath));
            }
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
    }
}