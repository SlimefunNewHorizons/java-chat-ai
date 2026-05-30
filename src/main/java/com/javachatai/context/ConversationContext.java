package com.javachatai.context;

import java.util.ArrayList;
import java.util.List;

public class ConversationContext {
    private final List<ChatMessage> messages;
    private int turnCount;
    private String partnerName;
    private String lastTopic;
    private long lastInteractionTime;

    public ConversationContext() {
        this.messages = new ArrayList<>();
        this.turnCount = 0;
        this.lastInteractionTime = System.currentTimeMillis();
    }

    public void addMessage(String content, boolean isUser) {
        messages.add(new ChatMessage(content, isUser));
        if (messages.size() > 50) {
            messages.remove(0);
        }
        this.lastInteractionTime = System.currentTimeMillis();
    }

    public void incrementTurnCount() {
        turnCount++;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getLastTopic() {
        return lastTopic;
    }

    public void setLastTopic(String lastTopic) {
        this.lastTopic = lastTopic;
    }

    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public String getLastUserMessage() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).isUser()) {
                return messages.get(i).getContent();
            }
        }
        return null;
    }

    public String getLastBotMessage() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (!messages.get(i).isUser()) {
                return messages.get(i).getContent();
            }
        }
        return null;
    }

    public long getTimeSinceLastInteraction() {
        return System.currentTimeMillis() - lastInteractionTime;
    }

    public boolean isStale() {
        return getTimeSinceLastInteraction() > 300000;
    }

    public void clear() {
        messages.clear();
        turnCount = 0;
        partnerName = null;
        lastTopic = null;
        lastInteractionTime = System.currentTimeMillis();
    }

    public static class ChatMessage {
        private final String content;
        private final boolean isUser;
        private final long timestamp;

        public ChatMessage(String content, boolean isUser) {
            this.content = content;
            this.isUser = isUser;
            this.timestamp = System.currentTimeMillis();
        }

        public String getContent() { return content; }
        public boolean isUser() { return isUser; }
        public long getTimestamp() { return timestamp; }
    }
}
