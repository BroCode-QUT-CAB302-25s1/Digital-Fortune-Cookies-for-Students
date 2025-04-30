package com.example.project.util;

import java.util.HashMap;
import java.util.Map;

public class ThemeManager {
    // Theme enum
    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    // Colour mappings
    private static final Map<String, String> LIGHT_THEME = new HashMap<>();
    private static final Map<String, String> DARK_THEME = new HashMap<>();

    static {
        // Light theme colours - Replace if needed
        LIGHT_THEME.put("background", "#FFFAF0");
        LIGHT_THEME.put("primary", "#4285F4");
        LIGHT_THEME.put("secondary", "#34A853");
        LIGHT_THEME.put("textPrimary", "#202124");
        LIGHT_THEME.put("textSecondary", "#5F6368");
        LIGHT_THEME.put("border", "#DADCE0");

        // Dark theme colours - Replace if needed
        DARK_THEME.put("background", "#202124");
        DARK_THEME.put("primary", "#8AB4F8");
        DARK_THEME.put("secondary", "#81C995");
        DARK_THEME.put("textPrimary", "#E8EAED");
        DARK_THEME.put("textSecondary", "#9AA0A6");
        DARK_THEME.put("border", "#5F6368");
    }

    public static String getColour(String colourName) {
        return currentTheme == Theme.LIGHT ?
                LIGHT_THEME.get(colourName) :
                DARK_THEME.get(colourName);
    }

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }
}
