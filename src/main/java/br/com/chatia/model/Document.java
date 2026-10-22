package br.com.chatia.model;

import org.bson.types.ObjectId;
import java.time.Instant;
import java.util.List;

public class Document {
    private ObjectId id;
    private String title;
    private String content;
    private List<String> keywords;
    private Instant createdAt;
    private Instant updatedAt;

    public Document() {}

    public Document(String title, String content, List<String> keywords) {
        this.id = new ObjectId();
        this.title = title;
        this.content = content;
        this.keywords = keywords;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
