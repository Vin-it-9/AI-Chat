
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.jboss.logging.Logger;

@ServerEndpoint("/chat-socket/{sessionId}")
@ApplicationScoped
public class ChatWebSocket {

    private static final Logger LOG = Logger.getLogger(ChatWebSocket.class);
    private Map<String, Session> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    AIChatService chatService;

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        LOG.info("WebSocket session opened: " + sessionId);
        sessions.put(sessionId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("sessionId") String sessionId) {
        LOG.info("WebSocket session closed: " + sessionId);
        sessions.remove(sessionId);
    }

    @OnError
    public void onError(Session session, @PathParam("sessionId") String sessionId, Throwable throwable) {
        LOG.error("WebSocket error in session " + sessionId, throwable);
        sessions.remove(sessionId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sessionId") String sessionId) {
        try {
            LOG.info("Received WebSocket message: " + message);
            ChatMessage userMessage = objectMapper.readValue(message, ChatMessage.class);
            ChatMessage aiResponse = chatService.processUserMessage(sessionId, userMessage);

            Session session = sessions.get(sessionId);
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(objectMapper.writeValueAsString(aiResponse));
            }
        } catch (Exception e) {
            LOG.error("Error processing WebSocket message", e);
        }
    }
}