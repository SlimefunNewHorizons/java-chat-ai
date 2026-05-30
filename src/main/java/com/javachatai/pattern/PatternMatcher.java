package com.javachatai.pattern;

import java.util.*;

public class PatternMatcher {
    private final Map<String, PatternEntry> patterns;

    public PatternMatcher() {
        this.patterns = new HashMap<>();
    }

    public void addPattern(String name, String regex, String responseKey) {
        patterns.put(name, new PatternEntry(name, regex, responseKey));
    }

    public String findPatternResponse(String input) {
        for (PatternEntry entry : patterns.values()) {
            if (input.matches(entry.regex)) {
                return entry.responseKey;
            }
        }
        return null;
    }

    public boolean matchesAny(String input) {
        return patterns.values().stream()
            .anyMatch(entry -> input.matches(entry.regex));
    }

    public List<String> getMatchingPatterns(String input) {
        List<String> matches = new ArrayList<>();
        for (PatternEntry entry : patterns.values()) {
            if (input.matches(entry.regex)) {
                matches.add(entry.name);
            }
        }
        return matches;
    }

    public double calculateSimilarity(String input1, String input2) {
        String[] words1 = input1.toLowerCase().split("\\s+");
        String[] words2 = input2.toLowerCase().split("\\s+");

        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    private static class PatternEntry {
        final String name;
        final String regex;
        final String responseKey;

        PatternEntry(String name, String regex, String responseKey) {
            this.name = name;
            this.regex = regex;
            this.responseKey = responseKey;
        }
    }
}
