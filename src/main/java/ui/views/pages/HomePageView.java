package ui.views.pages;

import application.CookieRunApp;
import graphics.rendering.BaseBackgroundPane;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Home Page view - displays title, play button, and select cookie button.
 * Extends StackPane to manage background and content layers.
 */
public class HomePageView extends StackPane {

    private final CookieRunApp app;
    private final BaseBackgroundPane backgroundPane;
    private final HomePageButtonPane buttonPane;

    public HomePageView(CookieRunApp app) {
        this.app = app;
        this.backgroundPane = new BaseBackgroundPane("/HomePage/palaceBackground.png",0.5);
        this.buttonPane = new HomePageButtonPane(app);

        // Set this view as a StackPane with background
        this.getChildren().add(backgroundPane);

        // Start background animation
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                backgroundPane.update();
            }
        }.start();

        // Create content container (will be filled once images load)
        VBox contentContainer = new VBox(5);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().add(new javafx.scene.control.Label("Loading..."));

        // Add content to this view
        this.getChildren().add(contentContainer);

        // Load content asynchronously
        loadContentAsync(contentContainer);
    }

    /**
     * Load images and build content on background thread.
     */
    private void loadContentAsync(VBox contentContainer) {
        Task<VBox> task = new Task<VBox>() {
            @Override
            protected VBox call() throws Exception {
                // Load images
                Image subtitleFile = new Image(getClass().getResourceAsStream("/HomePage/Subtitle.png"));
                Image titleImageFile = new Image(getClass().getResourceAsStream("/HomePage/CPrunTitle.png"));
                Image letsGoButtonFile = new Image(getClass().getResourceAsStream("/HomePage/letsgoButton.png"));

                // Create title section
                ImageView subtitle = new ImageView(subtitleFile);
                subtitle.setFitWidth(75);
                subtitle.setFitHeight(20);
                subtitle.setPreserveRatio(true);

                VBox titleBox = new VBox(2);
                titleBox.setAlignment(Pos.CENTER);
                titleBox.getChildren().add(subtitle);

                ImageView titleImage = new ImageView(titleImageFile);
                titleImage.setFitWidth(400);
                titleImage.setFitHeight(157);
                titleImage.setPreserveRatio(true);
                titleBox.getChildren().add(titleImage);

                // Create Let's Go button
                buttonPane.createButtons(letsGoButtonFile, null);

                // Assemble layout
                VBox content = new VBox(5, titleBox, buttonPane);
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

}
