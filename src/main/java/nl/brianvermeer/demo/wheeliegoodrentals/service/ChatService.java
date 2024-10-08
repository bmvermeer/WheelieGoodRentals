package nl.brianvermeer.demo.wheeliegoodrentals.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    List<String> chatHistory = new ArrayList<>();


    public String getResponse(String message) {
        chatHistory.add("User:" + message);


        var response =  "I'm sorry, I can't do that.";
        chatHistory.add("Bot:" + response);
        return response;

    }

    public List<String> getChatHistory() {
        return chatHistory;
    }
}
