package com.example.project.util;

import com.example.project.controller.AppSettingController;
import javafx.scene.Scene;
import java.util.HashMap;
import java.util.Map;

public class StyleManager {
    private static final String LIGHT_THEME_PATH = "/com/example/project/style_sheet/";
    private static final String DARK_THEME_PATH = "/com/example/project/darkmode_stylesheet/";
    
    // Map to standardize view names to stylesheet names
    private static final Map<String, String> VIEW_STYLESHEETS = new HashMap<>();

    static {
        // Initialize mapping of view names to stylesheet names
        //VIEW_STYLESHEETS.put("main", "main-stylesheet");
        VIEW_STYLESHEETS.put("home", "home");
        //VIEW_STYLESHEETS.put("signin", "signin-stylesheet");
        //VIEW_STYLESHEETS.put("signup", "signup-stylesheet");
        VIEW_STYLESHEETS.put("userdisplay", "userDisplay");
        VIEW_STYLESHEETS.put("appsetting", "appSetting");
        //VIEW_STYLESHEETS.put("passwordsetup", "passwordSetting-stylesheet");
//        VIEW_STYLESHEETS.put("usersetting", "userSetting-stylesheet");
//        VIEW_STYLESHEETS.put("fortune", "fortune-stylesheet");
//        VIEW_STYLESHEETS.put("message", "message-stylesheet");
    }

    public static void applyTheme(Scene scene, String viewName) {
        if (scene == null) return;

        String stylesheetName = VIEW_STYLESHEETS.getOrDefault(viewName.toLowerCase(), viewName);
        AppSettingController.Theme currentTheme = AppSettingController.getCurrentTheme();
        String themeSuffix = currentTheme == AppSettingController.Theme.DARK ? "-dark" : "";
        String stylesheet = (currentTheme == AppSettingController.Theme.DARK ?
                DARK_THEME_PATH : LIGHT_THEME_PATH) + stylesheetName + themeSuffix + ".css";

        scene.getStylesheets().clear();
        try {
            String resourcePath = StyleManager.class.getResource(stylesheet).toExternalForm();
            scene.getStylesheets().add(resourcePath);
        } catch (Exception e) {
            // Fallback to light theme if dark theme doesn't exist
            String lightStylesheet = LIGHT_THEME_PATH + stylesheetName + ".css";
            try {
                String lightPath = StyleManager.class.getResource(lightStylesheet).toExternalForm();
                scene.getStylesheets().add(lightPath);
            } catch (Exception ex) {
                System.err.println("Failed to load stylesheet for view: " + viewName);
                ex.printStackTrace();
            }
        }

        scene.getRoot().applyCss();
    }

    public static void applyThemeToAll(Scene scene) {
        if (scene == null) return;

        // Try to determine the view name from the scene's root
        String fxmlName = scene.getRoot().getTypeSelector().toLowerCase();
        String viewName = fxmlName.replace("-view", "");

        applyTheme(scene, viewName);
    }

    public static void updateAllOpenWindows() {
        // This method can be called when theme changes to update all open windows
        // You'll need to maintain a list of open windows/scenes
    }
}