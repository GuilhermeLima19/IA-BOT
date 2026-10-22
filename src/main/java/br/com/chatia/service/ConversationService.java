package br.com.chatia.service;

import br.com.chatia.model.Conversation;
import br.com.chatia.repository.ConversationRepository;
import org.bson.types.ObjectId;

import java.util.List;

public class ConversationService {
    private final ConversationRepository repository = new ConversationRepository();

    public Conversation create(ObjectId userId, String title) {
        Conversation c = new Conversation(userId, title);
        repository.save(c);
        return c;
    }

    public List<Conversation> list(ObjectId userId) {
        return repository.findByUser(userId);
    }

    public void rename(ObjectId conversationId, ObjectId userId, String title) {
        if (!repository.belongsToUser(conversationId, userId))
            throw new IllegalArgumentException("Conversa não pertence ao usuário.");
        repository.rename(conversationId, userId, title);
    }

    public void delete(ObjectId conversationId, ObjectId userId) {
        if (!repository.belongsToUser(conversationId, userId))
            throw new IllegalArgumentException("Conversa não pertence ao usuário.");
        repository.delete(conversationId, userId);
    }

    public boolean belongsToUser(ObjectId conversationId, ObjectId userId) {
        return repository.belongsToUser(conversationId, userId);
    }
}
