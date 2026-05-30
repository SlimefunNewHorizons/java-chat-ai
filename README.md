# JavaChatAI

**Zero-dependency, lightweight conversational AI agent for Java 17+.**

JavaChatAI is an embeddable chat agent library with multi-language intent detection, regex pattern matching, keyword extraction, and persistent conversation context — all with no external dependencies.

## Add as a Dependency

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.javachatai:java-chat-ai:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.javachatai</groupId>
    <artifactId>java-chat-ai</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

```java
import com.javachatai.core.ChatAgent;

ChatAgent agent = ChatAgent.getInstance();

// Session-less response
String reply = agent.respond("Hello!");

// Session-based response (preserves conversation context)
import java.util.UUID;
UUID sessionId = UUID.randomUUID();
reply = agent.respond(sessionId, "How are you?");
```

## Features

| Feature | Description |
|---|---|
| **Intent Detection** | Recognizes greetings, farewells, questions, statements, emotions, and requests across multiple languages |
| **Regex Patterns** | Predefined and custom regex-based pattern matching for input classification |
| **Keyword Extraction** | Multi-language keyword extraction with stop-word filtering (EN, ES, DE) |
| **Conversation Context** | Per-session message history, turn tracking, and conversation state |
| **Knowledge Base** | Built-in Q&A with lookup and fuzzy matching |
| **Localization** | Responses in English, Spanish, German, and French |
| **Dynamic Responses** | Template-based keyword-driven response generation |
| **Learning** | `agent.learn(input, response)` to teach new Q&A pairs at runtime |

## Architecture

```
ChatAgent (singleton)
 ├── KnowledgeBase     — Q&A storage, pattern definitions, localizations
 ├── PatternMatcher    — Regex pattern matching and similarity scoring
 ├── KeywordExtractor  — Keyword and named entity extraction
 └── ConversationContext (per session)
      └── ChatMessage history (capped at 50)
```

## Session Management

```java
// Start or continue a session
UUID id = UUID.randomUUID();
agent.respond(id, "Hi there!");

// Clear a session
agent.clearContext(id);

// Clear all sessions
agent.clearAllContexts();
```

## Teach the Agent

```java
agent.learn("what is java", "Java is a programming language.");
agent.respond("Tell me about java"); // → "Java is a programming language."
```

## License

MIT