package view;

import application.CookieRunApp;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Home Page: title, Play button, Select Cookie button.
 */
public class HomePageView {

    private final CookieRunApp app;
    private final StackPane    root;

    public HomePageView(CookieRunApp app) {
        this.app = app;

        Canvas bg = new Canvas(CookieRunApp.WIDTH, CookieRunApp.HEIGHT);
        drawBg(bg.getGraphicsContext2D(), 0);

        // Animate background
        final double[] off = {0};
        new AnimationTimer() {
            @Override public void handle(long now) {
                off[0] = (off[0] + 1.5) % CookieRunApp.WIDTH;
                drawBg(bg.getGraphicsContext2D(), off[0]);
            }
        }.start();

        // Title
        Label sub = new Label("VinZaynt's");
        sub.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        sub.setTextFill(Color.web("#FFD580"));

        Label title = new Label("COOKIE RUN");
        title.setFont(Font.font("Impact", FontWeight.BOLD, 62));
        title.setTextFill(Color.web("#FF6B00"));
        title.setStyle("-fx-effect: dropshadow(gaussian,#FF3300,14,0.5,3,3);");

        VBox titleBox = new VBox(0, sub, title);
        titleBox.setAlignment(Pos.CENTER);

        // Play button
        Button play = new Button("▶  PLAY");
        play.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#FF8C00,#FF4500);
            -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
            -fx-padding: 12 52; -fx-background-radius: 30; -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian,#000,8,0.4,2,3);
            """);
        play.setOnAction(e -> app.startGame(app.getSelectedCookieIndex()));

        // Select Cookie button
        Button select = new Button("🍪  Select Cookie");
        select.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#7B2FBE,#4A1080);
            -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;
            -fx-padding: 10 36; -fx-background-radius: 30; -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian,#000,6,0.3,1,2);
            """);
        select.setOnAction(e -> app.showCookieSelect());

        VBox content = new VBox(20, titleBox, play, select);
        content.setAlignment(Pos.CENTER);

        root = new StackPane(bg, content);
    }

    private void drawBg(GraphicsContext gc, double off) {
        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1A0A3C")),
                new Stop(1, Color.web("#8B2FC9"))));
        gc.fillRect(0, 0, CookieRunApp.WIDTH, CookieRunApp.HEIGHT);

        gc.setFill(Color.web("#3D1A6E"));
        gc.fillRect(0, CookieRunApp.HEIGHT - 80, CookieRunApp.WIDTH, 80);

        gc.setFill(Color.web("#5A2A9A", 0.4));
        for (int i = -1; i < 14; i++)
            gc.fillRect(i * 60 - off % 60, CookieRunApp.HEIGHT - 80, 30, 80);

        java.util.Random r = new java.util.Random(42);
        for (int i = 0; i < 40; i++) {
            gc.setFill(Color.color(1, 1, 1, 0.3 + r.nextDouble() * 0.6));
            gc.fillOval(r.nextDouble() * CookieRunApp.WIDTH,
                        r.nextDouble() * (CookieRunApp.HEIGHT - 100), 2, 2);
        }
        gc.setGlobalAlpha(1.0);
    }

    public Pane getRoot() { return root; }
}
