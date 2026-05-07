package view;

import application.CookieRunApp;
import model.CookieList;
import util.SpriteSheet;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Cookie Selection Page: pick a cookie, then play or go back.
 * Reads all cookie metadata from CookieList.ALL — no hardcoded arrays here.
 */
public class CookieSelectView {

    private final CookieRunApp app;
    private final StackPane    root;
    private int                selectedIndex;
    private final Label        nameLabel;
    private final HBox         cardRow;

    public CookieSelectView(CookieRunApp app) {
        this.app           = app;
        this.selectedIndex = app.getSelectedCookieIndex();

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

        // Page title
        Label heading = new Label("SELECT YOUR COOKIE");
        heading.setFont(Font.font("Impact", FontWeight.BOLD, 36));
        heading.setTextFill(Color.web("#FFD700"));
        heading.setStyle("-fx-effect: dropshadow(gaussian,#FF6600,10,0.4,2,2);");

        // Cookie cards — built from CookieList.ALL
        cardRow = new HBox(12);
        cardRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < CookieList.ALL.size(); i++) {
            cardRow.getChildren().add(makeCard(i));
        }

        // Selected name label
        nameLabel = new Label(CookieList.ALL.get(selectedIndex).name());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#FFE066"));
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian,#000,6,0.5,1,1);");

        // Play button
        Button play = new Button("▶  PLAY");
        play.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#FF8C00,#FF4500);
            -fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;
            -fx-padding: 11 48; -fx-background-radius: 30; -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian,#000,8,0.4,2,3);
            """);
        play.setOnAction(e -> {
            app.setSelectedCookieIndex(selectedIndex);
            app.startGame(selectedIndex);
        });

        // Back button
        Button back = new Button("← Back");
        back.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#555555,#333333);
            -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;
            -fx-padding: 8 28; -fx-background-radius: 30; -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian,#000,4,0.3,1,1);
            """);
        back.setOnAction(e -> app.showHomePage());

        HBox buttons = new HBox(16, back, play);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, heading, cardRow, nameLabel, buttons);
        content.setAlignment(Pos.CENTER);

        root = new StackPane(bg, content);
    }

    // ---- Card builder ----

    private VBox makeCard(int idx) {
        StackPane icon = buildIcon(idx);

        VBox card = new VBox(4, icon);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(80, 90);
        card.setStyle(cardStyle(idx == selectedIndex));

        card.setOnMouseClicked(e -> {
            selectedIndex = idx;
            nameLabel.setText(CookieList.ALL.get(idx).name());
            refreshCards();
        });
        return card;
    }

    private StackPane buildIcon(int idx) {
        CookieList.Entry entry = CookieList.ALL.get(idx);

        // Fallback canvas
        Canvas fb = new Canvas(64, 64);
        GraphicsContext gc = fb.getGraphicsContext2D();
        gc.setFill(Color.web(entry.hex()));
        gc.fillOval(5, 5, 54, 54);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Impact", 24));
        gc.fillText(entry.name().substring(0, 1), 22, 40);

        StackPane sp = new StackPane(fb);
        sp.setPrefSize(64, 64);

        try {
            Image img = new Image(getClass().getResourceAsStream(entry.spritePath()));
            if (!img.isError()) {
                int cols = entry.spriteCols();
                int fw   = (int)(img.getWidth() / cols);
                int rows = Math.max(1, (int) Math.round(img.getHeight() / (double) fw));
                int fh   = Math.max(1, (int)(img.getHeight() / rows));
                SpriteSheet ss = new SpriteSheet(entry.spritePath(), fw, fh, cols);
                Image frame = ss.getFrame(0);
                if (frame != null) {
                    ImageView iv = new ImageView(frame);
                    iv.setFitWidth(64);
                    iv.setFitHeight(64);
                    iv.setPreserveRatio(true);
                    sp.getChildren().setAll(iv);
                }
            }
        } catch (Exception ignored) {}

        return sp;
    }

    private void refreshCards() {
        for (int i = 0; i < cardRow.getChildren().size(); i++) {
            cardRow.getChildren().get(i).setStyle(cardStyle(i == selectedIndex));
        }
    }

    private String cardStyle(boolean selected) {
        return selected
            ? "-fx-background-color:rgba(255,200,0,0.35);-fx-background-radius:14;" +
              "-fx-border-color:#FFD700;-fx-border-width:2;-fx-border-radius:14;" +
              "-fx-cursor:hand;-fx-effect:dropshadow(gaussian,#FFD700,12,0.5,0,0);"
            : "-fx-background-color:rgba(255,255,255,0.12);-fx-background-radius:14;" +
              "-fx-border-color:rgba(255,255,255,0.25);-fx-border-width:1;" +
              "-fx-border-radius:14;-fx-cursor:hand;";
    }

    // ---- Background ----

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
