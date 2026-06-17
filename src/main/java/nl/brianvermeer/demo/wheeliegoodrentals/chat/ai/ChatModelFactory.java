package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import nl.brianvermeer.demo.wheeliegoodrentals.config.AppProps;

public class ChatModelFactory {

    private static final String BASE_URL = "http://127.0.0.1:11434/";

    private static final Double TEMPERATURE = 0.3;
    public static final String MODEL_LLAMA_3_3 = "llama3.3";
    public static final String MODEL_LLAMA_3_2 = "llama3.2";
    public static final String MODEL_LLAMA_3_1 = "llama3.1";
    public static final String MODEL_PHI_3 = "phi3";
    public static final String MODEL_GEMMA2 = "gemma2";
    public static final String MODEL_MISTRAL_NEMO = "mistral-nemo";

    private static String API_KEY = AppProps.getRequired("openai.api.key");

    public static ChatModel createOllamaChatModel(String modelName) {
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(modelName)
                .temperature(TEMPERATURE)
                .build();
    }

    public static ChatModel createOpenAiChatModel(OpenAiChatModelName name) {
        return OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .modelName(name)
                .temperature(TEMPERATURE)
                .build();
    }

    public static ChatModel createOpenAiChatModel(String name) {
        return OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .modelName(name)
                .build();
    }

}
