package cookierun.view;

import cookierun.CookieRunApp;
import cookierun.util.SpriteSheet;
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
 * Home Page: title, character selector, start button.
 */
public class HomePageView {

    private static final String[] NAMES = {
        "GingerBrave", "Strawberry", "Red Chili", "Banana", "Choco", "Wizard", "BraveGinger"
    };
    private static final String[] PATHS = {
        "/sprites/cookie_ginger.png", "/sprites/cookie_strawberry.png",
        "/sprites/cookie_chili.png",  "/sprites/cookie_banana.png",
        "/sprites/cookie_choco.png",  "/sprites/cookie_wizard.png",
        "/sprites/BraveGingerBreadCookie_SpriteSheet.png"
    };
    private static final int[]    COLS  = { 8, 10, 9, 8, 10, 9, 4 };
    private static final String[] HEX   = {
        "#C8822A","#E84060","#CC3300","#CCAA00","#4A2200","#6633AA","#C8822A"
    };

    private final CookieRunApp app;
    private final StackPane    root;
    private int                selectedIndex = 0;
    private final Label        nameLabel;
    private final HBox         cardRow;

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

        // Cookie cards
        cardRow = new HBox(10);
        cardRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < NAMES.length; i++) cardRow.getChildren().add(makeCard(i));

        nameLabel = new Label(NAMES[0]);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#FFE066"));
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian,#000,6,0.5,1,1);");

        // Start button
        Button start = new Button("▶  START GAME");
        start.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#FF8C00,#FF4500);
            -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
            -fx-padding: 12 42; -fx-background-radius: 30; -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian,#000,8,0.4,2,3);
            """);
        start.setOnAction(e -> app.startGame(selectedIndex));

        VBox content = new VBox(22, titleBox, cardRow, nameLabel, start);
        content.setAlignment(Pos.CENTER);

        root = new StackPane(bg, content);
    }

    private void drawBg(GraphicsContext gc, double off) {
        gc.setFill(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
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

    private VBox makeCard(int idx) {
        // Try to show first sprite frame as preview
        StackPane icon = buildIcon(idx);

        VBox card = new VBox(4, icon);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(72, 82);
        card.setStyle(cardStyle(idx == 0));

        card.setOnMouseClicked(e -> {
            selectedIndex = idx;
            nameLabel.setText(NAMES[idx]);
            refreshCards();
        });
        return card;
    }

    private StackPane buildIcon(int idx) {
        // Fallback canvas
        Canvas fb = new Canvas(60, 60);
        GraphicsContext gc = fb.getGraphicsContext2D();
        gc.setFill(Color.web(HEX[idx]));
        gc.fillOval(5, 5, 50, 50);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Impact", 22));
        gc.fillText(NAMES[idx].substring(0, 1), 20, 38);

        StackPane sp = new StackPane(fb);
        sp.setPrefSize(60, 60);

        try {
            Image img = new Image(getClass().getResourceAsStream(PATHS[idx]));
            if (!img.isError()) {
                int cols = COLS[idx];
                int fw   = (int)(img.getWidth() / cols);
                int rows = Math.max(1, (int) Math.round(img.getHeight() / (double) fw));
                int fh   = Math.max(1, (int)(img.getHeight() / rows));
                SpriteSheet ss = new SpriteSheet(PATHS[idx], fw, fh, cols);
                Image frame = ss.getFrame(0);
                if (frame != null) {
                    ImageView iv = new ImageView(frame);
                    iv.setFitWidth(60);
                    iv.setFitHeight(60);
                    iv.setPreserveRatio(true);
                    sp.getChildren().setAll(iv);
                }
            }
        } catch (Exception ignored) {}

        return sp;
    }

    private void refreshCards() {
        for (int i = 0; i < cardRow.getChildren().size(); i++)
            cardRow.getChildren().get(i).setStyle(cardStyle(i == selectedIndex));
    }

    private String cardStyle(boolean selected) {
        return selected
            ? "-fx-background-color:rgba(255,200,0,0.35);-fx-background-radius:12;" +
              "-fx-border-color:#FFD700;-fx-border-width:2;-fx-border-radius:12;" +
              "-fx-cursor:hand;-fx-effect:dropshadow(gaussian,#FFD700,10,0.5,0,0);"
            : "-fx-background-color:rgba(255,255,255,0.12);-fx-background-radius:12;" +
              "-fx-border-color:rgba(255,255,255,0.25);-fx-border-width:1;" +
              "-fx-border-radius:12;-fx-cursor:hand;";
    }

    public Pane getRoot() { return root; }
}
