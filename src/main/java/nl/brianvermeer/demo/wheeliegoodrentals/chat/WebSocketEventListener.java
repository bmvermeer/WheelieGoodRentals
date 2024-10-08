package nl.brianvermeer.demo.wheeliegoodrentals.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private ChatController chatController;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);



    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId", String.class);
        if (sessionId != null) {
            chatController.sessions.remove(sessionId);
            logger.info("Session removed: " + sessionId);
        }
        logger.info("--Disconnecting: " + event);
    }


}
