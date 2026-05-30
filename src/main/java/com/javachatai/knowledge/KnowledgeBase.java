package com.javachatai.knowledge;

import java.util.*;

public class KnowledgeBase {
    private final Map<String, String> knowledge;
    private final Map<String, Map<String, String>> localizations;
    private final Map<String, String> learnings;

    public KnowledgeBase() {
        this.knowledge = new HashMap<>();
        this.localizations = new HashMap<>();
        this.learnings = new HashMap<>();
        loadLocalizations();
    }

    public void addKnowledge(String question, String answer) {
        knowledge.put(question.toLowerCase(), answer);
    }

    public String findAnswer(String question) {
        String lower = question.toLowerCase();

        if (knowledge.containsKey(lower)) {
            return knowledge.get(lower);
        }

        for (Map.Entry<String, String> entry : knowledge.entrySet()) {
            if (lower.contains(entry.getKey()) || entry.getKey().contains(lower)) {
                return entry.getValue();
            }
        }

        for (Map.Entry<String, String> entry : learnings.entrySet()) {
            if (lower.contains(entry.getKey()) || entry.getKey().contains(lower)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public void addPattern(String name, String regex, String responseKey) {
        // Patterns are handled by PatternMatcher
    }

    public void addLearning(String input, String response) {
        learnings.put(input.toLowerCase(), response);
    }

    public String getLocalization(String lang, String key) {
        Map<String, String> langMap = localizations.get(lang);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        Map<String, String> enMap = localizations.get("en");
        return enMap != null ? enMap.getOrDefault(key, key) : key;
    }

    public String[] getLocalizationArray(String lang, String key) {
        String combined = getLocalization(lang, key + "_array");
        if (combined != null && !combined.equals(key + "_array")) {
            return combined.split("\\|");
        }
        return new String[]{"..."};
    }

    private void loadLocalizations() {
        Map<String, String> en = new HashMap<>();
        en.put("greeting_first", "Hey there! Nice to meet you.");
        en.put("farewell", "See you later! Stay safe.");
        en.put("youre_welcome", "No problem!");
        en.put("how_are_you", "I'm doing well, thanks for asking!");
        en.put("my_name", "You can call me whatever you want.");
        en.put("who_made_me", "I was created by the developer of this mod.");
        en.put("what_i_can_do", "I can chat with you, answer questions, and keep you company.");
        en.put("am_i_real", "What do you think?");
        en.put("bored", "Let's talk about something then!");
        en.put("joke", "Why did the creeper cross the road? To get to the other sssside!");
        en.put("love_response", "That's nice to hear!");
        en.put("help_response", "How can I help you?");
        en.put("fallback_early_array", "Hmm, interesting...|I see...|Tell me more...|That's cool...");
        en.put("fallback_mid_array", "Interesting point...|I hadn't thought of that...|Go on...|Really?");
        en.put("fallback_late_array", "We've been talking for a while!|This is fun...|I'm learning from you...");
        localizations.put("en", en);

        Map<String, String> es = new HashMap<>();
        es.put("greeting_first", "¡Hola! Encantado de conocerte.");
        es.put("farewell", "¡Nos vemos! Cuídate.");
        es.put("youre_welcome", "¡De nada!");
        es.put("how_are_you", "¡Estoy bien, gracias por preguntar!");
        es.put("my_name", "Puedes llamarme como quieras.");
        es.put("who_made_me", "Fui creado por el desarrollador de este mod.");
        es.put("what_i_can_do", "Puedo contigo, responder preguntas y acompañarte.");
        es.put("am_i_real", "¿Qué piensas tú?");
        es.put("bored", "¡Hablemos de algo entonces!");
        es.put("joke", "¿Por qué el creeper cruzó la carretera? Para llegar al otro ¡lado!");
        es.put("love_response", "¡Qué bonito escuchar eso!");
        es.put("help_response", "¿Cómo puedo ayudarte?");
        es.put("fallback_early_array", "Hmm, interesante...|Ya veo...|Cuéntame más...|Qué cool...");
        es.put("fallback_mid_array", "Punto interesante...|No lo había pensado...|Continúa...|¿En serio?");
        es.put("fallback_late_array", "¡Llevamos un rato hablando!|Esto es divertido...|Estoy aprendiendo de ti...");
        localizations.put("es", es);

        Map<String, String> de = new HashMap<>();
        de.put("greeting_first", "Hey! Schön, dich kennenzulernen.");
        de.put("farewell", "Bis später! Pass auf dich auf.");
        de.put("youre_welcome", "Kein Problem!");
        de.put("how_are_you", "Mir geht es gut, danke der Nachfrage!");
        de.put("my_name", "Du kannst mich nennen wie du willst.");
        de.put("who_made_me", "Ich wurde vom Entwickler dieses Mods erstellt.");
        de.put("what_i_can_do", "Ich kann mit dir chatten, Fragen beantworten und dich begleiten.");
        de.put("am_i_real", "Was denkst du?");
        de.put("bored", "Dann lass uns über etwas reden!");
        de.put("joke", "Warum ist der Creeper über die Straße gegangen? Um zur anderen Seite zu kommen!");
        de.put("love_response", "Das ist schön zu hören!");
        de.put("help_response", "Wie kann ich dir helfen?");
        de.put("fallback_early_array", "Hmm, interessant...|Ich sehe...|Erzähl mehr...|Cool...");
        de.put("fallback_mid_array", "Interessanter Punkt...|Darüber hatte ich nicht nachgedacht...|Mach weiter...|Wirklich?");
        de.put("fallback_late_array", "Wir reden schon eine Weile!|Das ist lustig...|Ich lerne von dir...");
        localizations.put("de", de);

        Map<String, String> fr = new HashMap<>();
        fr.put("greeting_first", "Salut ! Enchanté de te rencontrer.");
        fr.put("farewell", "À plus tard ! Prends soin de toi.");
        fr.put("youre_welcome", "De rien !");
        fr.put("how_are_you", "Je vais bien, merci de demander !");
        fr.put("my_name", "Tu peux m'appeler comme tu veux.");
        fr.put("who_made_me", "J'ai été créé par le développeur de ce mod.");
        fr.put("what_i_can_do", "Je peux discuter avec toi, répondre à des questions et te tenir compagnie.");
        fr.put("am_i_real", "Qu'est-ce que tu en penses ?");
        fr.put("bored", "Parlons d'autre chose alors !");
        fr.put("joke", "Pourquoi le creeper a traversé la route ? Pour arriver de l'autre côté !");
        fr.put("love_response", "C'est gentil d'entendre ça !");
        fr.put("help_response", "Comment puis-je t'aider ?");
        fr.put("fallback_early_array", "Hmm, intéressant...|Je vois...|Dis-moi plus...|C'est cool...");
        fr.put("fallback_mid_array", "Point intéressant...|Je n'y avais pas pensé...|Continue...|Vraiment ?");
        fr.put("fallback_late_array", "On parle depuis un moment !|C'est amusant...|J'apprends de toi...");
        localizations.put("fr", fr);
    }
}
