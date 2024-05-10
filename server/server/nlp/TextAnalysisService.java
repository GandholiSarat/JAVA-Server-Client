package server.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * This class provides a service for performing text analysis using OpenNLP.
 * It identifies persons' names in the input text.
 * 
 * @author Sarat
 */
public class TextAnalysisService {
    
    private NameFinderME nameFinder;
    
    /**
     * Constructs a TextAnalysisService and initializes the NameFinderME with the provided model.
     * 
     * @throws IOException if there's an error loading the NER model
     */
    public TextAnalysisService() throws IOException {
        try {
            // Load the NER model
            InputStream modelIn = new FileInputStream("en-ner-person.bin");  
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            modelIn.close();
        
            // Initialize the name finder
            nameFinder = new NameFinderME(model);
        } catch (IOException e) {
            // If an error occurs during initialization, propagate the exception
            throw new IOException("Error initializing TextAnalysisService", e);
        }
    }
    
    /**
     * Finds names in the given array of tokens.
     * 
     * @param tokens the array of tokens
     * @return an array of names found in the tokens
     */
    private String[] findNames(String[] tokens) {
        // Find names in the given text
        Span[] nameSpans = nameFinder.find(tokens);
        String[] names = Span.spansToStrings(nameSpans, tokens);
        return names;
    }
    
    /**
     * Performs NLP (Natural Language Processing) on the input text.
     * 
     * @param text the input text
     * @return a string containing the names found in the text
     */
    public String nlp(String text) {
        try {
            TextAnalysisService analysisService = new TextAnalysisService();
            String[] tokens = text.split(" ");
            
            // Find names in the text
            String[] names = analysisService.findNames(tokens);
            
            return ("Names found: " + Arrays.toString(names));
        } catch (IOException e) {
            // If an error occurs during NLP processing, print the stack trace and return an error message
            e.printStackTrace();
            return ("Error in finding names: " + e.getMessage());
        }
    }
}

