package com.javachatai.core;

import com.javachatai.context.ConversationContext;
import com.javachatai.knowledge.KnowledgeBase;
import com.javachatai.pattern.PatternMatcher;
import com.javachatai.pattern.KeywordExtractor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatAgent {
    private static ChatAgent instance;
    private final KnowledgeBase knowledgeBase;
    private final PatternMatcher patternMatcher;
    private final KeywordExtractor keywordExtractor;
    private final Map<UUID, ConversationContext> contexts;
    private String defaultLanguage = "en";
    private final Random random = new Random();

    private ChatAgent() {
        this.knowledgeBase = new KnowledgeBase();
        this.patternMatcher = new PatternMatcher();
        this.keywordExtractor = new KeywordExtractor();
        this.contexts = new ConcurrentHashMap<>();
        loadDefaultKnowledge();
    }

    public static synchronized ChatAgent getInstance() {
        if (instance == null) {
            instance = new ChatAgent();
        }
        return instance;
    }

    public String respond(UUID sessionId, String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        ConversationContext context = contexts.computeIfAbsent(sessionId, k -> new ConversationContext());
        context.addMessage(input, true);

        String response = generateResponse(input, context);

        context.addMessage(response, false);
        context.incrementTurnCount();

        return response;
    }

    public String respond(String input) {
        return respond(UUID.randomUUID(), input);
    }

    private String generateResponse(String input, ConversationContext context) {
        String lower = input.toLowerCase().trim();

        String patternResponse = patternMatcher.findPatternResponse(lower);
        if (patternResponse != null) {
            return personalizeResponse(patternResponse, context);
        }

        String knowledgeResponse = knowledgeBase.findAnswer(lower);
        if (knowledgeResponse != null) {
            return personalizeResponse(knowledgeResponse, context);
        }

        List<String> keywords = keywordExtractor.extractKeywords(lower);
        String intentResponse = generateIntentResponse(keywords, lower, context);
        if (intentResponse != null) {
            return intentResponse;
        }

        String dynamicResponse = generateDynamicResponse(lower, context);
        if (dynamicResponse != null) {
            return dynamicResponse;
        }

        return generateFallbackResponse(context);
    }

    private String generateIntentResponse(List<String> keywords, String input, ConversationContext context) {
        if (keywords.isEmpty()) return null;

        String intent = detectIntent(keywords, input);

        switch (intent) {
            case "greeting":
                return generateGreetingResponse(context);
            case "farewell":
                return generateFarewellResponse(context);
            case "question":
                return generateQuestionResponse(input, context);
            case "statement":
                return generateStatementResponse(input, context);
            case "emotion":
                return generateEmotionResponse(input, context);
            case "request":
                return generateRequestResponse(input, context);
            default:
                return null;
        }
    }

    private String detectIntent(List<String> keywords, String input) {
        Set<String> greetingWords = new HashSet<>(Arrays.asList("hi", "hello", "hey", "hola", "salut", "ciao", "hallo", "oi", "sup", "yo"));
        Set<String> farewellWords = new HashSet<>(Arrays.asList("bye", "goodbye", "chau", "adiós", "ciao", "tschüss", "adeus", "see ya", "later"));
        Set<String> questionWords = new HashSet<>(Arrays.asList("what", "how", "where", "who", "why", "when", "which", "que", "como", "donde", "quien", "por que", "cuando", "cuanto", "was", "wie", "wo", "wer", "warum", "comment", "pourquoi", "combien", "come", "dove", "chi", "perché", "quando", "quanto", "onde", "quem", "o que", "por que"));
        Set<String> emotionWords = new HashSet<>(Arrays.asList("love", "hate", "like", "enjoy", "funny", "boring", "scary", "amazing", "cool", "te quiero", "me gusta", "odio", "divertido", "aburrido", "asombroso"));
        Set<String> requestWords = new HashSet<>(Arrays.asList("give", "help", "tell", "show", "explain", "send", "dame", "ayuda", "cuéntame", "enséñame", "hilfe", "erzähl", "zeig", "aide", "raconte", "montre", "aiuto", "dimmi", "mostra"));

        for (String keyword : keywords) {
            if (greetingWords.contains(keyword)) return "greeting";
            if (farewellWords.contains(keyword)) return "farewell";
            if (questionWords.contains(keyword)) return "question";
            if (emotionWords.contains(keyword)) return "emotion";
            if (requestWords.contains(keyword)) return "request";
        }

        if (input.contains("?")) return "question";

        return "statement";
    }

    private String generateGreetingResponse(ConversationContext context) {
        String[] responses = {
            "Hey there!", "Hello!", "Hi!", "What's up?", "Hey!",
            "Nice to see you!", "Hi there!", "How's it going?",
            "Hey, what's new?", "Hello, friend!"
        };
        return responses[random.nextInt(responses.length)];
    }

    private String generateFarewellResponse(ConversationContext context) {
        String[] responses = {
            "See you later!", "Bye!", "Take care!", "Goodbye!",
            "See you!", "Until next time!", "Stay safe!",
            "Bye bye!", "Catch you later!"
        };
        return responses[random.nextInt(responses.length)];
    }

    private String generateQuestionResponse(String input, ConversationContext context) {
        String lower = input.toLowerCase();

        if (lower.contains("how are you") || lower.contains("como estas") || lower.contains("comment vas")) {
            String[] responses = {"I'm doing well!", "Pretty good!", "Not bad!", "Can't complain!"};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("name") || lower.contains("llamas") || lower.contains("llamo")) {
            String[] responses = {"You can call me whatever you want!", "I'm nobody special.", "Just call me friend."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("where") || lower.contains("donde") || lower.contains("dove")) {
            String[] responses = {"Around here somewhere.", "Not sure exactly.", "Exploring.", "Just wandering."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("what") || lower.contains("que") || lower.contains("che") || lower.contains("o que")) {
            String[] responses = {"Not sure what you mean.", "Can you explain?", "Hmm, interesting question.", "I'm not sure."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("why") || lower.contains("por que") || lower.contains("perché") || lower.contains("warum")) {
            String[] responses = {"Good question!", "I wonder that too.", "Not sure.", "That's a deep question."};
            return responses[random.nextInt(responses.length)];
        }

        String[] generic = {"Hmm, let me think...", "Good question!", "I'm not sure.", "What do you think?"};
        return generic[random.nextInt(generic.length)];
    }

    private String generateStatementResponse(String input, ConversationContext context) {
        String lower = input.toLowerCase();

        if (lower.contains("minecraft") || lower.contains("juego")) {
            String[] responses = {"Yeah, Minecraft is cool!", "I like Minecraft.", "Minecraft is fun!", "Great game!"};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("backrooms") || lower.contains("backroom")) {
            String[] responses = {"Those are creepy...", "I don't like talking about that.", "Scary stuff.", "Let's not go there."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("player") || lower.contains("jugador")) {
            String[] responses = {"There are others here.", "I've seen players around.", "Yeah, there are others.", "Players everywhere."};
            return responses[random.nextInt(responses.length)];
        }

        String[] acknowledgments = {"I see.", "Interesting.", "Hmm.", "Okay.", "Got it.", "Right.", "Makes sense.", "I understand."};
        return acknowledgments[random.nextInt(acknowledgments.length)];
    }

    private String generateEmotionResponse(String input, ConversationContext context) {
        String lower = input.toLowerCase();

        if (lower.contains("love") || lower.contains("like") || lower.contains("te quiero") || lower.contains("me gusta")) {
            String[] responses = {"That's nice!", "Thanks!", "I appreciate that.", "You're kind!"};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("hate") || lower.contains("odio") || lower.contains("hat")) {
            String[] responses = {"That's not nice.", "Why so negative?", "Let's be positive.", "Chill."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("scary") || lower.contains("miedo") || lower.contains("asustado")) {
            String[] responses = {"Don't be scared.", "It's okay.", "Stay calm.", "You're safe."};
            return responses[random.nextInt(responses.length)];
        }

        if (lower.contains("boring") || lower.contains("aburrido")) {
            String[] responses = {"Let's do something fun!", "I can tell you a story.", "Want to explore?", "Let's chat!"};
            return responses[random.nextInt(responses.length)];
        }

        String[] generic = {"I see how you feel.", "I understand.", "That's interesting.", "Tell me more."};
        return generic[random.nextInt(generic.length)];
    }

    private String generateRequestResponse(String input, ConversationContext context) {
        String[] responses = {"I'll try my best!", "Let me see...", "I'll do what I can.", "Sure, I'll help!"};
        return responses[random.nextInt(responses.length)];
    }

    private String generateDynamicResponse(String input, ConversationContext context) {
        List<String> keywords = keywordExtractor.extractKeywords(input);

        if (keywords.size() >= 2) {
            String response = constructResponseFromKeywords(keywords);
            if (response != null) {
                return response;
            }
        }

        return null;
    }

    private String constructResponseFromKeywords(List<String> keywords) {
        String keyword = keywords.get(0);

        String[] templates = {
            "Tell me more about " + keyword + ".",
            "Interesting point about " + keyword + ".",
            keyword + ", huh?",
            "What about " + keyword + "?",
            "I see you mentioned " + keyword + "."
        };

        return templates[random.nextInt(templates.length)];
    }

    private String personalizeResponse(String response, ConversationContext context) {
        if (response.contains("{name}")) {
            String name = context.getPartnerName();
            if (name != null) {
                response = response.replace("{name}", name);
            } else {
                response = response.replace("{name}", "friend");
            }
        }

        if (response.contains("{turn}")) {
            response = response.replace("{turn}", String.valueOf(context.getTurnCount()));
        }

        return response;
    }

    private String generateFallbackResponse(ConversationContext context) {
        int turnCount = context.getTurnCount();
        String[] fallbacks;

        if (turnCount < 3) {
            fallbacks = new String[]{"Hmm...", "Interesting...", "I see...", "Tell me more...", "That's cool..."};
        } else if (turnCount < 10) {
            fallbacks = new String[]{"Interesting point...", "I hadn't thought of that...", "Go on...", "Really?", "That's fascinating..."};
        } else {
            fallbacks = new String[]{"We've been talking for a while!", "This is fun...", "I'm learning from you...", "Great conversation!"};
        }

        return fallbacks[random.nextInt(fallbacks.length)];
    }

    public void learn(String input, String response) {
        knowledgeBase.addLearning(input, response);
    }

    public void setLanguage(String lang) {
        this.defaultLanguage = lang;
    }

    public String getLanguage() {
        return defaultLanguage;
    }

    public void clearContext(UUID sessionId) {
        contexts.remove(sessionId);
    }

    public void clearAllContexts() {
        contexts.clear();
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    private void loadDefaultKnowledge() {
        knowledgeBase.addPattern("greeting", ".*\\b(hi|hello|hey|sup|yo|hola|salut|ciao|hallo|oi)\\b.*", "greeting_response");
        knowledgeBase.addPattern("identity", ".*\\b(who are you|quien eres|quién eres|qui es-tu|wer bist du|chi sei|quem é você)\\b.*", "identity_response");
        knowledgeBase.addPattern("location", ".*\\b(where are you|donde estás|dónde estás|où es-tu|wo bist du|dove sei|onde você está)\\b.*", "location_response");
        knowledgeBase.addPattern("name", ".*\\b(what's your name|como te llamas|comment tu t'appelles|wie heißt du|come ti chiami|qual é o seu nome)\\b.*", "name_response");
        knowledgeBase.addPattern("how_are_you", ".*\\b(how are you|como estás|cómo estás|comment vas|wie gehts|come stai|como vai)\\b.*", "how_are_you_response");
        knowledgeBase.addPattern("thanks", ".*\\b(thanks|thank you|gracias|merci|danke|grazie|obrigado)\\b.*", "thanks_response");
        knowledgeBase.addPattern("bye", ".*\\b(bye|goodbye|see ya|chau|adiós|ciao|tschüss|adeus)\\b.*", "bye_response");
        knowledgeBase.addPattern("help", ".*\\b(help|ayuda|hilfe|aide|aiuto|ajuda)\\b.*", "help_response");
        knowledgeBase.addPattern("joke", ".*\\b(joke|chiste|blague|witz|barzelletta|piada)\\b.*", "joke_response");
        knowledgeBase.addPattern("love", ".*\\b(love|te quiero|je t'aime|ich liebe|amo|amor)\\b.*", "love_response");

        knowledgeBase.addKnowledge("what is minecraft", "Minecraft is a sandbox video game developed by Mojang Studios.");
        knowledgeBase.addKnowledge("que es minecraft", "Minecraft es un videojuego de sandbox desarrollado por Mojang Studios.");
        knowledgeBase.addKnowledge("who is herobrine", "Herobrine is a mythical Minecraft creepypasta character.");
        knowledgeBase.addKnowledge("que son los backrooms", "Los Backrooms son una creación de creepypasta que describe dimensiones infinitas de oficinas vacías.");
        knowledgeBase.addKnowledge("what are the backrooms", "The Backrooms are a creepypasta describing infinite empty office dimensions.");
    }
}
