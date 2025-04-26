package com.example.project.testing;
import com.example.project.controller.SignUpController;
import com.example.project.dao.MockUserDAO;
import com.example.project.model.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignUpControllerTest {
    private SignUpController controller;
    private MockUserDAO mockUserDAO;
//
//    @BeforeEach
//    void setUp() {
//        mockUserDAO = new MockUserDAO();
//        controller = new SignUpController(mockUserDAO);
//        // Initialize FXML fields for testing
//        controller.setEmailField(new TextField());
//        controller.setPasswordField(new PasswordField());
//        controller.setUsernameField(new TextField());
//    }
//
//    @Test
//    void testValidSignup() {
//        controller.setEmailField("test@example.com");
//        controller.passwordField.setText("secure123");
//        controller.usernameField.setText("testuser");
//
//        controller.handleSignupButton();
//
//        User user = mockUserDAO.getUser("test@example.com");
//        assertNotNull(user);
//        assertEquals("test@example.com", user.getEmail());
//        assertEquals("testuser", user.getUsername());
//        assertEquals("secure123", user.getPassword());
//        assertEquals(2, user.getId()); // ID=2 since initial user has ID=1
//    }
//
//    @Test
//    void testInvalidEmail() {
//        controller.setEmailField("invalid");
//        controller.passwordField.setText("secure123");
//        controller.usernameField.setText("testuser");
//
//        controller.handleSignupButton();
//
//        User user = mockUserDAO.getUser("invalid");
//        assertNull(user); // No user should be added
//    }
//
//    @Test
//    void testDuplicateEmail() {
//        controller.setEmailField("brocode.QUT@gmail.com"); // Initial user email
//        controller.passwordField.setText("secure123");
//        controller.usernameField.setText("newuser");
//
//        controller.handleSignupButton();
//
//        // Verify only one user with this email exists
//        long count = mockUserDAO.getAllUsers().stream()
//                .filter(u -> u.getEmail().equals("brocode.QUT@gmail.com"))
//                .count();
//        assertEquals(1, count);
//    }
//
//    @Test
//    void testDuplicateUsername() {
//        controller.setEmailField("new@example.com");
//        controller.passwordField.setText("secure123");
//        controller.usernameField.setText("brocodeTest01"); // Initial user username
//
//        controller.handleSignupButton();
//
//        User user = mockUserDAO.getUser("new@example.com");
//        assertNull(user); // No user should be added
//    }
//
//    @Test
//    void testInvalidPassword() {
//        controller.setEmailField("test@example.com");
//        controller.passwordField.setText("short");
//        controller.usernameField.setText("testuser");
//
//        controller.handleSignupButton();
//
//        User user = mockUserDAO.getUser("test@example.com");
//        assertNull(user); // No user should be added
//    }
//
//    @Test
//    void testInvalidUsername() {
//        controller.setEmailField("test@example.com");
//        controller.passwordField.setText("secure123");
//        controller.usernameField.setText("ab"); // Too short
//
//        controller.handleSignupButton();
//
//        User user = mockUserDAO.getUser("test@example.com");
//        assertNull(user); // No user should be added
//    }
}