package gui;

import application.CookieRunApp;
import entities.cookies.CookieList;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Cookie Selection Page: pick a cookie, then play or go back.
 * Uses CookieSelectionCard for each cookie display.
 */
public class CookieSelectView {

    private final CookieRunApp app;
    private final StackPane root;
    private int selectedIndex;
    private final Label nameLabel;
    private final HBox cardRow;
    private final List<CookieSelectionCard> cards;

    public CookieSelectView(CookieRunApp app) {
        this.app = app;
        this.selectedIndex = app.getSelectedCookieIndex();
        this.cards = new ArrayList<>();

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

        // Selected name label - DECLARE EARLY before cards
        nameLabel = new Label(CookieList.ALL.get(selectedIndex).getDisplayName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#FFE066"));
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian,#000,6,0.5,1,1);");

        // Cookie cards — built from CookieList.ALL
        cardRow = new HBox(12);
        cardRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < CookieList.ALL.size(); i++) {
            final int index = i;  // Create final copy for lambda
            CookieSelectionCard card = new CookieSelectionCard(CookieList.ALL.get(index), index);
            cards.add(card);

            // Click handler
            card.getCardUI().setOnMouseClicked(e -> {
                selectedIndex = index;
                nameLabel.setText(CookieList.ALL.get(index).getDisplayName());
                refreshCards();
            });

            cardRow.getChildren().add(card.getCardUI());
        }

        // Set initial selection
        cards.get(selectedIndex).setSelected(true);

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

    private void refreshCards() {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(i == selectedIndex);
        }
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
