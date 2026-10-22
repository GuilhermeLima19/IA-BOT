
package br.com.chatia.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroqService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL =
            "openai/gpt-oss-20b";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public String ask(String question, String context) {

        String apiKey = System.getenv("GROQ_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GROQ_API_KEY não configurada. "
                            + "Verifique as variáveis de ambiente."
            );
        }

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Digite uma pergunta antes de enviar."
            );
        }

        try {
            String system = """
                    Você é o ChatIA, um assistente inteligente
                    de uma aplicação desktop.

                    Responda sempre em português brasileiro.
                    Seja claro, educado e objetivo.
                    Organize respostas longas em tópicos quando necessário.

                    Quando houver contexto recuperado, utilize-o
                    como fonte adicional de informação.

                    Se a pergunta depender de informações que não
                    estão presentes no contexto, informe isso
                    claramente. Não invente informações da base.
                    """;

            String contextoSeguro =
                    context == null || context.isBlank()
                            ? "Nenhum contexto adicional disponível."
                            : context;

            String prompt = """
                    CONTEXTO RECUPERADO:
                    %s

                    PERGUNTA DO USUÁRIO:
                    %s
                    """.formatted(contextoSeguro, question);

            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", system);

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(systemMessage);
            messages.add(userMessage);

            Map<String, Object> body = new HashMap<>();
            body.put("model", MODEL);
            body.put("messages", messages);
            body.put("temperature", 0.3);

            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                String detalhe = response.body();

                try {
                    JsonNode erro = mapper.readTree(response.body());
                    JsonNode mensagem = erro.path("error").path("message");

                    if (!mensagem.isMissingNode()
                            && !mensagem.isNull()) {
                        detalhe = mensagem.asText();
                    }
                } catch (Exception ignored) {
                    // Mantém o corpo original se não for JSON válido.
                }

                throw new IllegalStateException(
                        "A Groq retornou HTTP "
                                + response.statusCode()
                                + ". "
                                + detalhe
                );
            }

            JsonNode root = mapper.readTree(response.body());

            JsonNode content = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content");

            if (content.isMissingNode() || content.isNull()) {
                throw new IllegalStateException(
                        "A API respondeu, mas não retornou conteúdo."
                );
            }

            String resposta = content.asText();

            if (resposta.isBlank()) {
                return "A IA retornou uma resposta vazia. "
                        + "Tente novamente.";
            }

            return resposta;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "A consulta à Groq foi interrompida.",
                    e
            );

        } catch (Exception e) {
            if (e instanceof IllegalStateException
                    && e.getMessage() != null
                    && e.getMessage().startsWith("A Groq retornou")) {
                throw (IllegalStateException) e;
            }

            throw new IllegalStateException(
                    "Erro ao consultar a API da Groq: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
