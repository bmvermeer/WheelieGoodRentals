package nl.brianvermeer.demo.wheeliegoodrentals.repository;

import nl.brianvermeer.demo.wheeliegoodrentals.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
