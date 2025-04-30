package com.example.project.dao;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDAO implements IUserDAO {
    private List<User> users = new ArrayList<>();

    public MockUserDAO() {
        // Add an initial user to simulate DatabaseInitializer
        User initialUser = new User(
                "brocodeTest01",
                "BroCode",
                "BroCode",
                "QUT",
                "brocode.QUT@gmail.com",
                "@BroCode-QUT",
                "0000 000 0001",
                "Australia",
                "Backend Developer",
                "Male",
                "01/01/2025",
                "123456"
        );
        users.add(initialUser);
    }

    @Override
    public void addUser(User user) {
        // Check for duplicate email or username
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()) || u.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("Email or username already exists");
        }
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(user.getEmail())) {
                users.set(i, user);
                return;
            }
        }
        throw new IllegalArgumentException("No user found with email: " + user.getEmail());
    }

    @Override
    public void deleteUser(User user) {
        users.removeIf(u -> u.getEmail().equals(user.getEmail()));
    }

    @Override
    public User getUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}