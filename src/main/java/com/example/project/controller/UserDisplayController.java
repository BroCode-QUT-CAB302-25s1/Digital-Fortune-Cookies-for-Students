package com.example.project.controller;

import com.example.project.dao.ProfileImageDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class UserDisplayController {
    @FXML
    private ImageView profileImage;

    @FXML
    private Pane profileCard;

    @FXML
    private Label statusOnline;

    @FXML
    private Label profileName;

    @FXML
    private Label githubLink;

    @FXML
    private ImageView githubIcon;

    @FXML
    private Label emailLink;

    @FXML
    private ImageView emailIcon;

    @FXML
    private Label phoneNumberLink;

    @FXML
    private ImageView phoneIcon;

    @FXML
    private Label currentJob;

    @FXML
    private ImageView jobIcon;

    @FXML
    private Label currentLocation;

    @FXML
    private ImageView locationIcon;

    @FXML
    private Label footerLabel;

    @FXML
    private ImageView statusDotIcon;

    @FXML
    private Pane infoPanel;

    @FXML
    private Label userName;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label preferredName;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Label userEmail;

    @FXML
    private Label userGender;

    @FXML
    private Label userPassword;

    @FXML
    private ImageView passwordVisibilityButton;

    @FXML
    private Label languagesLabel;

    @FXML
    private Label cookiesTypeLabel;

    @FXML
    private Label titleHeader;

    @FXML
    private Button backButton;

    @FXML
    private Button editButton;

    @FXML
    private Button changePasswordButton;

    private Parent root;
    private Stage userDisplayStage;
    private Scene homeScene;
    private HomeController homeController;
    private User currentUser;
    private final SqliteUserDAO userDAO;
    private final UserPreferencesDAO preferencesDAO;
    private final ProfileImageDAO profileImageDAO;
    private boolean isPasswordVisible = false;

    private static final String DEFAULT_PROFILE_IMAGE = "/com/example/project/symbol/digitalCookieMainIcon1.png";
    private static final String VISIBILITY_ICON_ON = "/com/example/project/symbol/visibilityModeIcon2.png";
    private static final String VISIBILITY_ICON_OFF = "/com/example/project/symbol/visibilityModeIcon1.png";
    private static final String MASKED_PASSWORD = "●●●●●●●●●●●";

    public UserDisplayController() {
        userDAO = new SqliteUserDAO();
        preferencesDAO = new UserPreferencesDAO();
        profileImageDAO = new ProfileImageDAO();
    }

    public void setStage(Stage stage) {
        this.userDisplayStage = stage;
    }

    public void setScene(Scene homeScene, HomeController homeController) {
        this.homeScene = homeScene;
        this.homeController = homeController;

        // Clip the image into a circle
        Circle clip = new Circle(75, 75, 75); // x, y, radius
        profileImage.setClip(clip);
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateUser(user);
    }

    public void updateUser(User user) {
        this.currentUser = user;
        if (user != null && user.getEmail() != null) {
            try {
                // Fetch user from database
                User dbUser = userDAO.fetchUserByEmail(user.getEmail());
                if (dbUser != null) {
                    this.currentUser = dbUser;
                    // Fetch preferences
                    String[] preferences = preferencesDAO.getPreferences(user.getEmail());
                    String languages = preferences != null ? preferences[0] : "";
                    String cookiesType = preferences != null ? preferences[1] : "";
                    // Fetch profile image
                    String profileImageUrl = profileImageDAO.getProfileImage(user.getEmail());

                    // Update UI with user data
                    profileName.setText(dbUser.getPreferredName() != null ? dbUser.getPreferredName() : dbUser.getUsername());
                    githubLink.setText(dbUser.getGithub() != null ? dbUser.getGithub() : "");
                    emailLink.setText(dbUser.getEmail() != null ? dbUser.getEmail() : "");
                    phoneNumberLink.setText(dbUser.getPhone() != null ? dbUser.getPhone() : "");
                    currentJob.setText(dbUser.getJob() != null ? dbUser.getJob() : "");
                    currentLocation.setText(dbUser.getLocation() != null ? dbUser.getLocation() : "");
                    footerLabel.setText("BroCode © 2025");
                    userName.setText(dbUser.getUsername() != null ? dbUser.getUsername() : "");
                    firstName.setText(dbUser.getFirstName() != null ? dbUser.getFirstName() : "");
                    lastName.setText(dbUser.getLastName() != null ? dbUser.getLastName() : "");
                    preferredName.setText(dbUser.getPreferredName() != null ? dbUser.getPreferredName() : "");
                    dateOfBirth.setText(dbUser.getDob() != null ? dbUser.getDob() : "");
                    userEmail.setText(dbUser.getEmail() != null ? dbUser.getEmail() : "");
                    userGender.setText(dbUser.getGender() != null ? dbUser.getGender() : "");
                    userPassword.setText(MASKED_PASSWORD);
                    languagesLabel.setText(languages);
                    cookiesTypeLabel.setText(cookiesType);
                    statusOnline.setText("Online");
                    titleHeader.setText("My Profile");

                    // Update profile image
                    if (profileImageUrl != null && !profileImageUrl.isEmpty() && profileImageUrl != DEFAULT_PROFILE_IMAGE) {
                        try {
                            if (profileImageUrl.compareTo("/com/example/project/symbol/BroCode.png") == 0) {
                                profileImage.setImage(new Image(getClass().getResourceAsStream(profileImageUrl)));
                            } else {
                                profileImage.setImage(new Image(profileImageUrl, true));
                            }
                        } catch (Exception e) {
                            profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE)));
                        }
                    } else {
                        System.out.println("No profile image URL found for user: " + user.getEmail());
                        profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE)));
                    }

                    // Initialize password visibility button
                    passwordVisibilityButton.setImage(new Image(getClass().getResourceAsStream(VISIBILITY_ICON_OFF)));
                    isPasswordVisible = false;
                } else {
                    ErrorAlert.show("Database Error", "No user found with email: " + user.getEmail());
                }
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to fetch user data: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            ErrorAlert.show("Display Error", "No user data or email provided.");
        }
    }

    @FXML
    private void initialize() {
        if (currentUser != null) {
            updateUser(currentUser);
        }
        backButton.setOnAction(this::handleBackButton);
        editButton.setOnAction(this::handleEditButton);
        changePasswordButton.setOnAction(this::handleChangePasswordButton);

        // Add click handler for password visibility button
        passwordVisibilityButton.setOnMouseClicked(this::togglePasswordVisibility);

        // Define tooltip style
        String toolTipStyle = "-fx-background-color: #FFFFE0;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-font-family: 'Arial';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: black;";

        // Add tooltips for all ImageView elements
        Tooltip forProfileImage = new Tooltip("Profile Image");
        forProfileImage.setStyle(toolTipStyle);
        Tooltip.install(profileImage, forProfileImage);

        Tooltip forPasswordVisibility = new Tooltip("Hide/UnHide Password");
        forPasswordVisibility.setStyle(toolTipStyle);
        Tooltip.install(passwordVisibilityButton, forPasswordVisibility);

        Tooltip forStatusDot = new Tooltip("Online Status");
        forStatusDot.setStyle(toolTipStyle);
        Tooltip.install(statusDotIcon, forStatusDot);

        Tooltip forGithubIcon = new Tooltip("GitHub");
        forGithubIcon.setStyle(toolTipStyle);
        Tooltip.install(githubIcon, forGithubIcon);

        Tooltip forEmailIcon = new Tooltip("Email");
        forEmailIcon.setStyle(toolTipStyle);
        Tooltip.install(emailIcon, forEmailIcon);

        Tooltip forPhoneIcon = new Tooltip("Phone Number");
        forPhoneIcon.setStyle(toolTipStyle);
        Tooltip.install(phoneIcon, forPhoneIcon);

        Tooltip forJobIcon = new Tooltip("Current Job");
        forJobIcon.setStyle(toolTipStyle);
        Tooltip.install(jobIcon, forJobIcon);

        Tooltip forLocationIcon = new Tooltip("Location");
        forLocationIcon.setStyle(toolTipStyle);
        Tooltip.install(locationIcon, forLocationIcon);

        // Add mouse hover handlers for footerLabel
        footerLabel.setOnMouseEntered(event -> {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            footerLabel.setText(currentDate.format(formatter));
        });

        footerLabel.setOnMouseExited(event -> {
            footerLabel.setText("BroCode © 2025");
        });

        // Add DropShadow effect on mouse hover
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.585);
        dropShadow.setWidth(23.34);
        dropShadow.setColor(Color.rgb(110, 42, 41)); // #6E2A29

        passwordVisibilityButton.setOnMouseEntered(event -> passwordVisibilityButton.setEffect(dropShadow));
        passwordVisibilityButton.setOnMouseExited(event -> passwordVisibilityButton.setEffect(null));

        // Optional: Add visual feedback for interactivity
        backButton.setStyle("-fx-cursor: hand;");
        editButton.setStyle("-fx-cursor: hand;");
        changePasswordButton.setStyle("-fx-cursor: hand;");
        passwordVisibilityButton.setStyle("-fx-cursor: hand;");
//        footerLabel.setStyle("-fx-cursor: hand;");
    }

    private void togglePasswordVisibility(Event event) {
        if (isPasswordVisible) {
            // Hide password
            userPassword.setText(MASKED_PASSWORD);
            passwordVisibilityButton.setImage(new Image(getClass().getResourceAsStream(VISIBILITY_ICON_OFF)));
            isPasswordVisible = false;
        } else {
            // Show password
            userPassword.setText(currentUser.getPassword() != null ? currentUser.getPassword() : "");
            passwordVisibilityButton.setImage(new Image(getClass().getResourceAsStream(VISIBILITY_ICON_ON)));
            isPasswordVisible = true;
        }
    }

    @FXML
    private void handleChangePasswordButton(ActionEvent event) {
        try {
            // Load PasswordSetup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/passwordsetup-view.fxml"));
            Parent root = loader.load();

            // Create a new stage for the password setup dialog
            Stage passwordStage = new Stage();
            passwordStage.initOwner(userDisplayStage); // Sets user display as the parent
            passwordStage.setTitle("Change Password");
            Scene passwordScene = new Scene(root);
            passwordStage.setScene(passwordScene);
            passwordStage.initModality(Modality.WINDOW_MODAL);
            passwordStage.setResizable(false);
            passwordStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/settingIcon1.png")));

            // Pass necessary data to PasswordSetupController
            PasswordSetupController passwordController = loader.getController();
            passwordController.setStage(passwordStage);
            passwordController.setScene(userDisplayStage.getScene(), this);
            passwordController.setUser(currentUser);

            // Show the password setup dialog (user display remains in background)
            passwordStage.showAndWait();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load password setup screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditButton(ActionEvent event) {
        try {
            // Load UserSetting FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/usersetting-view.fxml"));
            Parent root = loader.load();

            // Create a new stage for the user settings dialog
            Stage settingsStage = new Stage();
            settingsStage.initOwner(userDisplayStage); // Sets user display as the parent
            settingsStage.setTitle("Profile");
            Scene settingsScene = new Scene(root);
            settingsStage.setScene(settingsScene);
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.setResizable(false);
            settingsStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/EditorIcon1.png")));

            // Pass necessary data to UserSettingController
            UserSettingController userSettingController = loader.getController();
            userSettingController.setStage(settingsStage);
            userSettingController.setScene(userDisplayStage.getScene(), this);
            userSettingController.setUser(currentUser);

            // Show the settings dialog (user display remains in background)
            settingsStage.showAndWait();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load settings screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        userDisplayStage.close(); // Close the user display stage, home remains open
    }
}