# ChatIA — JavaFX + MongoDB + RAG + Groq

Aplicação desktop de chat com Inteligência Artificial desenvolvida em Java 21 e JavaFX.

## Tecnologias

- Java 21
- JavaFX 21
- Maven
- MongoDB Atlas
- Groq API
- Git/GitHub
- jpackage

## Arquitetura

View -> Controller/Service -> Repository -> MongoDB

A interface inicial deste protótipo concentra a interação nas Views e Services. Para cumprir integralmente a especificação acadêmica, os Controllers podem ser extraídos das Views na próxima etapa.

## Estrutura

```text
src/main/java/br/com/chatia/
├── config
├── model
├── repository
├── service
├── util
└── view
```

## Configuração

Defina as variáveis de ambiente no Windows:

```text
MONGODB_URI=mongodb+srv://USUARIO:SENHA@cluster.mongodb.net/?retryWrites=true&w=majority
GROQ_API_KEY=sua_chave
```

Nunca coloque essas credenciais no GitHub.

## Execução

No Eclipse/Maven:

```bash
mvn clean javafx:run
```

## Banco

Coleções usadas:

- users
- conversations
- messages
- documents
- password_reset_tokens

## RAG

O mecanismo normaliza o texto, remove stop words, extrai palavras relevantes e procura documentos por palavras-chave antes de enviar o contexto à Groq.

## Segurança

- Senhas usam BCrypt.
- API Key vem de variável de ambiente.
- Conversas verificam o `userId`.
- Credenciais não devem ser versionadas.

## Observação

A recuperação de senha deste protótipo gera um token para demonstração. Em uma implementação real, o token deve ser enviado por um canal seguro, como e-mail, e o fluxo de validação deve ser concluído antes da entrega.
