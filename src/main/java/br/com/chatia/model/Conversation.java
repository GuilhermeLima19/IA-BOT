package br.com.chatia.model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Conversation {
    private ObjectId id;
    private ObjectId userId; // Isolamento obrigatório por usuário
    private String title;
    private Date createdAt;
    private Date updatedAt;

    public Conversation() {}

    public Conversation(ObjectId userId, String title) {
        this.userId = userId;
        this.title = title;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = new Date();
    }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}