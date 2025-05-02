import com.example.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private static final String USERNAME_ONE = "AsdjANisajndf";
    private static final String PREFERRED_ONE = "ALANALANALANALANALAN";
    private static final String FIRST_NAME_ONE = "Alan";
    private static final String LAST_NAME_ONE = "Chan";
    private static final String EMAIL_ONE = "alanchan123@gmail.com";
    private static final String GITHUB_ONE = "Alanini234";
    private static final String PHONE_ONE = "57839274";
    private static final String LOCATION_ONE = "Brisbane";
    private static final String JOB_ONE = "Student";
    private static final String GENDER_ONE = "M";
    private static final String DOB_ONE = "12/04/1990";
    private static final String PASSWORD_ONE = "ASLDashdbfnakljshdouadshfb2353245";
    private static final String THEME_ONE = "Light";
    private static final boolean RUN_ON_STARTUP_ONE = false;
    private static final String USERNAME_TWO = "KJnijbadlj";
    private static final String PREFERRED_TWO = "BERRYBOY";
    private static final String FIRST_NAME_TWO = "Berry";
    private static final String LAST_NAME_TWO = "Alan";
    private static final String EMAIL_TWO = "kajndsf23234@gmail.com";
    private static final String GITHUB_TWO = "Sfjhdj98234";
    private static final String PHONE_TWO = "85736264";
    private static final String LOCATION_TWO = "Kenya";
    private static final String JOB_TWO = "CEO";
    private static final String GENDER_TWO = "F";
    private static final String DOB_TWO = "30/12/2017";
    private static final String PASSWORD_TWO = "adfglkjba324";
    private static final String THEME_TWO = "Dark";
    private static final boolean RUN_ON_STARTUP_TWO = true;

    private User user;
    private User userTwo;

    @BeforeEach
    public void setup() {
        user = new User(USERNAME_ONE, PREFERRED_ONE, FIRST_NAME_ONE, LAST_NAME_ONE, EMAIL_ONE,
                GITHUB_ONE, PHONE_ONE, LOCATION_ONE, JOB_ONE, GENDER_ONE, DOB_ONE, PASSWORD_ONE,
                THEME_ONE, RUN_ON_STARTUP_ONE);
        userTwo = new User(USERNAME_TWO, PREFERRED_TWO, FIRST_NAME_TWO, LAST_NAME_TWO, EMAIL_TWO,
                GITHUB_TWO, PHONE_TWO, LOCATION_TWO, JOB_TWO, GENDER_TWO, DOB_TWO, PASSWORD_TWO,
                THEME_TWO, RUN_ON_STARTUP_TWO);
    }

    // Username
    @Test
    public void testGetUsername() {
        assertEquals(USERNAME_ONE, user.getUsername());
    }

    @Test
    public void testSetUsername() {
        user.setUsername(USERNAME_TWO);
        assertEquals(USERNAME_TWO, user.getUsername());
    }

    // Preferred Name
    @Test
    public void testGetPreferredName() {
        assertEquals(PREFERRED_ONE, user.getPreferredName());
    }

    @Test
    public void testSetPreferredName() {
        user.setPreferredName(PREFERRED_TWO);
        assertEquals(PREFERRED_TWO, user.getPreferredName());
    }

    // First Name
    @Test
    public void testGetFirstName() {
        assertEquals(FIRST_NAME_ONE, user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName(FIRST_NAME_TWO);
        assertEquals(FIRST_NAME_TWO, user.getFirstName());
    }

    // Last Name
    @Test
    public void testGetLastName() {
        assertEquals(LAST_NAME_ONE, user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName(LAST_NAME_TWO);
        assertEquals(LAST_NAME_TWO, user.getLastName());
    }

    // Email
    @Test
    public void testGetEmail() {
        assertEquals(EMAIL_ONE, user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL_TWO);
        assertEquals(EMAIL_TWO, user.getEmail());
    }

    // Github
    @Test
    public void testGetGithub() {
        assertEquals(GITHUB_ONE, user.getGithub());
    }

    @Test
    public void testSetGithub() {
        user.setGithub(GITHUB_TWO);
        assertEquals(GITHUB_TWO, user.getGithub());
    }

    // Phone
    @Test
    public void testGetPhone() {
        assertEquals(PHONE_ONE, user.getPhone());
    }

    @Test
    public void testSetPhone() {
        user.setPhone(PHONE_TWO);
        assertEquals(PHONE_TWO, user.getPhone());
    }

    // Location
    @Test
    public void testGetLocation() {
        assertEquals(LOCATION_ONE, user.getLocation());
    }

    @Test
    public void testSetLocation() {
        user.setLocation(LOCATION_TWO);
        assertEquals(LOCATION_TWO, user.getLocation());
    }

    // Job
    @Test
    public void testGetJob() {
        assertEquals(JOB_ONE, user.getJob());
    }

    @Test
    public void testSetJob() {
        user.setJob(JOB_TWO);
        assertEquals(JOB_TWO, user.getJob());
    }

    // Gender
    @Test
    public void testGetGender() {
        assertEquals(GENDER_ONE, user.getGender());
    }

    @Test
    public void testSetGender() {
        user.setGender(GENDER_TWO);
        assertEquals(GENDER_TWO, user.getGender());
    }

    // DOB
    @Test
    public void testGetDOB() {
        assertEquals(DOB_ONE, user.getDob());
    }

    @Test
    public void testSetDOB() {
        user.setDob(DOB_TWO);
        assertEquals(DOB_TWO, user.getDob());
    }

    // Password
    @Test
    public void testGetPassword() {
        assertEquals(PASSWORD_ONE, user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD_TWO);
        assertEquals(PASSWORD_TWO, user.getPassword());
    }

    // Theme
    @Test
    public void testGetTheme() {
        assertEquals(THEME_ONE, user.getTheme());
    }

    @Test
    public void testSetTheme() {
        user.setTheme(THEME_TWO);
        assertEquals(THEME_TWO, user.getTheme());
    }

    // Run on Startup
    @Test
    public void testIsRunOnStartup() {
        assertFalse(user.isRunOnStartup());
    }

    @Test
    public void testSetRunOnStartup() {
        user.setRunOnStartup(RUN_ON_STARTUP_TWO);
        assertTrue(user.isRunOnStartup());
    }
}