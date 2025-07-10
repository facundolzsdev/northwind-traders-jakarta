package com.northwind.util.security;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 * <p>
 * Ensures secure password handling through salting and hashing.
 * </p>
 */
@UtilityClass
public class PasswordEncoder {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword the password to hash
     * @return the hashed password
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Verifies that a plain-text password matches a previously hashed one.
     * <p>
     * If the hashed password is invalid this method returns {@code false} rather than throwing an exception.
     * </p>
     *
     * @param plainPassword  the raw password to check
     * @param hashedPassword the stored hashed password
     * @return {@code true} if the password matches the hash; {@code false} otherwise
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}