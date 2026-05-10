package ui.views.pages;

import application.CookieRunApp;
import graphics.rendering.BaseBackgroundPane;
import ui.views.util.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;


/**
 * Cookie Selection view - pick a cookie, then proceed to stage selection.
 * Extends StackPane to manage background and content layers.
 * Displays all available cookies with animated background.
 * Navigation flow: Home → Cookie Selection → Stage Selection → Game
 */
public class CookieSelectView extends StackPane {

    private final CardsRowPane cardsRowPane;
    private final BaseBackgroundPane backgroundPane;

    /**
     * Create the cookie selection view.
     * @param app The main application instance for navigation
     */
    public CookieSelectView(CookieRunApp app) {
        this.backgroundPane = new BaseBackgroundPane("/CookieSelectionPane/MagicalBg.png", 0.5);
        this.getChildren().add(backgroundPane);

        // Page title with background image
        StackPane headingPane = new StackPane();
        headingPane.setPrefHeight(80);
        headingPane.setAlignment(Pos.CENTER);

        // Background image
        var bgStream = getClass().getResourceAsStream("/CookieSelectionPane/anotherbutton.png");
        Image bgImage = new Image(bgStream);
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitHeight(60);
        bgImageView.setPreserveRatio(true);
        headingPane.getChildren().add(bgImageView);

        // Text with black outline
        Text heading = new Text("SELECT YOUR COOKIE");
        heading.setFont(FontLoader.getCookieRunBold(25));
        heading.setFill(Color.WHITE);
        heading.setStroke(Color.BLACK);
        heading.setStrokeWidth(1.7);
        heading.setStyle("-fx-effect: dropshadow(gaussian, #000000, 0, 0.6, 1.3, 1.4);");
        headingPane.getChildren().add(heading);

        // Cookie cards row pane (initialize with previously selected cookie)
        cardsRowPane = new CardsRowPane(app.getSelectedCookieIndex());

        // Play button
        PlayButton play = new PlayButton();
        play.setOnMouseClicked(e  -> {
            int selectedIndex = cardsRowPane.getSelectedIndex();
            app.setSelectedCookieIndex(selectedIndex);
            app.showStageSelection();
        });

        // Back button
        BackButton back = new BackButton();
        back.setOnMouseClicked(e -> app.showHomePage());

        HBox buttons = new HBox(16, back, play);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(18, headingPane, cardsRowPane, buttons);
        content.setAlignment(Pos.CENTER);

        this.getChildren().add(content);
    }
}
