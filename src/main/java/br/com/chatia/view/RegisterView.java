package br.com.chatia.view;

import br.com.chatia.service.AuthenticationService;
import br.com.chatia.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {
    private final AuthenticationService auth = new AuthenticationService();

    public void show(Stage stage) {
        Label title = new Label("Criar conta");
        title.getStyleClass().add("title");

        TextField name = new TextField();
        name.setPromptText("Nome");

        TextField email = new TextField();
        email.setPromptText("E-mail");

        PasswordField password = new PasswordField();
        password.setPromptText("Senha — mínimo 8 caracteres");

        PasswordField confirmation = new PasswordField();
        confirmation.setPromptText("Confirme a senha");

        Button create = new Button("Cadastrar");
        Button back = new Button("Voltar");

        create.setMaxWidth(Double.MAX_VALUE);
        back.setMaxWidth(Double.MAX_VALUE);

        create.setOnAction(e -> {
            try {
                auth.register(name.getText(), email.getText(), password.getText(), confirmation.getText());
                AlertUtil.info("Cadastro realizado com sucesso.");
                new LoginView().show(stage);
            } catch (Exception ex) {
                AlertUtil.error(ex.getMessage());
            }
        });

        back.setOnAction(e -> new LoginView().show(stage));

        VBox box = new VBox(14, title, name, email, password, confirmation, create, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setMaxWidth(400);

        Scene scene = new Scene(box, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChatIA — Cadastro");
        stage.show();
    }
}
