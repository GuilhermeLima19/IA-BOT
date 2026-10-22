package br.com.chatia.service;

import br.com.chatia.model.Document;
import br.com.chatia.repository.DocumentRepository;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class RAGService {
    private final DocumentRepository repository = new DocumentRepository();

    private static final Set<String> STOP_WORDS = Set.of(
            "a","o","as","os","de","da","do","em","para","por","com","uma","um","que",
            "e","é","no","na","nos","nas","ao","aos","se","como","ou","dos","das"
    );

    public List<String> extractKeywords(String text) {
        String normalized = Normalizer.normalize(text.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9\\s]", " ");

        return Arrays.stream(normalized.split("\\s+"))
                .filter(w -> w.length() > 2)
                .filter(w -> !STOP_WORDS.contains(w))
                .distinct()
                .collect(Collectors.toList());
    }

    public String retrieveContext(String question) {
        List<String> keywords = extractKeywords(question);
        if (keywords.isEmpty()) return "";

        StringBuilder context = new StringBuilder();
        for (String keyword : keywords) {
            for (Document d : repository.findByKeyword(keyword)) {
                context.append("Título: ").append(d.getTitle()).append("\n")
                        .append("Conteúdo: ").append(d.getContent()).append("\n\n");
            }
        }

        return context.toString();
    }
}
