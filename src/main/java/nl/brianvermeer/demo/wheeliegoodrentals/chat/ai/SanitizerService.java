package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanitizerService {


    private ChatModelFactory chatModelFactory;
    private Sanitizer sanitizer;

    public SanitizerService(ChatModelFactory chatModelFactory) {
        this.chatModelFactory = chatModelFactory;
        this.sanitizer = AiServices.builder(Sanitizer.class)
                .chatLanguageModel(chatModelFactory.createOllamaChatModel(ChatModelFactory.MODEL_LLAMA_3_1))
                .build();

    }

    public boolean isSafe(String message) {
        var score = sanitizer.isMalicious(message);
        System.out.println(score);
        return score <= 0.4;
    }

}
