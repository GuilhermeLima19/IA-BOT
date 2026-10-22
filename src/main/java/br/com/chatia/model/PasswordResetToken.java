package br.com.chatia.model;

import org.bson.types.ObjectId;
import java.time.Instant;

public class PasswordResetToken {
    private ObjectId id;
    private ObjectId userId;
    private String tokenHash;
    private Instant expiresAt;
    private boolean used;

    public PasswordResetToken() {}

    public PasswordResetToken(ObjectId userId, String tokenHash, Instant expiresAt) {
        this.id = new ObjectId();
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
