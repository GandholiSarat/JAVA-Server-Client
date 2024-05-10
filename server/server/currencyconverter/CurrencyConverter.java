package server.currencyconverter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * This class provides functionality to convert currency using an external API.
 * The API key needs to be set for accessing the currency conversion service.
 * 
 * @author Sarat
 */
public class CurrencyConverter {
    private final String API_KEY = "31307f07be68b25e1186aff3"; // Replace with your API key

    /**
     * Converts an amount from one currency to another.
     * 
     * @param fromCurrency The currency to convert from.
     * @param toCurrency The currency to convert to.
     * @param amount The amount to convert.
     * @return The converted amount.
     * @throws IOException If an I/O error occurs while making the API request.
     * @throws CurrencyConversionException If there is an error in currency conversion.
     */
    public double convertCurrency(String fromCurrency, String toCurrency, double amount) throws IOException, CurrencyConversionException {
        String urlString = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + fromCurrency;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new CurrencyConversionException("Failed to fetch exchange rates. HTTP error code: " + responseCode);
        }

        InputStream inputStream = conn.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        String jsonResponse = response.toString();

        // Parse JSON response
        double exchangeRate = parseExchangeRate(jsonResponse, toCurrency);

        if (exchangeRate == 0.0) {
            throw new CurrencyConversionException("Failed to parse exchange rate for currency: " + toCurrency);
        }

        return amount * exchangeRate;
    }

    private double parseExchangeRate(String jsonResponse, String toCurrency) {
        // Parse JSON response to extract exchange rate for the target currency
        double exchangeRate = 0.0;
        try {
            // Convert JSON string to JSON object
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract conversion rates object
            JSONObject conversionRates = jsonObject.getJSONObject("conversion_rates");

            // Extract exchange rate for the target currency
            exchangeRate = conversionRates.getDouble(toCurrency);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    /**
     * Custom exception class for currency conversion errors.
     */
    public static class CurrencyConversionException extends Exception {

	/**
	 * Constructor for CurrencyConversionException
     	 * @param message The messgae to display .
	 */
        public CurrencyConversionException(String message) {
            super(message);
        }
    }
}

