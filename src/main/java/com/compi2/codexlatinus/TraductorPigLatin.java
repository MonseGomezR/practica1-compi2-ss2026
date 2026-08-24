package com.compi2.codexlatinus;

/**
 *
 * @author Usuario
 */
public class TraductorPigLatin {
    
    private static final String CONSONANTES = "aeiouAEIOU";
    public static String convert(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        // Empieza con vocal
        if (CONSONANTES.indexOf(text.charAt(0)) >= 0) {
            return text + "way";
        }

        // Empieza con consonante(s)
        int firstVowel = 0;

        while (firstVowel < text.length()
                && CONSONANTES.indexOf(text.charAt(firstVowel)) < 0) {
            firstVowel++;
        }

        // No contiene vocales
        if (firstVowel == text.length()) {
            return text + "ay";
        }

        return text.substring(firstVowel)
                + text.substring(0, firstVowel)
                + "ay";
    }
}
