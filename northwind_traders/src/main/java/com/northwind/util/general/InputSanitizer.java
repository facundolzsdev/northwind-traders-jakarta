package com.northwind.util.general;

import lombok.experimental.UtilityClass;

/**
 * Utility class for sanitizing user input to ensure consistent and safe data storage.
 */
@UtilityClass
public class InputSanitizer {

    /**
     * Sanitizes general text input by:
     * <ul>
     *     <li>Trimming leading and trailing spaces.</li>
     *     <li>Collapsing multiple consecutive spaces into a single space.</li>
     *     <li>Removing all non-letter characters except spaces.</li>
     *     <li>Capitalizing the first letter of each word.</li>
     * </ul>
     *
     * @param input The raw user input.
     * @return A sanitized and formatted string, or {@code null} if input is {@code null}.
     */
    public static String sanitizeText(String input) {
        if (input == null) return null;
        String cleaned = input.trim().replaceAll("\\s+", " ");
        cleaned = cleaned.replaceAll("[^\\p{L} ]", "");
        return capitalizeWords(cleaned);
    }

    /**
     * Sanitizes alphanumeric input by:
     * <ul>
     *     <li>Trimming leading and trailing spaces.</li>
     *     <li>Collapsing multiple consecutive spaces into a single space.</li>
     *     <li>Allowing letters, numbers, spaces, commas, and periods.</li>
     *     <li>Capitalizing the first letter of each word, leaving punctuation untouched.</li>
     * </ul>
     *
     * <p>This method is suitable for flexible fields such as addresses, units of measure,
     * or other free-form alphanumeric data where punctuation is permitted.</p>
     *
     * @param input The raw user input.
     * @return A sanitized and formatted string, or {@code null} if input is {@code null}.
     */
    public static String sanitizeAlphaNumericInput(String input) {
        if (input == null) return null;
        String cleaned = input.trim().replaceAll("\\s+", " ");
        cleaned = cleaned.replaceAll("[^\\p{L}\\p{N} ,\\.]", "");
        return capitalizeWords(cleaned);
    }

    /**
     * Capitalizes the first letter of each word in a string
     * and converts the rest of the characters to lowercase.
     * Punctuation and numbers are left intact.
     *
     * @param str The input string.
     * @return The capitalized string.
     */
    private static String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                char firstChar = word.charAt(0);
                if (Character.isLetter(firstChar)) {
                    result.append(Character.toUpperCase(firstChar));
                    if (word.length() > 1) {
                        result.append(word.substring(1).toLowerCase());
                    }
                } else {
                    result.append(word);
                }
                result.append(" ");
            }
        }
        return result.toString().trim();
    }
}