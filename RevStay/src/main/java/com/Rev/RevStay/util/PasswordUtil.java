package com.Rev.RevStay.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for handling password hashing and verification.
 * 
 * This class provides methods to securely hash passwords and verify them 
 * against stored hashes using the BCrypt algorithm.
 * 
 * Methods:
 * - `hashPassword(String password)`: Hashes a plain-text password.
 * - `checkPassword(String storedHash, String password)`: Verifies a plain-text 
 *   password against a stored hash.
 */
public class PasswordUtil {

    /**
     * Hashes a plain-text password using the BCrypt algorithm.
     * 
     * @param password The plain-text password to hash.
     * @return The hashed password.
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verifies a plain-text password against a stored hash.
     * 
     * @param storedHash The hashed password stored in the database.
     * @param password   The plain-text password to verify.
     * @return `true` if the password matches the hash, `false` otherwise.
     */
    public static boolean checkPassword(String storedHash, String password) {
        return BCrypt.checkpw(password, storedHash);
    }
}
