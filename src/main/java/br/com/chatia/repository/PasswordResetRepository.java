package br.com.chatia.repository;

import br.com.chatia.config.Database;
import br.com.chatia.model.PasswordResetToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Optional;

public class PasswordResetRepository {
    private final MongoCollection<Document> collection =
            Database.getDatabase().getCollection("password_reset_tokens");

    public void save(PasswordResetToken t) {
        collection.insertOne(new Document("_id", t.getId())
                .append("userId", t.getUserId())
                .append("tokenHash", t.getTokenHash())
                .append("expiresAt", t.getExpiresAt().toString())
                .append("used", false));
    }

    public Optional<PasswordResetToken> findValid(ObjectId userId, String tokenHash) {
        Document d = collection.find(Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("tokenHash", tokenHash),
                Filters.eq("used", false))).first();

        if (d == null) return Optional.empty();

        PasswordResetToken t = new PasswordResetToken();
        t.setId(d.getObjectId("_id"));
        t.setUserId(d.getObjectId("userId"));
        t.setTokenHash(d.getString("tokenHash"));
        t.setExpiresAt(Instant.parse(d.getString("expiresAt")));
        t.setUsed(d.getBoolean("used"));
        return t.getExpiresAt().isAfter(Instant.now()) ? Optional.of(t) : Optional.empty();
    }

    public void markUsed(ObjectId id) {
        collection.updateOne(Filters.eq("_id", id),
                new Document("$set", new Document("used", true)));
    }
}
