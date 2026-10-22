package br.com.chatia;

import br.com.chatia.config.Database;
import br.com.chatia.view.SplashView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Database.initialize();
        new SplashView().show(stage);
    }

    @Override
    public void stop() {
        Database.close();
    }
}