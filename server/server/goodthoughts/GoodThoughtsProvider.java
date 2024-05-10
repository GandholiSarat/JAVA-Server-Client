package server.goodthoughts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A provider for fetching random good thoughts from a file.The file should contain one good thought per line.If the file is not found or there is an error reading it, an empty list will be used.
 * @author Sarat
 */
public class GoodThoughtsProvider {
    private List<String> goodThoughts;
    private Random random;

    /**
     * Constructs a GoodThoughtsProvider.
     * Reads good thoughts from a file and initializes a random number generator.
     */
    public GoodThoughtsProvider() {
        this.goodThoughts = readGoodThoughtsFromFile();
        this.random = new Random();
    }

    /**
     * Reads good thoughts from a file.
     * @return A list of good thoughts read from the file, or an empty list if there is an error.
     */
    private List<String> readGoodThoughtsFromFile() {
        List<String> thoughts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("good_thoughts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                thoughts.add(line);
            }
        } catch (FileNotFoundException e) {
            // File not found, log the error and return an empty list
            System.err.println("File 'good_thoughts.txt' not found.");
        } catch (IOException e) {
            // Error reading file, log the error and return an empty list
            System.err.println("Error reading file: " + e.getMessage());
        }
        return thoughts;
    }

    /**
     * Gets a random good thought from the list.
     * @return A random good thought.
     */
    public String getRandomGoodThought() {
        if (goodThoughts.isEmpty()) {
            return "No good thoughts available.";
        }
        int index = random.nextInt(goodThoughts.size());
        return goodThoughts.get(index);
    }
}

