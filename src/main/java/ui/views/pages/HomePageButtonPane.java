package ui.views.pages;

import application.CookieRunApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Button pane for home page.
 * Displays a single "Let's Go" button that leads to cookie selection.
 */
public class HomePageButtonPane extends VBox {

    private final CookieRunApp app;
    private ImageView letsGoButton;

    public HomePageButtonPane(CookieRunApp app) {
        this.app = app;
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Create the Let's Go button with image and set up events/hover effects.
     */
    public void createButtons(Image letsGoButtonImg, Image unused) {
        // Let's Go button
        letsGoButton = new ImageView(letsGoButtonImg);
        letsGoButton.setFitWidth(174);
        letsGoButton.setFitHeight(49);
        letsGoButton.setPreserveRatio(true);
        letsGoButton.setStyle("-fx-cursor: hand;");
        letsGoButton.setOnMouseClicked(e -> app.showCookieSelect());
        addHoverEffect(letsGoButton);

        // Add button to this pane
        this.getChildren().add(letsGoButton);
    }

    private void addHoverEffect(ImageView button) {
        button.setOnMouseEntered(e -> {
            button.setOpacity(0.85);
            button.setScaleX(1.08);
            button.setScaleY(1.08);
        });
        button.setOnMouseExited(e -> {
            button.setOpacity(1.0);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }

    public ImageView getLetsGoButton() {
        return letsGoButton;
    }
}
