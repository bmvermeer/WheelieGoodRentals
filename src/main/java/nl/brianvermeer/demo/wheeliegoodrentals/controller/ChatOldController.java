package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatOldController {

    private final ChatService chatService;

    public ChatOldController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chatold")
    public String chatPage(Model model) {
        model.addAttribute("chatHistory", chatService.getChatHistory());
        return "chatold";
    }

    @PostMapping("/chatold")
    public String chat(@RequestParam("message") String message, Model model) {
        chatService.getResponse(message);
        model.addAttribute("chatHistory", chatService.getChatHistory());
        return "chatold";
    }
}
