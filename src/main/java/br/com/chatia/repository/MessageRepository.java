package br.com.chatia.repository;

import br.com.chatia.config.Database;
import br.com.chatia.model.Message;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    private final MongoCollection<Document> collection =
            Database.getDatabase().getCollection("messages");

    public void save(Message m) {
        collection.insertOne(new Document("_id", m.getId())
                .append("userId", m.getUserId())
                .append("conversationId", m.getConversationId())
                .append("author", m.getAuthor())
                .append("content", m.getContent())
                .append("createdAt", m.getCreatedAt().toString()));
    }

    public List<Message> findByConversation(ObjectId conversationId, ObjectId userId) {
        List<Message> list = new ArrayList<>();
        for (Document d : collection.find(Filters.and(
                Filters.eq("conversationId", conversationId),
                Filters.eq("userId", userId))).sort(Sorts.ascending("createdAt"))) {
            Message m = new Message();
            m.setId(d.getObjectId("_id"));
            m.setUserId(d.getObjectId("userId"));
            m.setConversationId(d.getObjectId("conversationId"));
            m.setAuthor(d.getString("author"));
            m.setContent(d.getString("content"));
            m.setCreatedAt(Instant.parse(d.getString("createdAt")));
            list.add(m);
        }
        return list;
    }
}
