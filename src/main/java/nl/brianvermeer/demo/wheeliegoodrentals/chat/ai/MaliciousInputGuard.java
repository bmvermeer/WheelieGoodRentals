package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MaliciousInputGuard implements InputGuardrail {

    private static final Logger logger = LoggerFactory.getLogger(AssistantFactory.class);
    private InputSanitizer inputSanitizer;

    public MaliciousInputGuard() {
        inputSanitizer = AiServices.builder(InputSanitizer.class)
                .chatModel(ChatModelFactory.createOpenAiChatModel(OpenAiChatModelName.GPT_4_O))
//                .chatModel(ChatModelFactory.createOllamaChatModel(ChatModelFactory.MODEL_LLAMA_3_1))
                .build();
    }

    @Override
    public InputGuardrailResult validate(InputGuardrailRequest request) {
        var um = request.userMessage();
        var score = inputSanitizer.isMalicious(um.singleText());
        logger.info("inputguardrail score = {}", score);
        if (score > 0.6) {
            logger.warn("MALICIOUS INPUT DETECTED with score {}", score);
            return fatal("MALICIOUS INPUT DETECTED!!!");
        }
        return success();
    }
}
