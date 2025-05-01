<p align="center">
    <img src="src/main/resources/com/example/project/symbol/BroCode.png" alt="BroCode Logo">
</p>

# Digital Fortune Cookies for Students

## Overview
Digital Fortune Cookies for Students is a Java-based desktop application developed by the **BroCode** team for the CAB302 Software Development course at QUT (Semester 1, 2025). It enhances the learning experience by delivering personalized study tips, motivational quotes, and fun predictions through a user-friendly JavaFX interface. The app tracks learning progress against user-set study targets, with AI-generated content tailored to time spent studying or remaining time, making studying engaging and motivating. **User stories** prioritize features like setting study goals, tracking study hours, and receiving time-based feedback to support student progress. Built using an **Agile methodology**, the project employs **Object-Oriented Design (OOD)**, **Test-Driven Development (TDD)**, and **GitHub** for version control, aligning with industry standards (ACS CBOK: 1, 1.2-1.4, 1.6; SFIA: PROG).

The application uses general AI integration to generate dynamic content. The Week 9 preliminary prototype includes a functional GUI, authentication, SQLite persistence, and unit tests, with a full feature set planned for Week 13.

<p align="center">
    <img src="src/main/resources/com/example/project/symbol/digitalCookieMainIcon2.png" alt="Digital Fortune Cookies Logo" width="60%">
</p>

## Features
- **Personalized Study Tips**: AI-generated advice tailored to study habits, progress, and time spent or remaining toward user-set targets.
- **Motivational Quotes**: Customized inspirational quotes reflecting study time or goals.
- **Fun Predictions**: Lighthearted predictions tied to learning progress to keep users engaged.
- **Progress Tracking**: Monitors study time and progress against user-defined targets.
- **User Authentication**: Secure sign-up/sign-in system with GUI and models.
- **Data Persistence**: Stores user data in a SQLite `.db` file for accounts and progress.
- **Graphical User Interface**: Intuitive JavaFX-based interface.

## Technologies Used
- **Java**: Core programming language; Amazon Corretto 21 strongly recommended, though other JDKs (17+) are supported.
- **JavaFX**: Framework for building the GUI.
- **AI**: General AI integration for generating personalized content.
- **Persistence**: SQLite `.db` file for user data storage.
- **JUnit**: Unit testing for code reliability.
- **Maven**: Dependency and build management.
- **Git**: Version control with branching workflow.

## Installation
### Prerequisites
- **Java Development Kit (JDK)**: Amazon Corretto 21 strongly recommended, available at [AWS Corretto Downloads](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html); other JDKs (17+) are compatible.
- JavaFX SDK
- Maven
- Git

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/BroCode-QUT-CAB302-25s1/Digital-Fortune-Cookies-for-Students.git
   cd Digital-Fortune-Cookies-for-Students
   ```

2. **Configure JavaFX**:
   - Download JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/).
   - Set up in your IDE (e.g., IntelliJ, Eclipse) or Maven `pom.xml`.

3. **Install Dependencies**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   - The SQLite `.db` file is automatically created if it does not exist.
   ```bash
   mvn javafx:run
   ```

## Usage
1. **Sign Up/Sign In**: Create or log into an account.
2. **Set Study Targets**: Define goals (e.g., study hours per week).
3. **Receive Daily Messages**: View tips, quotes, and predictions based on study time or remaining goals.
4. **Track Progress**: Update study hours to monitor progress toward targets.
5. **Explore Interface**: Navigate JavaFX windows for features and settings.

## Project Structure
```
Digital-Fortune-Cookies-for-Students/
├── .idea/                     # IDE configuration (IntelliJ)
├── .mvn/                      # Maven wrapper files
├── lib/                       # External libraries
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/project/
│   │   │       ├── controller/    # JavaFX controllers for UI logic
│   │   │       ├── dao/           # Data Access Objects for database interaction
│   │   │       ├── database/      # Database connection and setup
│   │   │       ├── model/         # Data models (e.g., User, StudyProgress)
│   │   │       ├── util/          # Utility classes and helpers
│   │   │       ├── services/      # AI integration - API
│   │   │       ├── MainApplication.java  # Application entry point
│   │   │       └── module-info.java      # Java module configuration
│   │   └── resources/
│   │       └── com/example/project/      # Contain FXML for UI
│   │           ├── images/        # Image assets
│   │           ├── style/         # CSS styles for JavaFX UI
│   │           └── symbol/        # Logo and icon files (e.g., digitalCookieMainIcon2.png)
│   └── test/                  # JUnit tests
├── README.md                  # Project documentation (this file)
├── pom.xml                    # Maven configuration
└── userData.db                # SQLite database file for user data storage
```

## Contributing
This project is developed by the **BroCode** team for CAB302. Team members can contribute by:
1. Creating a branch (`git checkout -b feature/name`).
2. Making and testing changes.
3. Submitting a pull request with a clear description.

## License
This project is for educational purposes only and is not distributed under a specific license. All code and resources are intended for use within the CAB302 course at QUT.

## Contact
For questions or support, contact the **BroCode** team via:
- **Antares (Van Thien Phuoc Mai)**: overlimit090@gmail.com
- **GitHub**: [BroCode-QUT-CAB302-25s1/Digital-Fortune-Cookies-for-Students](https://github.com/BroCode-QUT-CAB302-25s1/Digital-Fortune-Cookies-for-Students)
- QUT course channels

## Acknowledgments
- QUT CAB302 teaching team for guidance and resources.
- JavaFX community for UI development support.
