package ui.views.pages;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackButton extends ImageView {
    public BackButton() {
        try {
            Image backImg = new Image(getClass().getResourceAsStream("/CookieSelectionPane/BackButton.png"));
            this.setImage(backImg);
            this.setFitWidth(174);
            this.setFitHeight(49);
            this.setPreserveRatio(true);
            this.setStyle("-fx-cursor: hand;");
        } catch (Exception e) {
            System.err.println("Failed to load BackButton image: " + e.getMessage());
        }
    }
}
