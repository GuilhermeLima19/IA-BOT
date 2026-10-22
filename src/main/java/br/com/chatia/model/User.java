package br.com.chatia.model;

import org.bson.types.ObjectId;

import java.time.Instant;

public class User {
    private ObjectId id;
    private String name;
    private String email;
    private String passwordHash;
    private Instant createdAt;

    public User() {}

    public User(String name, String email, String passwordHash) {
        this.id = new ObjectId();
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
