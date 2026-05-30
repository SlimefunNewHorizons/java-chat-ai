package com.javachatai.pattern;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordExtractor {
    private static final Set<String> STOP_WORDS_ES = new HashSet<>(Arrays.asList(
        "el", "la", "los", "las", "un", "una", "unos", "unas", "de", "del", "al",
        "en", "con", "por", "para", "sin", "sobre", "entre", "hasta", "desde",
        "es", "son", "está", "están", "ser", "estar", "hay", "tiene", "tienen",
        "que", "como", "donde", "quien", "cuando", "cuanto", "porque",
        "y", "o", "pero", "sino", "si", "no", "ya", "muy", "mas", "menos",
        "este", "esta", "estos", "estas", "ese", "esa", "esos", "esas",
        "yo", "tu", "el", "ella", "nosotros", "ustedes", "ellos", "ellas",
        "mi", "tu", "su", "nuestro", "vuestro", "este", "ese", "aquel"
    ));

    private static final Set<String> STOP_WORDS_EN = new HashSet<>(Arrays.asList(
        "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
        "of", "with", "by", "from", "is", "are", "was", "were", "be", "been",
        "being", "have", "has", "had", "do", "does", "did", "will", "would",
        "could", "should", "may", "might", "can", "shall", "this", "that",
        "these", "those", "i", "you", "he", "she", "it", "we", "they",
        "my", "your", "his", "her", "its", "our", "their", "what", "which",
        "who", "whom", "where", "when", "why", "how", "if", "then", "else"
    ));

    private static final Set<String> STOP_WORDS_DE = new HashSet<>(Arrays.asList(
        "der", "die", "das", "ein", "eine", "und", "oder", "aber", "in", "an",
        "auf", "für", "mit", "von", "zu", "bei", "nach", "über", "unter",
        "ist", "sind", "war", "waren", "sein", "haben", "hat", "hatte",
        "ich", "du", "er", "sie", "es", "wir", "ihr", "sie", "mein", "dein",
        "sein", "ihr", "unser", "euer", "was", "welcher", "wer", "wo", "wann",
        "warum", "wie", "wenn", "dann", "sonst", "diese", "jene"
    ));

    public List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (String word : words) {
            String cleaned = word.replaceAll("[^a-záéíóúñü]", "");
            if (cleaned.length() < 2) continue;
            if (STOP_WORDS_ES.contains(cleaned)) continue;
            if (STOP_WORDS_EN.contains(cleaned)) continue;
            if (STOP_WORDS_DE.contains(cleaned)) continue;
            keywords.add(cleaned);
        }

        return keywords;
    }

    public String extractMainKeyword(String text) {
        List<String> keywords = extractKeywords(text);
        return keywords.isEmpty() ? null : keywords.get(0);
    }

    public Map<String, String> extractNamedEntities(String text) {
        Map<String, String> entities = new HashMap<>();

        Matcher matcher = Pattern.compile("\\b([A-Z][a-z]+)\\b").matcher(text);
        while (matcher.find()) {
            entities.put(matcher.group(1), "PERSON");
        }

        matcher = Pattern.compile("\\b(Minecraft|Java|Fabric|Forge)\\b").matcher(text);
        while (matcher.find()) {
            entities.put(matcher.group(1), "TECHNOLOGY");
        }

        return entities;
    }
}
