package nl.brianvermeer.demo.wheeliegoodrentals.repository;

import nl.brianvermeer.demo.wheeliegoodrentals.chat.ChatMessage;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesByConversation(Conversation conversation);
}
