import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.UUID;

@Path("/api/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    private static final Logger LOG = Logger.getLogger(ChatResource.class);

    @Inject
    AIChatService chatService;

    @POST
    public Response createNewSession() {
        String sessionId = UUID.randomUUID().toString();
        LOG.info("Created new chat session: " + sessionId);
        return Response.ok().entity("{\"sessionId\":\"" + sessionId + "\"}").build();
    }

    @POST
    @Path("/{sessionId}/message")
    public Response sendMessage(@PathParam("sessionId") String sessionId, ChatMessage message) {
        LOG.info("Received message in session " + sessionId + ": " + message.getContent());

        ChatMessage response = chatService.processUserMessage(sessionId, message);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{sessionId}")
    public Response endSession(@PathParam("sessionId") String sessionId) {
        LOG.info("Ending chat session: " + sessionId);
        chatService.clearChatHistory(sessionId);
        return Response.noContent().build();
    }
}