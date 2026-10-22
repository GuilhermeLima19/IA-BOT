package br.com.chatia.service;

import br.com.chatia.model.Message;
import br.com.chatia.repository.MessageRepository;
import org.bson.types.ObjectId;

import java.util.List;

public class ChatService {
    private final MessageRepository repository = new MessageRepository();
    private final RAGService ragService = new RAGService();
    private final GroqService groqService = new GroqService();

    public String ask(ObjectId userId, ObjectId conversationId, String question) {
        String context = ragService.retrieveContext(question);
        String answer = groqService.ask(question, context);

        repository.save(new Message(userId, conversationId, "user", question));
        repository.save(new Message(userId, conversationId, "assistant", answer));
        return answer;
    }

    public List<Message> history(ObjectId userId, ObjectId conversationId) {
        return repository.findByConversation(conversationId, userId);
    }
}
