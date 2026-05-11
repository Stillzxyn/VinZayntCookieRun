package gui.components;

import gui.graphics.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * PauseOverlayPane - Overlay for Pause when press P key
 */
public class PauseOverlayPane extends StackPane {

    public PauseOverlayPane(double width, double height) {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(0, 0, 0, 0.6));


        Text pauseText = new Text("PAUSED");
        pauseText.setFont(FontLoader.getCookieRunBold(60));
        pauseText.setFill(Color.web("#FFD700"));

        Text subText = new Text("Press P to Resume");
        subText.setFont(FontLoader.getCookieRunBold(20));
        subText.setFill(Color.WHITE);

        VBox content = new VBox(10, pauseText, subText);
        content.setAlignment(Pos.CENTER);


        this.getChildren().addAll(bg, content);
        this.setVisible(false);
    }
}