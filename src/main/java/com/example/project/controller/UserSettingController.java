package com.example.project.controller;

import com.example.project.dao.IUserDAO;
import com.example.project.dao.ProfileImageDAO;
import com.example.project.dao.SqliteUserDAO;
import com.example.project.dao.UserPreferencesDAO;
import com.example.project.model.User;
import com.example.project.util.ErrorAlert;
import com.example.project.util.SuccessAlert;
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
                // #A
                "Accountant", "Actor", "Acting Coach", "Actuary", "Acupuncturist", "Administrator", "Advertising Manager",
                "Aerospace Engineer", "Agricultural Engineer", "Air Traffic Controller", "AI Researcher", "Animator",
                "Animal Trainer", "Anthropologist", "App Developer", "Apprentice", "Archaeologist", "Architect", "Archivist",
                "Art Director", "Art Teacher", "Artist", "Astrologer", "Astronomer", "Athlete", "Attorney", "Auctioneer",
                "Audio Engineer", "Auto Mechanic", "Author",

                // #B
                "Backend Developer", "Baker", "Ballet Dancer", "Banker", "Barber", "Barista", "Biologist",
                "Biomedical Engineer", "Blacksmith", "Blogger", "Bodyguard", "Botanist", "Broadcast Technician",
                "Broker", "Builder", "Budget Analyst", "Business Analyst", "Business Development Manager",
                "Business Owner", "Butcher",

                // #C
                "Camera Operator", "Carpenter", "Cardiologist", "Cartographer", "Cashier", "Chef", "Chemical Engineer",
                "Chemist", "Chiropractor", "Choreographer", "Civil Engineer", "Claims Adjuster", "Clergy",
                "Clinical Psychologist", "Cloud Engineer", "Coach", "Computer Programmer", "Computer Scientist",
                "Conservationist", "Consultant", "Content Creator", "Content Marketer", "Contractor", "Copywriter",
                "Costume Designer", "Counselor", "Courier", "Crane Operator", "Critic", "Cruise Director", "Curator",
                "Customer Service Representative", "Cybersecurity Analyst", "Cybersecurity Specialist",

                // #D
                "Dance Instructor", "Dancer", "Data Analyst", "Data Engineer", "Data Scientist", "Database Administrator",
                "Demographer", "Dentist", "Dermatologist", "Designer", "Developer", "DevOps Engineer",

                // #E
                "Ecologist", "Economist", "Editor", "Electric Line Installer", "Electrician", "Emergency Dispatcher",
                "Emergency Room Doctor", "Emissions Inspector", "Energy Consultant", "Engineer", "English Teacher",
                "Environmental Engineer", "Ergonomist", "Event Coordinator", "Event Planner", "Exterminator",

                // #F
                "Fabricator", "Farmer", "Fashion Designer", "Financial Analyst", "Financial Planner", "Fire Investigator",
                "Firefighter", "Fisherman", "Flight Attendant", "Florist", "Forensic Analyst", "Freelancer", "Fundraiser",
                "Furniture Designer",

                // #G
                "Game Designer", "Game Developer", "General Practitioner", "Geneticist", "Geographer", "Geologist",
                "Glassblower", "Graphic Designer", "Greenhouse Manager", "Guidance Counselor",

                // #H
                "Hairdresser", "Handyman", "Hazardous Materials Removal Worker", "Health Educator", "Healthcare Assistant",
                "Helicopter Pilot", "Historian", "Home Inspector", "Horticulturist", "Hospital Administrator", "HR Specialist",
                "HVAC Technician", "Hydrologist", "Hypnotherapist",

                // #I
                "Illustrator", "Import/Export Specialist", "Industrial Designer", "Industrial Engineer",
                "Infection Control Specialist", "Information Architect", "Inspector", "Instructional Designer",
                "Insurance Agent", "Intelligence Analyst", "Interior Decorator", "Interpreter", "Inventory Manager",
                "Investment Analyst", "Investment Banker", "IT Specialist", "IT Support Specialist",

                // #J
                "Janitor", "Jeweler", "Jewelry Designer", "Journalist", "Judge", "Judicial Clerk",

                // #K
                "Kindergarten Teacher",

                // #L
                "Laboratory Technician", "Landscape Architect", "Lawyer", "Lecturer", "Librarian", "Lifeguard",
                "Linguist", "Loan Officer", "Locksmith", "Logistician",

                // #M
                "Machinist", "Makeup Artist", "Manager", "Marine Biologist", "Market Research Analyst", "Marketing Specialist",
                "Massage Therapist", "Mathematician", "Math Teacher", "Mechanic", "Mediator", "Medical Assistant",
                "Medical Biller", "Meteorologist", "Microbiologist", "Mining Engineer", "Model", "Motion Graphics Designer",
                "Mortgage Broker", "Motorcycle Mechanic", "Music Producer", "Musician", "Mycologist",

                // #N
                "Nanny", "Naval Officer", "Network Administrator", "Network Engineer", "Neuroscientist", "Nuclear Engineer",
                "Nurse", "Nurse Practitioner", "Nutritionist",

                // #O
                "Occupational Therapist", "Oceanographer", "Office Clerk", "Oncologist", "Operations Manager",
                "Ophthalmologist", "Optician", "Ornithologist",

                // #P
                "Painter", "Paramedic", "Park Ranger", "Paralegal", "Patent Examiner", "Pathologist", "Payroll Specialist",
                "Pediatrician", "Perfusionist", "Personal Trainer", "Pet Groomer", "Pharmacist", "Pharmacy Technician",
                "Phlebotomist", "Photojournalist", "Photographer", "Physical Therapist", "Physician", "Physicist",
                "Pilot", "Pilot Instructor", "Plumber", "Police Detective", "Police Officer", "Political Scientist",
                "Postdoctoral Researcher", "Postman", "Postsecondary Administrator", "Principal", "Producer",
                "Product Manager", "Professor", "Project Manager", "Proofreader", "Property Manager", "Psychiatrist",
                "Psychologist", "Public Defender", "Public Relations Specialist", "Publisher",

                // #Q
                "Quality Assurance Analyst", "Quality Control Inspector", "Quantitative Analyst",

                // #R
                "Rabbi", "Radiologic Technologist", "Radiologist", "Real Estate Agent", "Realtor", "Receptionist",
                "Recreational Therapist", "Recycling Coordinator", "Referee", "Registrar", "Rehabilitation Counselor",
                "Religious Educator", "Reporter", "Research Analyst", "Research Assistant", "Researcher", "Retired",
                "Risk Analyst", "Risk Manager", "Robotics Engineer",

                // #S
                "Sailor", "Sales Representative", "School Counselor", "Scientist", "Scientific Illustrator",
                "Scrum Master", "Seismologist", "Security Guard", "SEO Specialist", "Set Designer", "Shipping Coordinator",
                "Silversmith", "Social Media Content Creator", "Social Media Manager", "Social Worker", "Software Developer",
                "Software Engineer", "Solar Panel Installer", "Sommelier", "Sound Designer", "Sound Engineer",
                "Special Education Teacher", "Speech-Language Pathologist", "Statistician", "Storekeeper", "Strategic Planner",
                "Structural Engineer", "Student", "Substance Abuse Counselor", "Supply Chain Manager", "Surgeon",
                "Surgical Nurse", "Surgical Technician", "Surveyor", "Systems Analyst",

                // #T
                "Tailor", "Tax Advisor", "Tax Consultant", "Taxi Driver", "Teacher", "Technician",
                "Technical Writer", "Telecommunications Technician", "Theater Manager", "Therapist", "Tour Guide",
                "Tour Operator", "Traffic Analyst", "Transit Police", "Translator", "Transportation Planner", "Travel Agent",
                "Travel Nurse", "Tree Surgeon", "Truck Driver", "Tutor",

                // #U
                "Unemployed", "Urban Planner", "Usability Tester", "UX Designer", "UX Researcher",

                // #V
                "Veterinarian", "Veterinary Technician", "Video Editor", "Video Game Artist", "Video Game Tester",
                "Virologist", "Voice Actor", "Voice Coach", "Volunteer", "Volunteer Coordinator",

                // #W
                "Waiter", "Warehouse Worker", "Watchmaker", "Water Treatment Specialist", "Web Developer", "Welder",
                "Wildlife Biologist", "Wind Turbine Technician", "Writer",

                // #Y
                "Yoga Instructor",

                // #Z
                "Zoologist"
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
                SuccessAlert.show("Notification", "Your data is saved!");
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
        return true;
    }
}