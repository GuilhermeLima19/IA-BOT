package br.com.chatia.view;

import br.com.chatia.service.PasswordRecoveryService;
import br.com.chatia.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotPasswordView {
    private final PasswordRecoveryService service = new PasswordRecoveryService();

    public void show(Stage stage) {
        Label title = new Label("Recuperação de senha");
        title.getStyleClass().add("title");

        TextField email = new TextField();
        email.setPromptText("E-mail cadastrado");

        Button generate = new Button("Gerar token");
        Button back = new Button("Voltar");

        generate.setMaxWidth(Double.MAX_VALUE);
        back.setMaxWidth(Double.MAX_VALUE);

        generate.setOnAction(e -> {
            try {
                String token = service.createToken(email.getText());
                AlertUtil.info("Token de demonstração gerado:\n\n" + token +
                        "\n\nEm produção, este token deve ser enviado por e-mail.");
            } catch (Exception ex) {
                AlertUtil.error(ex.getMessage());
            }
        });

        back.setOnAction(e -> new LoginView().show(stage));

        VBox box = new VBox(14, title, email, generate, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setMaxWidth(400);

        Scene scene = new Scene(box, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChatIA — Recuperação");
        stage.show();
    }
}
