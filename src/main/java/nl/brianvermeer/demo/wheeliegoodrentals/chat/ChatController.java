package nl.brianvermeer.demo.wheeliegoodrentals.chat;

import nl.brianvermeer.demo.wheeliegoodrentals.chat.ai.Assistant;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.ai.AssistantFactory;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.ChatMessageRepository;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private ChatMessageRepository chatMessageRepository;
    private ConversationRepository conversationRepository;
    private AssistantFactory assistantFactory;
    private SimpMessagingTemplate messagingTemplate;

    public Map<String, ChatContext> sessions = new HashMap<>();

    public ChatController(ChatMessageRepository chatMessageRepository, ConversationRepository conversationRepository, AssistantFactory assistantFactory, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.conversationRepository = conversationRepository;
        this.assistantFactory = assistantFactory;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message, @Header("simpSessionId") String sessionId, @Header(value = "conversationId", required = false) String conversationId) {
        logger.info("Received message: {}", message);
        logger.info("Session id: {}", sessionId);
        logger.info("Conversation id: {}", conversationId);

        var context = getContext(sessionId, conversationId);

        var assistant = context.assistant;
        String response = "";
        for (int i = 0; i < 3; i++) {
            try {
                response = assistant.answer(message.getContent());
                break;
            } catch (Exception e) {
                logger.error("Operation failed, retrying", e);
            }
            response = "Something went wrong, please try again";
        }
        var botResponse = new ChatMessage("Assistant", response);

        Conversation conversation = context.getConversation();
        message.setConversation(conversation);
        botResponse.setConversation(conversation);
        chatMessageRepository.save(message);
        chatMessageRepository.save(botResponse);

        // Return bot's response
        return botResponse;
    }

    //write me a method that catches an exception and retries the operation 3 times
    @Transactional
    public void retryOperation(int retries, Runnable operation) {
        for (int i = 0; i < retries; i++) {
            try {
                operation.run();
                return;
            } catch (Exception e) {
                logger.error("Operation failed, retrying", e);
            }
        }
        throw new RuntimeException("Operation failed after " + retries + " retries");
    }

    //write me a method that catches an exception and retries the operation 3 times


    private ChatContext getContext(String sessionId, String conversationId) {
        var context = sessions.get(sessionId);

        if (context == null) {
            Conversation conversation;
            if (conversationId != null && !conversationId.isEmpty()) {
                conversation = conversationRepository.findById(Long.parseLong(conversationId)).orElse(new Conversation());
            } else {
                conversation = new Conversation();

            }
            conversationRepository.save(conversation);


            var assistant = assistantFactory.createAssistant(conversation);
            context = new ChatContext(assistant);
            context.setConversation(conversation);
            sessions.put(sessionId, context);
        }
        return context;
    }


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "conversationId", required = false, defaultValue = "0") String conversationId, Model model, @Header("simpSessionId") String sessionId) {
        logger.info("Chatting with conversation id: {}", conversationId);

        if (conversationId != null && !conversationId.isEmpty()) {
            Conversation conversation = conversationRepository.findById(Long.parseLong(conversationId)).orElse(null);
            if (conversation != null) {
                List<ChatMessage> messages = conversation.getMessages();
                model.addAttribute("messages", messages);
            }
        }

        List<Conversation> conversations = conversationRepository.findAll();
        model.addAttribute("conversations", conversations);
        model.addAttribute("conversationId", conversationId);
        model.addAttribute("view", "chat");
        return "layout";
    }


}