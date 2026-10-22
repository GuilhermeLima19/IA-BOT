package br.com.chatia.repository;

import br.com.chatia.config.Database;
import br.com.chatia.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Optional;

public class UserRepository {
    private final MongoCollection<Document> collection =
            Database.getDatabase().getCollection("users");

    public Optional<User> findByEmail(String email) {
        Document d = collection.find(Filters.eq("email", email.toLowerCase())).first();
        return d == null ? Optional.empty() : Optional.of(fromDocument(d));
    }

    public boolean existsByEmail(String email) {
        return collection.countDocuments(Filters.eq("email", email.toLowerCase())) > 0;
    }

    public void save(User user) {
        collection.insertOne(new Document("_id", user.getId())
                .append("name", user.getName())
                .append("email", user.getEmail().toLowerCase())
                .append("passwordHash", user.getPasswordHash())
                .append("createdAt", user.getCreatedAt().toString()));
    }

    public void updatePassword(ObjectId userId, String passwordHash) {
        collection.updateOne(Filters.eq("_id", userId),
                new Document("$set", new Document("passwordHash", passwordHash)));
    }

    private User fromDocument(Document d) {
        User u = new User();
        u.setId(d.getObjectId("_id"));
        u.setName(d.getString("name"));
        u.setEmail(d.getString("email"));
        u.setPasswordHash(d.getString("passwordHash"));
        u.setCreatedAt(Instant.parse(d.getString("createdAt")));
        return u;
    }
}
