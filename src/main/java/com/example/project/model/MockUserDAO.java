package com.example.project.model;

import java.util.ArrayList;
import java.util.List;

public class MockUserDAO implements IUserDAO {
    /**
     * A static list of users to be used as a mock database.
     */
    private static final ArrayList<User> users = new ArrayList<>();
    private static int autoIncrementedId = 0;

    public MockUserDAO() {
        // Add an initial user to the mock database
        addUser(new User(
                "brocodeTest01",
                "BroCode",
                "QUT",
                "BroCode",
                "brocode.QUT@gmail.com",
                "@BroCode-QUT",
                "0000 000 0001",
                "Australia",
                "Backend Developer",
                "Male",
                "01/01/2025",
                "12345"
        ));
    }

    @Override
    public void addUser(User user) {
        user.setId(autoIncrementedId);
        autoIncrementedId++;
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        users.removeIf(u -> u.getUsername().equals(user.getUsername()));
    }

    @Override
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}