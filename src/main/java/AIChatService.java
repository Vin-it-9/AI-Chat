
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

@ApplicationScoped
public class AIChatService {

    private static final Logger LOG = Logger.getLogger(AIChatService.class);
    private final ConcurrentHashMap<String, List<dev.langchain4j.data.message.ChatMessage>> chatHistories = new ConcurrentHashMap<>();
    private final OllamaConfig ollamaConfig;
    private ChatLanguageModel chatModel;

    @Inject
    public AIChatService(OllamaConfig ollamaConfig) {
        this.ollamaConfig = ollamaConfig;
        initializeChatModel();
    }

    private void initializeChatModel() {
        try {
            this.chatModel = OllamaChatModel.builder()
                    .baseUrl(ollamaConfig.getBaseUrl())
                    .modelName(ollamaConfig.getModelName())
                    .build();
            LOG.info("Successfully initialized Ollama chat model: " + ollamaConfig.getModelName());
        } catch (Exception e) {
            LOG.error("Failed to initialize Ollama chat model", e);
            throw new RuntimeException("Could not initialize Ollama chat model", e);
        }
    }

    public ChatMessage processUserMessage(String sessionId, ChatMessage userMessage) {
        List<dev.langchain4j.data.message.ChatMessage> history = chatHistories.computeIfAbsent(sessionId, k -> {
            List<dev.langchain4j.data.message.ChatMessage> newHistory = new ArrayList<>();
            newHistory.add(new SystemMessage("You are a helpful AI assistant. Provide concise, accurate and friendly responses."));
            return newHistory;
        });

        history.add(new UserMessage(userMessage.getContent()));

        try {

            AiMessage aiMessage = chatModel.generate(history).content();
            history.add(aiMessage);

            return new ChatMessage(aiMessage.text(), "AI");
        } catch (Exception e) {
            LOG.error("Error getting response from Ollama", e);
            return new ChatMessage("Sorry, I encountered an error. Please try again later.", "AI");
        }
    }

    public void clearChatHistory(String sessionId) {
        chatHistories.remove(sessionId);
    }
}