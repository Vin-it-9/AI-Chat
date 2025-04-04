
import java.util.UUID;

public class ChatMessage {

    private String id;
    private String content;
    private String sender;
    private String timestamp;

    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = java.time.Instant.now().toString();
    }

    public ChatMessage(String content, String sender) {
        this();
        this.content = content;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}