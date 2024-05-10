package server.fileencryption;

import java.io.*;
import java.util.HashMap;

/**
 * This class provides methods for encrypting and decrypting files using the ROT13 cipher.
 * ROT13 is a simple letter substitution cipher that replaces each letter with the 13th letter after it in the alphabet.
 * Non-alphabetic characters remain unchanged.
 * @author Sarat
 */
public class FileEncryption {

    private final HashMap<Character, Character> encryptionMap = new HashMap<>();
    {
        // Map lowercase letters
        for (char c = 'a'; c <= 'z'; c++) {
            encryptionMap.put(c, rotateCharacter(c, 13));
        }
        // Map uppercase letters
        for (char c = 'A'; c <= 'Z'; c++) {
            encryptionMap.put(c, rotateCharacter(c, 13));
        }
        // Other characters remain unchanged
        for (char c = 0; c < 128; c++) {
            if (!Character.isLetter(c)) {
                encryptionMap.put(c, c);
            }
        }
    }

    /**
     * Rotates a character by a specified shift according to the ROT13 cipher.
     *
     * @param c     The character to be rotated.
     * @param shift The number of positions to shift the character.
     * @return The rotated character.
     */
    private char rotateCharacter(char c, int shift) {
        if (Character.isLowerCase(c)) {
            return (char) ((c - 'a' + shift) % 26 + 'a');
        } else if (Character.isUpperCase(c)) {
            return (char) ((c - 'A' + shift) % 26 + 'A');
        } else {
            return c; // Return unchanged for non-alphabetic characters
        }
    }

    /**
     * Encrypts a file using the ROT13 cipher.
     *
     * @param filePath The path to the file to be encrypted.
     * @return The path to the encrypted file.
     */
    public String encryptFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "File not found: " + filePath;
        }
        String encryptedFilePath = filePath + "_encrypted";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(encryptedFilePath))) {
            int character;
            while ((character = br.read()) != -1) {
                char c = (char) character;
                char encryptedChar = encryptionMap.getOrDefault(c, c);
                bw.write(encryptedChar);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred during encryption: " + e.getMessage();
        }
        return encryptedFilePath;
    }

    /**
     * Decrypts a file encrypted with the ROT13 cipher.
     *
     * @param filePath The path to the file to be decrypted.
     * @return The path to the decrypted file.
     */
    public String decryptFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "File not found: " + filePath;
        }
        String decryptedFilePath = filePath + "_decrypted";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(decryptedFilePath))) {
            int character;
            while ((character = br.read()) != -1) {
                char c = (char) character;
                char decryptedChar = getDecryptedChar(c);
                bw.write(decryptedChar);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred during decryption: " + e.getMessage();
        }
        return decryptedFilePath;
    }

    /**
     * Retrieves the original character from its encrypted counterpart.
     *
     * @param c The encrypted character.
     * @return The original character.
     */
    private char getDecryptedChar(char c) {
        for (char key : encryptionMap.keySet()) {
            if (encryptionMap.get(key) == c) {
                return key;
            }
        }
        return c;
    }
}

