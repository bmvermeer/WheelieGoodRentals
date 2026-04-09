package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import nl.brianvermeer.demo.wheeliegoodrentals.config.AppProps;



public class MaliciousInputGuard implements InputGuardrail {

    private static final String API_KEY = AppProps.getRequired("openai.api.key");
    private InputSanitizer inputSanitizer;

    public MaliciousInputGuard() {
        var chatmodel = OpenAiChatModel.builder()
                    .apiKey(API_KEY)
                    .modelName(OpenAiChatModelName.GPT_4_O)
                    .temperature(0.3)
                    .build();

        inputSanitizer = AiServices.builder(InputSanitizer.class)
                .chatModel(chatmodel)
                .build();
    }


    @Override
    public InputGuardrailResult validate(InputGuardrailRequest request) {
        var um = request.userMessage();
        var score = inputSanitizer.isMalicious(um.singleText());
        if (score > 0.6) {
            System.out.println("MALICIOUS INPUT DETECTED with score " + score);
            return fatal("MALICIOUS INPUT DETECTED!!!");
        }
        return success();
    }
}
