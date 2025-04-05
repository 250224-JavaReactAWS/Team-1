package com.Rev.RevStay.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    //We make a class for hash the password and save it in the DB
    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String storedHash,String password ) {
        return BCrypt.checkpw(password, storedHash);
    }
}
