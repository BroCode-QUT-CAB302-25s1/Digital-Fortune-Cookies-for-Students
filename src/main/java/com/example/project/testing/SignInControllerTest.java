package com.example.project.testing;

import com.example.project.controller.SignInController;
import com.example.project.dao.MockUserDAO;
import com.example.project.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignInControllerTest {
    private SignInController controller;
    private MockUserDAO mockUserDAO;

//    @BeforeEach
//    void setUp() {
//        mockUserDAO = new MockUserDAO();
//        controller = new SignInController(mockUserDAO);
//        controller.setEmailField(new TextField());
//        controller.setPasswordField(new PasswordField());
//        Button loginButton = new Button();
//        loginButton.setScene(new Scene(new VBox()));
//        controller.setLoginButton(loginButton);
//    }
//
//    @Test
//    void testValidLogin() {
//        controller.emailField.setText("brocode.QUT@gmail.com");
//        controller.passwordField.setText("12345");
//        controller.handleLoginButton(null);
//        // Verify navigation in real app; here, just check user exists
//        User user = mockUserDAO.getUser("brocode.QUT@gmail.com");
//        assertNotNull(user);
//        assertEquals("12345", user.getPassword());
//    }
//
//    @Test
//    void testInvalidEmail() {
//        controller.emailField.setText("nonexistent@example.com");
//        controller.passwordField.setText("12345");
//        controller.handleLoginButton(null);
//        // No navigation; alert shown in real app
//        User user = mockUserDAO.getUser("nonexistent@example.com");
//        assertNull(user);
//    }
}