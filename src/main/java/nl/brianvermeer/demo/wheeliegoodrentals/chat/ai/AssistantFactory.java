package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.ChatMessage;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.Conversation;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.ChatMessageRepository;
import nl.brianvermeer.demo.wheeliegoodrentals.service.BookingService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.CarService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
public class AssistantFactory {

    private static final Logger logger = LoggerFactory.getLogger(AssistantFactory.class);

    private ChatModelFactory chatModelFactory;
    private ChatMessageRepository chatMessageRepository;
    private CarService carService;
    private UserService userService;
    private BookingService bookingService;
    private SimpMessagingTemplate messagingTemplate;

    public AssistantFactory(ChatModelFactory chatModelFactory,ChatMessageRepository chatMessageRepository, CarService carService, UserService userService, BookingService bookingService, SimpMessagingTemplate messagingTemplate) {
        this.chatModelFactory = chatModelFactory;
        this.chatMessageRepository = chatMessageRepository;
        this.carService = carService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.messagingTemplate = messagingTemplate;
    }

    public Assistant createAssistant(Conversation conversation) {
        var chatMemory = MessageWindowChatMemory.withMaxMessages(100);
        var messages = chatMessageRepository.findChatMessagesByConversation(conversation);
        logger.info("Creating assistant with {} messages", messages.size());

        for (ChatMessage mes : messages){
            if (mes.getSender().equalsIgnoreCase("user"))
                chatMemory.add(new UserMessage(mes.getContent()));
            if (mes.getSender().equalsIgnoreCase("assistant"))
                chatMemory.add(new AiMessage(mes.getContent()));
        }

        return AiServices.builder(Assistant.class)
//                .chatLanguageModel(chatModelFactory.createOpenAiChatModel())
                .chatLanguageModel(chatModelFactory.createOllamaChatModel(ChatModelFactory.MODEL_LLAMA_3_1))
                .chatMemory(chatMemory)
                .contentRetriever(documentRetriever())
                .tools(new Tools(carService, userService, bookingService, messagingTemplate))
                .build();
    }

    private ContentRetriever documentRetriever() {
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        Path documentPath = Path.of("documents/terms-of-use.txt");

        EmbeddingStore<TextSegment> embeddingStore =
                embededStore(documentPath, embeddingModel);

       return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();
    }

    private EmbeddingStore<TextSegment> embededStore(Path documentPath, EmbeddingModel embeddingModel) {
        DocumentParser documentParser = new TextDocumentParser();
        Document document = loadDocument(documentPath, documentParser);

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);

        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);
        return embeddingStore;
    }
}
