package gui;

import application.CookieRunApp;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

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

        // Content container (will be filled once images load)
        VBox contentContainer = new VBox(5);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().add(new Label("Loading..."));

        root = new StackPane(bg, contentContainer);

        // Load images asynchronously to prevent UI freeze
        loadImagesAsync(contentContainer);
    }

    private void loadImagesAsync(VBox contentContainer) {
        Task<VBox> task = new Task<VBox>() {
            @Override
            protected VBox call() throws Exception {
                // Load images on background thread
                Image subtitleFile = new Image(getClass().getResourceAsStream("/HomePage/Subtitle.png"));
                Image titleImageFile = new Image(getClass().getResourceAsStream("/HomePage/HomeTitle.png"));
                Image greenButtonFile = new Image(getClass().getResourceAsStream("/HomePage/GreenButton.png"));
                Image blueButtonFile = new Image(getClass().getResourceAsStream("/HomePage/BlueButton.png"));

                ImageView subtitle = new ImageView(subtitleFile);
                subtitle.setFitWidth(75);
                subtitle.setFitHeight(20);
                subtitle.setPreserveRatio(true);

                VBox titleBox = new VBox(2);
                titleBox.setAlignment(Pos.CENTER);
                titleBox.getChildren().add(subtitle);

                ImageView titleImage = new ImageView(titleImageFile);
                titleImage.setFitWidth(400);
                titleImage.setFitHeight(150);
                titleImage.setPreserveRatio(true);
                titleBox.getChildren().add(titleImage);

                ImageView playButton = new ImageView(greenButtonFile);
                playButton.setFitWidth(186);
                playButton.setFitHeight(51);
                playButton.setPreserveRatio(false);
                playButton.setStyle("-fx-cursor: hand;");
                playButton.setOnMouseClicked(e -> app.startGame(app.getSelectedCookieIndex()));

                ImageView selectButton = new ImageView(blueButtonFile);
                selectButton.setFitWidth(186);
                selectButton.setFitHeight(51);
                selectButton.setPreserveRatio(false);
                selectButton.setStyle("-fx-cursor: hand;");
                selectButton.setOnMouseClicked(e -> app.showCookieSelect());

                VBox content = new VBox(5, titleBox, selectButton, playButton);
                content.setAlignment(Pos.CENTER);
                return content;
            }
        };

        task.setOnSucceeded(e -> {
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(task.getValue());
        });

        new Thread(task, "ImageLoader").start();
    }

    private void drawBg(GraphicsContext gc, double off) {
        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#71C5CF")),
                new Stop(1, Color.web("#EBFDD3"))));
        gc.fillRect(0, 0, CookieRunApp.WIDTH, CookieRunApp.HEIGHT);

        gc.setFill(Color.web("#84E18C"));
        gc.fillRect(0, CookieRunApp.HEIGHT - 80, CookieRunApp.WIDTH, 80);

        gc.setFill(Color.web("#78C58C", 0.4));
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
