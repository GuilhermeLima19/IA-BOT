package br.com.chatia.view;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashView {
    public void show(Stage stage) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8">
                <script src="https://cdn.jsdelivr.net/npm/gsap@3.13.0/dist/gsap.min.js"></script>
                <style>
                    *{box-sizing:border-box}
                    body{margin:0;background:#07111f;color:white;font-family:Arial,sans-serif;
                         height:100vh;display:flex;align-items:center;justify-content:center;overflow:hidden}
                    .wrap{text-align:center}
                    .logo{width:110px;height:110px;border:2px solid #42a5ff;border-radius:50%;
                          margin:auto;display:flex;align-items:center;justify-content:center;
                          font-size:38px;font-weight:900;box-shadow:0 0 35px #1685ff}
                    h1{opacity:0;letter-spacing:6px;margin:25px 0 8px}
                    p{opacity:0;color:#8fb8dd;margin:0}
                    .bar{width:260px;height:4px;background:#17263b;margin:28px auto 0;overflow:hidden}
                    .load{height:100%;width:0;background:#42a5ff;box-shadow:0 0 15px #42a5ff}
                </style>
                </head>
                <body>
                  <div class="wrap">
                    <div class="logo" id="logo">AI</div>
                    <h1 id="title">CHATIA</h1>
                    <p id="sub">Inteligência artificial desktop</p>
                    <div class="bar"><div class="load" id="load"></div></div>
                  </div>
                  <script>
                    const tl = gsap.timeline();
                    tl.from("#logo",{scale:0,rotation:-180,duration:0.9,ease:"back.out(1.7)"})
                      .to("#title",{opacity:1,y:-5,duration:0.6})
                      .to("#sub",{opacity:1,duration:0.4})
                      .to("#load",{width:"100%",duration:1.4,ease:"power2.inOut"})
                      .to(".wrap",{opacity:0,scale:1.08,duration:0.4});
                  </script>
                </body>
                </html>
                """;

        engine.loadContent(html);
        StackPane root = new StackPane(webView);
        stage.setScene(new Scene(root, 1000, 650));
        stage.setTitle("ChatIA");
        stage.setResizable(false);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(4.0));
        pause.setOnFinished(e -> new LoginView().show(stage));
        pause.play();
    }
}
