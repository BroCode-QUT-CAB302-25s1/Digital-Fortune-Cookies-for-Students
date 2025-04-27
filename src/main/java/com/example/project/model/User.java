package com.example.project.model;

public class User {
    private int id;
    private String username;
    private String preferredName;
    private String firstName;
    private String lastName;
    private String email;
    private String github;
    private String phone;
    private String location;
    private String job;
    private String gender;
    private String dob;
    private String password;
    private String languages;
    private String cookiesType;

    // Constructor
    public User(String username, String preferredName, String firstName, String lastName,
                String email, String github, String phone, String location, String job,
                String gender, String dob, String password, String languages, String cookiesType) {
        this.username = username;
        this.preferredName = preferredName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.github = github;
        this.phone = phone;
        this.location = location;
        this.job = job;
        this.gender = gender;
        this.dob = dob;
        this.password = password;
        this.languages = languages;
        this.cookiesType = cookiesType;
    }

    public User(String username, String preferredName, String firstName, String lastName,
                String email, String github, String phone, String location, String job,
                String gender, String dob, String password) {
        this.username = username;
        this.preferredName = preferredName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.github = github;
        this.phone = phone;
        this.location = location;
        this.job = job;
        this.gender = gender;
        this.dob = dob;
        this.password = password;
    }

    public User(String email, String password, String username){
        this.email = email;
        this.password = password;
        this.username = username;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPreferredName() { return preferredName; }
    public void setPreferredName(String preferredName) { this.preferredName = preferredName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGithub() { return github; }
    public void setGithub(String github) { this.github = github; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public String getCookiesType() { return cookiesType; }
    public void setCookiesType(String cookiesType) { this.cookiesType = cookiesType; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", preferredName='" + preferredName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", github='" + github + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                ", job='" + job + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", password='" + password + '\'' +
                ", languages='" + languages + '\'' +
                ", cookiesType='" + cookiesType + '\'' +
                '}';
    }
}

