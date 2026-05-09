package ui.views.pages;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayButton extends ImageView {
    public PlayButton() {
        try {
            Image playImg = new Image(getClass().getResourceAsStream("/CookieSelectionPane/PlayButton.png"));
            this.setImage(playImg);
            this.setFitWidth(174);
            this.setFitHeight(49);
            this.setPreserveRatio(true);
            this.setStyle("-fx-cursor: hand;");
        } catch (Exception e) {
            System.err.println("Failed to load PlayButton image: " + e.getMessage());
        }
    }
}
