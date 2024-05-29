package com.example.phone_recognizer.helpers;

import java.util.HashMap;
import java.util.Map;

public class PhoneCodeEntityHelper {

    public static String getDefaultArea(String sourceCode) {
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

    public static Map<String, String> getCountriesWithGroupMap() {
        Map<String, String> countries = new HashMap<>();
        countries.put("United States", "1");
        countries.put("Canada", "1");
        countries.put("Tanzania", "255");
        countries.put("Réunion", "262");
        countries.put("Saint Helena", "290");
        countries.put("Portugal", "351");
        countries.put("Finland", "358");
        countries.put("Italy", "39");
        countries.put("Switzerland", "41");
        countries.put("United Kingdom", "44");
        countries.put("Norway", "47");
        countries.put("Falkland Islands", "500");
        countries.put("Netherlands Antilles", "599");
        countries.put("Australia", "61");
        countries.put("New Zealand", "64");
        countries.put("Australian External Territories", "672");
        countries.put("Russia", "7");
        countries.put("Turkey", "90");
        countries.put("India", "91");
        countries.put("Pakistan", "92");
        countries.put("Georgia", "995");

        return countries;
    }
}
