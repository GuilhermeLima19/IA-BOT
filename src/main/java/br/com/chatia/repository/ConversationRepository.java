package br.com.chatia.repository;

import br.com.chatia.config.Database;
import br.com.chatia.model.Conversation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationRepository {
    private final MongoCollection<Document> collection =
            Database.getDatabase().getCollection("conversations");

    public void save(Conversation c) {
        Document doc = toDocument(c);
        collection.insertOne(doc);
        c.setId(doc.getObjectId("_id"));
    }

    public List<Conversation> findByUser(ObjectId userId) {
        List<Conversation> list = new ArrayList<>();
        for (Document d : collection.find(Filters.eq("userId", userId))
                .sort(Sorts.descending("updatedAt"))) {
            list.add(fromDocument(d));
        }
        return list;
    }

    public void rename(ObjectId conversationId, ObjectId userId, String title) {
        collection.updateOne(
                Filters.and(Filters.eq("_id", conversationId), Filters.eq("userId", userId)),
                new Document("$set", new Document("title", title).append("updatedAt", Instant.now().toString()))
        );
    }

    public void delete(ObjectId conversationId, ObjectId userId) {
        collection.deleteOne(Filters.and(
                Filters.eq("_id", conversationId),
                Filters.eq("userId", userId)));
    }

    public boolean belongsToUser(ObjectId conversationId, ObjectId userId) {
        return collection.countDocuments(Filters.and(
                Filters.eq("_id", conversationId),
                Filters.eq("userId", userId))) > 0;
    }

    private Document toDocument(Conversation c) {
        Document doc = new Document()
                .append("userId", c.getUserId())
                .append("title", c.getTitle())
                .append("createdAt", c.getCreatedAt() != null ? c.getCreatedAt().toInstant().toString() : Instant.now().toString())
                .append("updatedAt", c.getUpdatedAt() != null ? c.getUpdatedAt().toInstant().toString() : Instant.now().toString());

        if (c.getId() != null) {
            doc.append("_id", c.getId());
        }

        return doc;
    }

    private Conversation fromDocument(Document d) {
        Conversation c = new Conversation();
        c.setId(d.getObjectId("_id"));
        c.setUserId(d.getObjectId("userId"));
        c.setTitle(d.getString("title"));

        // Conversão segura de String ISO para java.util.Date
        try {
            String createdAtStr = d.getString("createdAt");
            if (createdAtStr != null) {
                c.setCreatedAt(Date.from(Instant.parse(createdAtStr)));
            }
            String updatedAtStr = d.getString("updatedAt");
            if (updatedAtStr != null) {
                c.setUpdatedAt(Date.from(Instant.parse(updatedAtStr)));
            }
        } catch (Exception e) {
            c.setCreatedAt(new Date());
            c.setUpdatedAt(new Date());
        }

        return c;
    }
}