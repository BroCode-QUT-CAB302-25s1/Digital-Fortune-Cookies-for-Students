package com.example.project.controller;

import com.example.project.dao.ProfileImageDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Label emailLink;

    @FXML
    private Label phoneNumberLink;

    @FXML
    private Label currentJob;

    @FXML
    private Label currentLocation;

    @FXML
    private Label footerLabel;

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

    private static final String DEFAULT_PROFILE_IMAGE = "/com/example/project/symbol/digitalCookieMainIcon1.png";

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
                    languagesLabel.setText(languages);
                    cookiesTypeLabel.setText(cookiesType);
                    statusOnline.setText("Online");
                    titleHeader.setText("My Profile");

                    // Update profile image
                    if (profileImageUrl != null && !profileImageUrl.isEmpty() && profileImageUrl != DEFAULT_PROFILE_IMAGE) {
                        try {
//                            System.out.println("Attempting to load profile image: " + profileImageUrl);
                            profileImage.setImage(new Image(profileImageUrl, true));
                        } catch (Exception e) {
                            profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE)));
                        }
                    } else {
                        System.out.println("No profile image URL found for user: " + user.getEmail());
                        profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE)));
                    }
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

        // Optional: Add visual feedback for interactivity
        backButton.setStyle("-fx-cursor: hand;");
        editButton.setStyle("-fx-cursor: hand;");
        changePasswordButton.setStyle("-fx-cursor: hand;");
    }

    @FXML
    private void handleChangePasswordButton(ActionEvent event) {
        try {
            // Load PasswordSetup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/passwordsetup-view.fxml"));
            Parent root = loader.load();

            // Create a new stage for the password setup dialog
            Stage passwordStage = new Stage();
//            passwordStage.initModality(Modality.APPLICATION_MODAL); // Makes it modal, blocking the user display
            passwordStage.initOwner(userDisplayStage); // Sets user display as the parent
            passwordStage.setTitle("Change Password");
            Scene passwordScene = new Scene(root);
            passwordStage.setScene(passwordScene);

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
            root = loader.load();
            Stage userDisplayStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene userDisplayScene = new Scene(root);
            userDisplayStage.setTitle("Profile");
            userDisplayStage.setScene(userDisplayScene);

            // Pass the stage and scene to UserSettingController
            UserSettingController userSettingController = loader.getController();
            userSettingController.setStage(userDisplayStage);
            userSettingController.setScene(editButton.getScene(), this);
            userSettingController.setUser(currentUser);

            userDisplayStage.show();
        } catch (IOException e) {
            ErrorAlert.show("Navigation Error", "Failed to load settings screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        if (homeController != null) {
            userDisplayStage.setScene(homeScene);
        }
    }
}