package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.ProfileImageDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UserSettingController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField preferredNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField githubField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField locationField;

    @FXML
    private ChoiceBox<String> jobField;

    @FXML
    private ComboBox<String> genderField;

    @FXML
    private TextField dobField;

    @FXML
    private TextField languagesField;

    @FXML
    private TextField cookiesTypeField;

    @FXML
    private ImageView profileImage;

    @FXML
    private ImageView editProfile;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private static final String DEFAULT_PROFILE_IMAGE = "/com/example/project/symbol/digitalCookieMainIcon1.png";

    private User currentUser;
    private final IUserDAO userDAO;
    private final UserPreferencesDAO userPreferencesDAO;
    private final ProfileImageDAO profileImageDAO;

    private Stage userSettingStage;
    private Scene userDisplayScene;
    private UserDisplayController userDisplayController;

    public UserSettingController() {
        userDAO = new SqliteUserDAO();
        userPreferencesDAO = new UserPreferencesDAO();
        profileImageDAO = new ProfileImageDAO();
    }

    public void setUser(User user) {
        this.currentUser = user;
        displayUserData();
    }

    public void setStage(Stage stage) {
        this.userSettingStage = stage;
    }

    public void setScene(Scene userDisplayScene, UserDisplayController userDisplayController) {
        this.userDisplayScene = userDisplayScene;
        this.userDisplayController = userDisplayController;

        // Clip the image into a circle
        Circle clip = new Circle(110, 110, 110); // x, y, radius
        if (profileImage != null) {
            profileImage.setClip(clip);
        }
    }

    @FXML
    private void initialize() {
        genderField.getItems().addAll("Male", "Female", "Non-binary", "Genderqueer", "Genderfluid",
                "Agender", "Bigender", "Transgender Male", "Transgender Female", "Transmasculine",
                "Transfeminine", "Two-Spirit", "Intersex", "Demiboy", "Demigirl", "Pangender",
                "Androgyne", "Neutrois", "Questioning", "Prefer not to say", "Other");
        jobField.getItems().addAll(
                "Accountant", "Actor", "Actuary", "Administrator", "Advertising Manager", "Aerospace Engineer",
                "Agricultural Engineer", "Air Traffic Controller", "Animator", "Anthropologist", "Architect",
                "Archivist", "Art Director", "Artist", "Astronomer", "Athlete", "Attorney", "Auditor", "Author",
                "Baker", "Banker", "Barber", "Biologist", "Biomedical Engineer", "Bricklayer", "Broker", "Builder",
                "Business Analyst", "Butcher", "Camera Operator", "Carpenter", "Cashier", "Chef",
                "Chemical Engineer", "Chemist", "Choreographer", "Civil Engineer", "Clergy", "Coach",
                "Computer Programmer", "Construction Worker", "Consultant", "Copywriter", "Counselor", "Courier",
                "Customer Service Representative", "Dancer", "Data Analyst", "Data Scientist", "Dentist",
                "Designer", "Dietitian", "Director", "Doctor", "Economist", "Editor", "Electrician", "Engineer",
                "Event Planner", "Farmer", "Fashion Designer", "Financial Analyst", "Firefighter", "Fisherman",
                "Flight Attendant", "Florist", "Graphic Designer", "Hairdresser", "Healthcare Assistant",
                "Historian", "Hotel Manager", "Human Resources Specialist", "Illustrator", "Industrial Designer",
                "Inspector", "Interpreter", "Investigator", "IT Specialist", "Janitor", "Jeweler", "Journalist",
                "Judge", "Laboratory Technician", "Lawyer", "Lecturer", "Librarian", "Lifeguard", "Logistician",
                "Machinist", "Manager", "Marketing Specialist", "Mathematician", "Mechanic", "Medical Assistant",
                "Meteorologist", "Model", "Musician", "Nurse", "Nutritionist", "Occupational Therapist",
                "Office Clerk", "Optician", "Painter", "Paramedic", "Pharmacist", "Photographer", "Physician",
                "Pilot", "Plumber", "Police Officer", "Political Scientist", "Postman", "Principal",
                "Product Manager", "Professor", "Project Manager", "Psychologist", "Public Relations Specialist",
                "Quality Assurance Analyst", "Radiologic Technologist", "Real Estate Agent", "Receptionist",
                "Researcher", "Retail Manager", "Sailor", "Sales Representative", "Scientist", "Security Guard",
                "Social Media Manager", "Social Worker", "Software Developer", "Software Engineer", "Statistician",
                "Storekeeper", "Surgeon", "Surveyor", "Systems Analyst", "Tailor", "Taxi Driver", "Teacher",
                "Technician", "Therapist", "Tour Guide", "Translator", "Travel Agent", "Truck Driver", "Tutor",
                "UX Designer", "Veterinarian", "Video Editor", "Voice Actor", "Waiter", "Warehouse Worker",
                "Web Developer", "Welder", "Writer", "Zoologist", "Acting Coach", "Acupuncturist", "Animal Trainer",
                "App Developer", "Archaeologist", "Art Teacher", "Astrologer", "Auctioneer", "Ballet Dancer",
                "Barista", "Blacksmith", "Blogger", "Bodyguard", "Botanist", "Broadcast Technician",
                "Budget Analyst", "Cartographer", "Chiropractor", "Claims Adjuster", "Clinical Psychologist",
                "Compliance Officer", "Composer", "Computer Scientist", "Conservationist", "Content Creator",
                "Costume Designer", "Crane Operator", "Critic", "Cruise Director", "Curator", "Cybersecurity Analyst",
                "Dance Instructor", "Database Administrator", "Demographer", "Dermatologist", "Dog Trainer",
                "Drone Operator", "Ecologist", "Electric Line Installer", "Embryologist", "Emergency Dispatcher",
                "Emissions Inspector", "Energy Consultant", "Environmental Engineer", "Ergonomist",
                "Event Coordinator", "Exterminator", "Fabricator", "Film Director", "Fire Investigator",
                "Forensic Analyst", "Fundraiser", "Furniture Designer", "Game Designer", "Geneticist",
                "Geographer", "Geologist", "Glassblower", "Grant Writer", "Greenhouse Manager", "Guidance Counselor",
                "Handyman", "Hazardous Materials Removal Worker", "Health Educator", "Helicopter Pilot",
                "Home Inspector", "Horticulturist", "Hospital Administrator", "Hydrologist", "Hypnotherapist",
                "Import/Export Specialist", "Industrial Engineer", "Infection Control Specialist", "Information Architect",
                "Instructional Designer", "Intelligence Analyst", "Interior Decorator", "Interpreter",
                "Inventory Manager", "Investment Banker", "Jewelry Designer", "Judicial Clerk", "Kindergarten Teacher",
                "Landscape Architect", "Linguist", "Loan Officer", "Locksmith", "Makeup Artist", "Marine Biologist",
                "Market Research Analyst", "Massage Therapist", "Mediator", "Medical Biller", "Microbiologist",
                "Mining Engineer", "Mortgage Broker", "Music Producer", "Mycologist", "Naval Officer",
                "Network Administrator", "Neuroscientist", "Notary Public", "Nuclear Engineer", "Oceanographer",
                "Operations Manager", "Ophthalmologist", "Ornithologist", "Paralegal", "Park Ranger",
                "Patent Examiner", "Pathologist", "Pediatrician", "Perfusionist", "Personal Trainer", "Pet Groomer",
                "Phlebotomist", "Photojournalist", "Physical Therapist", "Physicist", "Pilot Instructor",
                "Plant Manager", "Playwright", "Police Detective", "Polygraph Examiner", "Postsecondary Administrator",
                "Private Investigator", "Probation Officer", "Producer", "Proofreader", "Property Manager",
                "Psychiatrist", "Public Defender", "Quality Control Inspector", "Quantitative Analyst",
                "Rabbi", "Radiologist", "Realtor", "Recreational Therapist", "Recycling Coordinator",
                "Referee", "Registrar", "Rehabilitation Counselor", "Religious Educator", "Reporter",
                "Research Analyst", "Risk Manager", "Robotics Engineer", "Safety Officer", "School Counselor",
                "Scientific Illustrator", "Scrum Master", "Seismologist", "Set Designer", "Shipping Coordinator",
                "Silversmith", "Ski Instructor", "Solar Panel Installer", "Sommelier", "Sound Engineer",
                "Special Education Teacher", "Speech-Language Pathologist", "Statistician", "Stockbroker",
                "Stonemason", "Strategic Planner", "Structural Engineer", "Substance Abuse Counselor",
                "Supply Chain Manager", "Surgical Technician", "Tax Advisor", "Technical Writer",
                "Telecommunications Technician", "Theater Manager", "Tour Operator", "Traffic Analyst",
                "Transit Police", "Transportation Planner", "Travel Nurse", "Tree Surgeon", "Urban Planner",
                "Usability Tester", "Veterinary Technician", "Video Game Tester", "Virologist", "Voice Coach",
                "Volunteer Coordinator", "Watchmaker", "Water Treatment Specialist", "Wildlife Biologist",
                "Wind Turbine Technician", "Yoga Instructor"
        );


        // Make emailField non-editable
        emailField.setEditable(false);

        // Only display data if user is set
        if (currentUser != null) {
            displayUserData();
        }
        cancelButton.setOnAction(this::handleCancelButton);
        saveButton.setOnAction(this::handleSaveButton);
        editProfile.setOnMouseClicked(this::handleEditProfile);

        // Add Enter key handler to input field
        usernameField.setOnKeyPressed(this::handleKeyPressed);
        preferredNameField.setOnKeyPressed(this::handleKeyPressed);
        firstNameField.setOnKeyPressed(this::handleKeyPressed);
        lastNameField.setOnKeyPressed(this::handleKeyPressed);
        emailField.setOnKeyPressed(this::handleKeyPressed);
        githubField.setOnKeyPressed(this::handleKeyPressed);
        phoneField.setOnKeyPressed(this::handleKeyPressed);
        locationField.setOnKeyPressed(this::handleKeyPressed);
        dobField.setOnKeyPressed(this::handleKeyPressed);
        languagesField.setOnKeyPressed(this::handleKeyPressed);
        cookiesTypeField.setOnKeyPressed(this::handleKeyPressed);

        // Optional: Add visual feedback for interactivity
        cancelButton.setStyle("-fx-cursor: hand;");
        saveButton.setStyle("-fx-cursor: hand;");
        editProfile.setStyle("-fx-cursor: hand;");

        // Create and style tooltips for buttons with custom font and background
        Tooltip profileEdit = new Tooltip("Edit your avatar");
        profileEdit.setStyle(
                "-fx-background-color: #FFFFE0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;"
        );
        Tooltip.install(editProfile, profileEdit);
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSaveButton(new ActionEvent(saveButton, null));
        }
    }

    @FXML
    private void handleEditProfile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(userSettingStage);
        if (selectedFile != null) {
            try {
                String imageUrl = selectedFile.toURI().toString();
                System.out.println("Selected profile image: " + imageUrl);
                // Update ImageView
                Image image = new Image(imageUrl, true);
                if (image.isError()) {
                    throw new IllegalArgumentException("Failed to load image: " + image.getException().getMessage());
                }
                profileImage.setImage(image);
                // Save to database
                profileImageDAO.saveProfileImage(currentUser.getEmail(), imageUrl);
            } catch (Exception e) {
                ErrorAlert.show("Image Error", "Failed to set profile image: " + e.getMessage());
                System.err.println("Failed to set profile image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
//        if (userDisplayController != null) {
//            userSettingStage.setResizable(false);
//            userSettingStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/project/symbol/userIcon3.png")));
//            userSettingStage.setScene(userDisplayScene);
//        }
        userSettingStage.close();
    }

    private void displayUserData() {
        if (currentUser != null) {
            if (usernameField != null) usernameField.setText(currentUser.getUsername() != null ? currentUser.getUsername() : "");
            if (preferredNameField != null) preferredNameField.setText(currentUser.getPreferredName() != null ? currentUser.getPreferredName() : "");
            if (firstNameField != null) firstNameField.setText(currentUser.getFirstName() != null ? currentUser.getFirstName() : "");
            if (lastNameField != null) lastNameField.setText(currentUser.getLastName() != null ? currentUser.getLastName() : "");
            if (emailField != null) emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            if (githubField != null) githubField.setText(currentUser.getGithub() != null ? currentUser.getGithub() : "");
            if (phoneField != null) phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            if (locationField != null) locationField.setText(currentUser.getLocation() != null ? currentUser.getLocation() : "");
            if (jobField != null) jobField.setValue(currentUser.getJob());
            if (genderField != null) genderField.setValue(currentUser.getGender());
            if (dobField != null) dobField.setText(currentUser.getDob() != null ? currentUser.getDob() : "");

            // Load preferences from user_preferences table
            try {
                String[] preferences = userPreferencesDAO.getPreferences(currentUser.getEmail());
                if (preferences != null && languagesField != null && cookiesTypeField != null) {
                    languagesField.setText(preferences[0] != null ? preferences[0] : "");
                    cookiesTypeField.setText(preferences[1] != null ? preferences[1] : "");
                }
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to load preferences: " + e.getMessage());
            }

            // Load profile image from preferences table
            try {
                String profileImageUrl = profileImageDAO.getProfileImage(currentUser.getEmail());
//                System.out.println(profileImageUrl);
                if (profileImageUrl.compareTo(DEFAULT_PROFILE_IMAGE) == 0) {
                    profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE)));
                } else {
                    if (profileImageUrl.compareTo("/com/example/project/symbol/BroCode.png") == 0) {
                        profileImage.setImage(new Image(getClass().getResourceAsStream(profileImageUrl)));
                    }
                    else {
                        profileImage.setImage(new Image(profileImageUrl, true));
                    }
//                    Image image = new Image(profileImageUrl, true);
//                    profileImage.setImage(image);
                }
            } catch (Exception e) {
                ErrorAlert.show("Image Error", "Failed to load profile image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSaveButton(Event event) {
        if (currentUser == null) {
            ErrorAlert.show("Error", "No user data available to save.");
            return;
        }

        // Update the user object
        currentUser.setUsername(usernameField != null ? usernameField.getText() : null);
        currentUser.setPreferredName(preferredNameField != null ? preferredNameField.getText() : null);
        currentUser.setFirstName(firstNameField != null ? firstNameField.getText() : null);
        currentUser.setLastName(lastNameField != null ? lastNameField.getText() : null);
        currentUser.setEmail(emailField != null ? emailField.getText() : null);
        currentUser.setGithub(githubField != null ? githubField.getText() : null);
        currentUser.setPhone(phoneField != null ? phoneField.getText() : null);
        currentUser.setLocation(locationField != null ? locationField.getText() : null);
        currentUser.setJob(jobField != null ? jobField.getValue() : null);
        currentUser.setGender(genderField != null ? genderField.getValue() : null);
        currentUser.setDob(dobField != null ? dobField.getText() : null);

        // Save to database
        if (saveToDB(currentUser)) {
            // Save preferences to user_preferences table
            try {
                userPreferencesDAO.savePreferences(currentUser.getEmail(),
                        languagesField != null ? languagesField.getText() : null,
                        cookiesTypeField != null ? cookiesTypeField.getText() : null);
            } catch (Exception e) {
                ErrorAlert.show("Database Error", "Failed to save preferences: " + e.getMessage());
            }
            // Switch back to display scene
            if (userDisplayController != null) {
                ErrorAlert.show("Notification", "Your data is saved!");
                userDisplayController.updateUser(currentUser);
//                userSettingStage.setScene(userDisplayScene);
            }
        }
    }

    private boolean saveToDB(User user) {
        // Input validation
        if (!validateUserInput(user)) {
            ErrorAlert.show("Validation Error", "Invalid input. Please check your details.");
            return false;
        }

        try {
            User existingUser = userDAO.getUser(user.getEmail());
            if (existingUser != null) {
                userDAO.updateUser(user);
            } else {
                userDAO.addUser(user);
            }
            return true;
        } catch (Exception e) {
            ErrorAlert.show("Database Error", "Failed to save user: " + e.getMessage());
            return false;
        }
    }

    private boolean validateUserInput(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }
//        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
//            return false;
//        }
//        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
//            return false;
//        }
//        if (user.getPassword() == null || user.getPassword().length() < 6) {
//            return false;
//        }
        return true;
    }
}