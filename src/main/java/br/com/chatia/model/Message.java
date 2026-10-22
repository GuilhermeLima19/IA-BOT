package br.com.chatia.model;

import org.bson.types.ObjectId;
import java.time.Instant;

public class Message {
    private ObjectId id;
    private ObjectId userId;
    private ObjectId conversationId;
    private String author;
    private String content;
    private Instant createdAt;

    public Message() {}

    public Message(ObjectId userId, ObjectId conversationId, String author, String content) {
        this.id = new ObjectId();
        this.userId = userId;
        this.conversationId = conversationId;
        this.author = author;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }
    public ObjectId getConversationId() { return conversationId; }
    public void setConversationId(ObjectId conversationId) { this.conversationId = conversationId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
