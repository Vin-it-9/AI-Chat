# AI Chat Assistant

A modern, locally-hosted AI chat application built with Quarkus, Ollama, and LangChain4j. This application provides a web-based interface for interacting with local Large Language Models through Ollama.

## Features

- 🤖 Real-time chat with AI using local language models
- 🔒 Privacy-focused (all processing happens on your machine)
- 💬 Modern, responsive UI with typing animations
- 🔄 WebSocket support for instant communication
- 🌐 RESTful API endpoints as fallback
- 💾 Chat session management
- 🎨 Sleek design with Tailwind CSS

## Prerequisites

- Java 21
- Maven 3.8+
- [Ollama](https://ollama.ai/) - Local LLM serving platform
- An LLM model downloaded through Ollama (e.g., llama2)

## Installation

### 1. Install Ollama

First, install Ollama by following the official instructions at [ollama.ai](https://ollama.ai/download).

### 2. Pull a Language Model

After installing Ollama, pull a language model (this example uses llama2):

```bash
ollama pull llama2
```

### 3. Run Ollama and Start the Model

Ensure Ollama is running and start the model service:
```bash
ollama run llama2
```
You can keep this terminal open while running the application. The model will be available for API calls on port 11434.

### 4. Run the Application

```bash
./mvnw quarkus:dev
```

This will start the application in development mode. The chat interface will be available at http://localhost:8080.

## Configuration

The application can be configured in `src/main/resources/application.properties`:

```properties
# Application configuration
quarkus.application.name=AI Chat Assistant
quarkus.http.port=8080

# Ollama configuration
ollama.base.url=http://localhost:11434  # URL where Ollama is running
ollama.model=llama2                     # Default model to use

# Logging configuration
quarkus.log.console.enable=true
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n
quarkus.log.level=INFO
```
 
### Starting a Chat Session

1. Open your browser and navigate to http://localhost:8080
2. The chat interface will automatically create a new session
3. Type your message in the input field and press Enter or click the send button
4. The AI will respond in real-time with a typing animation

### Chat Commands

You can use the "New Chat" button in the top-right corner to clear the current conversation and start a fresh chat session.

## API Endpoints

If you want to integrate with the chat assistant programmatically:

### Create a new session
```
POST /api/chat
```
Returns: `{"sessionId":"uuid-string"}`

### Send a message
```
POST /api/chat/{sessionId}/message
```
Request body:
```json
{
  "content": "Your message here",
  "sender": "user"
}
```

### End a session
```
DELETE /api/chat/{sessionId}
```

## WebSocket API

For real-time communication:

```javascript
const socket = new WebSocket(`ws://localhost:8080/chat-socket/${sessionId}`);

// Send a message
socket.send(JSON.stringify({
  "content": "Your message here",
  "sender": "user"
}));

// Listen for responses
socket.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log(message.content);
};
```

