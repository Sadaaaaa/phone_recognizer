package com.example.phone_recognizer.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneCodeEntityHelper {

    public static String getDefaultCountry(String sourceCode) {
        char firstChar = sourceCode.charAt(0);
        return switch (firstChar) {
            case '1' -> "probably North America";
            case '2' -> "probably Africa";
            case '3', '4' -> "probably Europe";
            case '5' -> "probably Latin America";
            case '6' -> "probably Oceania";
            case '7' -> "probably Russia and neighboring regions";
            case '8' -> "probably East, South Asia";
            case '9' -> "probably West, Central, and South Asia";
            default -> null;
        };
    }

    public static List<String> getCountriesWithGroup() {
        List<String> countries = new ArrayList<>();
        countries.add("United States");
        countries.add("Canada");
        countries.add("Tanzania");
        countries.add("Réunion");
        countries.add("Saint Helena");
        countries.add("Portugal");
        countries.add("Finland");
        countries.add("Italy");
        countries.add("Switzerland");
        countries.add("United Kingdom");
        countries.add("Norway");
        countries.add("Falkland Islands");
        countries.add("Netherlands Antilles");
        countries.add("Australia");
        countries.add("New Zealand");
        countries.add("Australian External Territories");
        countries.add("Russia");
        countries.add("Turkey");
        countries.add("India");
        countries.add("Pakistan");
        countries.add("Georgia");

        return countries;
    }
}
