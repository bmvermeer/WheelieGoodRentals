package nl.brianvermeer.demo.wheeliegoodrentals.chat;

import nl.brianvermeer.demo.wheeliegoodrentals.chat.ai.Assistant;

import java.util.List;

public class ChatContext {
    private Conversation conversation;
    Assistant assistant;

    public ChatContext(Assistant assistant) {
        this.assistant = assistant;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<ChatMessage> getHistory() {
        return conversation.getMessages();
    }

    public void addMessage(ChatMessage message) {
        conversation.getMessages().add(message);
    }
}
