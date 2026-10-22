package br.com.chatia.repository;

import br.com.chatia.config.Database;
import br.com.chatia.model.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.List;

public class DocumentRepository {
    private final MongoCollection<org.bson.Document> collection =
            Database.getDatabase().getCollection("documents");

    public void save(Document d) {
        collection.insertOne(new org.bson.Document("_id", d.getId())
                .append("title", d.getTitle())
                .append("content", d.getContent())
                .append("keywords", d.getKeywords())
                .append("createdAt", d.getCreatedAt().toString())
                .append("updatedAt", d.getUpdatedAt().toString()));
    }

    public List<Document> findByKeyword(String keyword) {
        List<Document> result = new ArrayList<>();
        for (org.bson.Document d : collection.find(Filters.or(
                Filters.regex("title", keyword, "i"),
                Filters.regex("content", keyword, "i"),
                Filters.regex("keywords", keyword, "i")))) {
            Document x = new Document();
            x.setId(d.getObjectId("_id"));
            x.setTitle(d.getString("title"));
            x.setContent(d.getString("content"));
            x.setKeywords((List<String>) d.get("keywords"));
            x.setCreatedAt(java.time.Instant.parse(d.getString("createdAt")));
            x.setUpdatedAt(java.time.Instant.parse(d.getString("updatedAt")));
            result.add(x);
        }
        return result;
    }
}
