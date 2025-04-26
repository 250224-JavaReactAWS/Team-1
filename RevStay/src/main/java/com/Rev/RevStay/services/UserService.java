package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.UserDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.util.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing user-related operations such as registration,
 * login,
 * and managing favorite hotels.
 * 
 * This class provides methods to:
 * - Register a new user.
 * - Login a user.
 * - Add a hotel to a user's favorites.
 * - Remove a hotel from a user's favorites.
 * 
 * It uses `UserDAO` and `HotelDAO` for database interactions and `PasswordUtil`
 * for password hashing and validation.
 * 
 * Exceptions:
 * - Throws `GenericException` for invalid inputs or when required entities are
 * not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 * - `@Transactional`: Ensures that database operations are executed within a
 * transaction.
 */
@Service
@Transactional
public class UserService {

    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    /**
     * Constructor for UserService.
     * 
     * @param userDAO  Data access object for user-related operations.
     * @param hotelDAO Data access object for hotel-related operations.
     */
    @Autowired
    public UserService(UserDAO userDAO, HotelDAO hotelDAO) {
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
    }

    /**
     * Registers a new user.
     * 
     * @param userToBeRegistered The user to be registered.
     * @return An Optional containing the registered user.
     * @throws GenericException if the email is already taken, the email format is
     *                          invalid,
     *                          or the password does not meet the required criteria.
     */
    public Optional<User> register(User userToBeRegistered) {

        Optional<User> potentialUser = userDAO.findUserByEmail(userToBeRegistered.getEmail());

        if (potentialUser.isPresent()) {
            throw new GenericException("Email: " + userToBeRegistered.getEmail() + " is already taken!");
        }

        if (!userToBeRegistered.getEmail().matches(EMAIL_PATTERN)) {
            throw new GenericException("The email must be like Example@domainExamle.com");
        }

        if (!userToBeRegistered.getPasswordHash().matches(PASSWORD_PATTERN)) {
            throw new GenericException("Invalid Password. Must be at least 8 characters" +
                    "and need to contain at least one uppercase and lowercase letter ");
        }

        String hashPassword = PasswordUtil.hashPassword(userToBeRegistered.getPasswordHash());

        userToBeRegistered.setPasswordHash(hashPassword);

        return Optional.of(userDAO.save(userToBeRegistered));
    }

    /**
     * Logs in a user by validating their credentials.
     * 
     * @param userCredentials The user's login credentials.
     * @return An Optional containing the UserDTO of the logged-in user.
     * @throws GenericException if the user is not found or the password is
     *                          incorrect.
     */
    public Optional<UserDTO> login(User userCredentials) {
        Optional<User> user = userDAO.findUserByEmail(userCredentials.getEmail());
        User userToLogin;

        if (user.isEmpty()) {
            throw new GenericException("User not found with the Email");
        } else {
            userToLogin = user.get();
        }

        if (!PasswordUtil.checkPassword(userToLogin.getPasswordHash(), userCredentials.getPasswordHash())) {
            throw new GenericException("Incorrect Password");
        }
        UserDTO userDTO = new UserDTO(userToLogin.getUserId(), userToLogin.getEmail(), userToLogin.getFullName(),
                userToLogin.getUserType());
        return Optional.of(userDTO);
    }

    /**
     * Adds a hotel to the user's list of favorite hotels.
     * 
     * @param userId  The ID of the user.
     * @param hotelId The ID of the hotel to be added to favorites.
     * @throws GenericException if the user or hotel does not exist.
     */
    public void addHotelToFavorites(int userId, int hotelId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Hotel> hotel = hotelDAO.findById(hotelId);

        if (hotel.isEmpty()) {
            throw new GenericException("The hotel does not exist");
        }
        if (user.isEmpty()) {
            throw new GenericException(("The user does not exist"));
        }
        User userExist = user.get();
        Hotel hotelExist = hotel.get();

        if (!userExist.getFavoriteHotels().contains(hotelExist)) {
            userExist.getFavoriteHotels().add(hotelExist);
            // hotelExist.getUsersWhoFavorite().add(userExist);
            userDAO.save(userExist);
        }
    }

    /**
     * Removes a hotel from the user's list of favorite hotels.
     * 
     * @param userId  The ID of the user.
     * @param hotelId The ID of the hotel to be removed from favorites.
     * @throws GenericException if the user or hotel does not exist.
     */
    public void removeHotelFromFavorites(int userId, int hotelId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Hotel> hotel = hotelDAO.findById(hotelId);

        if (hotel.isEmpty()) {
            throw new GenericException("The hotel does not exist");
        }
        if (user.isEmpty()) {
            throw new GenericException(("The user does not exist"));
        }
        User userExist = user.get();
        Hotel hotelExist = hotel.get();

        userExist.getFavoriteHotels().remove(hotelExist);
        // hotelExist.getUsersWhoFavorite().remove(userExist);
        userDAO.save(userExist);
    }
}
