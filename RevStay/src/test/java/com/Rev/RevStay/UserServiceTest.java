package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.UserDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.models.UserType;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.UserService;
import com.Rev.RevStay.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testRegister_Success() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPasswordHash("Password1");
//
//        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
//        when(userDAO.save(any(User.class))).thenReturn(user);
//
//        Optional<User> registeredUser = userService.register(user);
//
//        assertTrue(registeredUser.isPresent());
//        assertEquals(UserType.USER, registeredUser.get().getUserType());
//        verify(userDAO, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void testRegister_EmailAlreadyTaken() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPasswordHash("Password1");
//
//        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
//        assertEquals("Email: test@example.com is already taken!", exception.getMessage());
//    }
//
//    @Test
//    public void testRegister_InvalidEmail() {
//        User user = new User();
//        user.setEmail("invalid-email");
//        user.setPasswordHash("Password1");
//
//        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
//        assertEquals("The email must be like Example@domainExamle.com", exception.getMessage());
//    }
//
//    @Test
//    public void testRegister_InvalidPassword() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPasswordHash("password");
//
//        GenericException exception = assertThrows(GenericException.class, () -> userService.register(user));
//        assertEquals("Invalid Password. Must be at least 8 charactersand need to contain at least one uppercase and lowercase letter ", exception.getMessage());
//    }
//
//    @Test
//    public void testLogin_Success() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPasswordHash(PasswordUtil.hashPassword("Password1"));
//
//        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        try (MockedStatic<PasswordUtil> mockedPasswordUtil = mockStatic(PasswordUtil.class)) {
//            mockedPasswordUtil.when(() -> PasswordUtil.checkPassword(user.getPasswordHash(), "Password1")).thenReturn(true);
//        }
//
//        Optional<UserDTO> loggedInUser = userService.login(new User("test@example.com", "Password1"));
//
//        assertTrue(loggedInUser.isPresent());
//        assertEquals(user.getEmail(), loggedInUser.get().getEmail());
//    }
//
//    @Test
//    public void testLogin_UserNotFound() {
//        when(userDAO.findUserByEmail("test@example.com")).thenReturn(Optional.empty());
//
//        GenericException exception = assertThrows(GenericException.class, () -> userService.login(new User("test@example.com", "Password1")));
//        assertEquals("User not found with tha Email", exception.getMessage());
//    }
//
//    @Test
//    public void testLogin_IncorrectPassword() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPasswordHash(PasswordUtil.hashPassword("Password1"));
//
//        when(userDAO.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        try (MockedStatic<PasswordUtil> mockedPasswordUtil = mockStatic(PasswordUtil.class)) {
//            mockedPasswordUtil.when(() -> PasswordUtil.checkPassword(user.getPasswordHash(), "WrongPassword")).thenReturn(false);
//
//            GenericException exception = assertThrows(GenericException.class, () -> userService.login(new User("test@example.com", "WrongPassword")));
//            assertEquals("Incorrect Password", exception.getMessage());
//        }
//    }
}
