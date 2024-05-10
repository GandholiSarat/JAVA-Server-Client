package server.internetsearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * GoogleSearch class for searching Google and retrieving search results.Uses Jsoup library for web scraping.
 * @author Sarat
 */
public class GoogleSearch {

/**
 * Searches Google for the given query and retrieves the content of the first search result, limited to 200 words.
 *
 * @param query The search query.
 * @return The content of the first search result (limited to 200 words), or an error message if no results found or an error occurred.
 */
public String search(String query) {
    StringBuilder searchResult = new StringBuilder();

    try {
        // Constructing the Google search URL
        String googleSearchUrl = "https://www.google.com/search?q=" + query;

        // Fetching and parsing the Google search results
        Document doc = Jsoup.connect(googleSearchUrl).get();

        // Selecting the first search result element
        Element firstResult = doc.selectFirst("div.g");

        if (firstResult != null) {
            // Extracting title and URL of the first search result
            String title = firstResult.select("h3").text();
            String url = firstResult.select("a[href]").attr("href");

            try {
                // Fetching content from the first result URL
                Document resultDoc = Jsoup.connect(url).get();
                String content = resultDoc.text();

                // Limiting content to 200 words
                String[] words = content.split("\\s+");
                StringBuilder limitedContent = new StringBuilder();
                int wordCount = 0;
                for (String word : words) {
                    if (wordCount >= 200) {
                        break;
                    }
                    limitedContent.append(word).append(" ");
                    wordCount++;
                }

                // Appending limited content to the search result
                searchResult.append("Content: ").append(limitedContent).append("...");
            } catch (IOException e) {
                // Error fetching content from the URL
                searchResult.append("Error fetching content from URL: ").append(url).append("\n");
            }
        } else {
            // No search results found
            searchResult.append("No search results found.");
        }
    } catch (IOException e) {
        // Error fetching or parsing Google search results
        searchResult.append("Error fetching or parsing Google search results.");
        e.printStackTrace();
    }

    return searchResult.toString();
}
}

