package server.passwordgenerator;

import java.util.Random;

/**
 * This class generates random passwords.
 * 
 * @author Sarat
 */
public class PasswordGenerator {

    /**
     * Generates a random password of specified length.
     * 
     * @param length The length of the password to generate.
     * @return A randomly generated password.
     * @throws IllegalArgumentException if length is less than or equal to 0.
     */
    public String generatePassword(int length) {
        // Define character sets
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-=_+[]{}|;:,.<>?";

        // Combine all character sets
        String allChars = upper + lower + digits + specialChars;
        
        // Check for valid length
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be greater than 0");
        }

        // Generate password
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(allChars.charAt(rand.nextInt(allChars.length())));
        }
        return password.toString();
    }
}

