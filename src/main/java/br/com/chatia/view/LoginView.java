package br.com.chatia.view;

import br.com.chatia.model.User;
import br.com.chatia.service.AuthenticationService;
import br.com.chatia.util.AlertUtil;
import br.com.chatia.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private final AuthenticationService auth = new AuthenticationService();

    public void show(Stage stage) {
        Label title = new Label("CHATIA");
        title.getStyleClass().add("title");

        TextField email = new TextField();
        email.setPromptText("E-mail");

        PasswordField password = new PasswordField();
        password.setPromptText("Senha");

        Button login = new Button("Entrar");
        Button register = new Button("Criar conta");
        Button forgot = new Button("Esqueci minha senha");

        login.setMaxWidth(Double.MAX_VALUE);
        register.setMaxWidth(Double.MAX_VALUE);
        forgot.setMaxWidth(Double.MAX_VALUE);

        login.setOnAction(e -> {
            try {
                User user = auth.login(email.getText(), password.getText());
                Session.login(user);
                new ChatView().show(stage);
            } catch (Exception ex) {
                AlertUtil.error(ex.getMessage());
            }
        });

        register.setOnAction(e -> new RegisterView().show(stage));
        forgot.setOnAction(e -> new ForgotPasswordView().show(stage));

        VBox box = new VBox(14, title, email, password, login, register, forgot);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setMaxWidth(380);

        Scene scene = new Scene(box, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChatIA — Login");
        stage.setResizable(true);
        stage.show();
    }
}
