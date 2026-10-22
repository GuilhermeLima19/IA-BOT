package br.com.chatia.view;

import br.com.chatia.model.Conversation;
import br.com.chatia.model.Message;
import br.com.chatia.service.ChatService;
import br.com.chatia.service.ConversationService;
import br.com.chatia.util.AlertUtil;
import br.com.chatia.util.Session;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ChatView {
    private final ConversationService conversationService = new ConversationService();
    private final ChatService chatService = new ChatService();

    private final ListView<Conversation> conversations = new ListView<>();
    private final VBox messagesBox = new VBox(12);
    private final TextArea input = new TextArea();
    private Conversation currentConversation;

    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        VBox sidebar = createSidebar(stage);
        VBox chat = createChatArea();

        root.setLeft(sidebar);
        root.setCenter(chat);

        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("ChatIA — " + Session.getUser().getName());
        stage.show();

        loadConversations();
    }

    private VBox createSidebar(Stage stage) {
        Label title = new Label("Conversas");
        title.getStyleClass().add("subtitle");

        Button newConversation = new Button("+ Nova conversa");
        Button rename = new Button("Renomear");
        Button delete = new Button("Excluir");
        Button logout = new Button("Sair");

        conversations.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Conversation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        conversations.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                currentConversation = selected;
                loadMessages();
            }
        });

        newConversation.setOnAction(e -> {
            String titleText = askText("Nova conversa", "Título:");
            if (titleText != null && !titleText.isBlank()) {
                currentConversation = conversationService.create(Session.getUser().getId(), titleText.trim());
                loadConversations();
                conversations.getSelectionModel().select(currentConversation);
            }
        });

        rename.setOnAction(e -> {
            if (currentConversation == null) return;
            String titleText = askText("Renomear", "Novo título:");
            if (titleText != null && !titleText.isBlank()) {
                conversationService.rename(currentConversation.getId(), Session.getUser().getId(), titleText.trim());
                loadConversations();
            }
        });

        delete.setOnAction(e -> {
            if (currentConversation == null) return;
            if (AlertUtil.confirm("Excluir esta conversa?")) {
                conversationService.delete(currentConversation.getId(), Session.getUser().getId());
                currentConversation = null;
                messagesBox.getChildren().clear();
                loadConversations();
            }
        });

        logout.setOnAction(e -> {
            Session.logout();
            new LoginView().show(stage);
        });

        VBox box = new VBox(12, title, newConversation, conversations, rename, delete, logout);
        box.setPadding(new Insets(20));
        box.setPrefWidth(280);
        VBox.setVgrow(conversations, Priority.ALWAYS);
        return box;
    }

    private VBox createChatArea() {
        Label header = new Label("Chat");
        header.getStyleClass().add("subtitle");

        messagesBox.setPadding(new Insets(20));
        ScrollPane scroll = new ScrollPane(messagesBox);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        input.setPromptText("Digite sua pergunta...");
        input.setWrapText(true);
        input.setPrefRowCount(3);

        Button send = new Button("Enviar");
        send.setOnAction(e -> sendMessage());
        input.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode().toString().equals("ENTER")) sendMessage();
        });

        HBox bottom = new HBox(10, input, send);
        bottom.setAlignment(Pos.CENTER);
        HBox.setHgrow(input, Priority.ALWAYS);

        VBox box = new VBox(12, header, scroll, bottom);
        box.setPadding(new Insets(20));
        return box;
    }

    private void sendMessage() {
        if (currentConversation == null) {
            AlertUtil.error("Crie ou selecione uma conversa primeiro.");
            return;
        }

        String question = input.getText().trim();
        if (question.isBlank()) return;

        input.clear();
        addMessage("Você", question);

        Label loading = new Label("IA está pensando...");
        messagesBox.getChildren().add(loading);

        new Thread(() -> {
            try {
                String answer = chatService.ask(
                        Session.getUser().getId(),
                        currentConversation.getId(),
                        question
                );
                Platform.runLater(() -> {
                    messagesBox.getChildren().remove(loading);
                    addMessage("ChatIA", answer);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    messagesBox.getChildren().remove(loading);
                    AlertUtil.error(ex.getMessage());
                });
            }
        }, "groq-request").start();
    }

    private void loadMessages() {
        messagesBox.getChildren().clear();
        List<Message> list = chatService.history(
                Session.getUser().getId(), currentConversation.getId());
        for (Message m : list)
            addMessage(m.getAuthor().equals("user") ? "Você" : "ChatIA", m.getContent());
    }

    private void addMessage(String author, String content) {
        Label authorLabel = new Label(author);
        authorLabel.getStyleClass().add("message-author");

        Label text = new Label(content);
        text.setWrapText(true);

        VBox bubble = new VBox(5, authorLabel, text);
        bubble.getStyleClass().add("message");
        messagesBox.getChildren().add(bubble);
    }

    private void loadConversations() {
        conversations.getItems().setAll(
                conversationService.list(Session.getUser().getId()));
    }

    private String askText(String title, String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);
        return dialog.showAndWait().orElse(null);
    }
}
