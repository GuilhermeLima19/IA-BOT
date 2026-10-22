package br.com.chatia.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class Database {
    private static MongoClient client;
    private static MongoDatabase database;

    private Database() {}

    public static void initialize() {
        String uri = System.getenv("MONGODB_URI");
        if (uri == null || uri.isBlank()) {
            System.err.println("MONGODB_URI não configurada. O aplicativo será iniciado, mas recursos do banco não funcionarão.");
            return;
        }

        try {
            client = MongoClients.create(uri);
            database = client.getDatabase("chatia");
            System.out.println("MongoDB conectado.");
        } catch (Exception e) {
            System.err.println("Falha ao conectar ao MongoDB: " + e.getMessage());
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoDB não está configurado. Defina MONGODB_URI.");
        }
        return database;
    }

    public static void close() {
        if (client != null) client.close();
    }
}
