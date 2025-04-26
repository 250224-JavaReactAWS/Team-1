package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.UserDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.models.UserType;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.UserService;
import com.Rev.RevStay.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for testing user-related operations in the `UserService`.
 * 
 * This class contains test cases to verify the functionality of user registration, 
 * login, and validation of user credentials.
 * 
 * Annotations:
 * - `@Mock`: Marks dependencies to be mocked using Mockito.
 * - `@InjectMocks`: Injects mocked dependencies into the `UserService` instance.
 * - `@BeforeEach`: Sets up the test environment before each test case.
 * - `@Test`: Marks a method as a test case.
 * 
 * Test Cases:
 * - `testRegister_Success`: Verifies successful user registration.
 * - `testRegister_EmailAlreadyTaken`: Verifies behavior when the email is already taken.
 * - `testRegister_InvalidEmail`: Verifies behavior when the email format is invalid.
 * - `testRegister_InvalidPassword`: Verifies behavior when the password does not meet requirements.
 * - `testLogin_Success`: Verifies successful user login with correct credentials.
 * - `testLogin_UserNotFound`: Verifies behavior when the user is not found during login.
 * - `testLogin_IncorrectPassword`: Verifies behavior when the password is incorrect during login.
 */
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private HotelDAO hotelDAO;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPasswordHash("Password123");
        user.setUserType(UserType.USER);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userDAO.save(any(User.class))).thenReturn(user);

        Optional<User> registeredUser = userService.register(user);

        assertTrue(registeredUser.isPresent());
        assertEquals(UserType.USER, registeredUser.get().getUserType());
        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    public void testRegister_EmailAlreadyTaken() {
        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
        assertEquals("Email: test@example.com is already taken!", exception.getMessage());
    }

    @Test
    public void testRegister_InvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setPasswordHash("Password1");

        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
        assertEquals("The email must be like Example@domainExamle.com", exception.getMessage());
    }

    @Test
    public void testRegister_InvalidPassword() {
        user.setPasswordHash("abc");

        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
        assertEquals(
                "Invalid Password. Must be at least 8 characters and need to contain at least one uppercase and lowercase letter ",
                exception.getMessage());
    }

    @Test
    public void testLogin_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        String rawPassword = "Password123";
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        user.setPasswordHash(hashedPassword);

        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loginAttempt = new User();
        loginAttempt.setEmail(user.getEmail());
        loginAttempt.setPasswordHash(rawPassword);

        Optional<UserDTO> result = userService.login(loginAttempt);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    public void testLogin_UserNotFound() {
        when(userDAO.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        GenericException exception = assertThrows(GenericException.class,
                () -> {
                    User loginAttempt = new User();
                    loginAttempt.setEmail("test@example.com");
                    loginAttempt.setPasswordHash("Password1");
                    userService.login(loginAttempt);
                });
        assertEquals("User not found with the Email", exception.getMessage());
    }

    @Test
    void testLogin_IncorrectPassword() {
        user.setPasswordHash(PasswordUtil.hashPassword("Password123"));
        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loginAttempt = new User();
        loginAttempt.setEmail(user.getEmail());
        loginAttempt.setPasswordHash("WrongPassword123");

        GenericException ex = assertThrows(GenericException.class, () -> userService.login(loginAttempt));
        assertEquals("Incorrect Password", ex.getMessage());
    }
}
