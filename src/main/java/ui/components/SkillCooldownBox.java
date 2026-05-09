package ui.components;

import core.abilities.CookieAbility;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SkillCooldownBox extends VBox {

    private final Rectangle overlay;
    private final Label text;

    public SkillCooldownBox(CookieAbility ability) {

        setAlignment(Pos.CENTER);

        Rectangle bg = new Rectangle(90, 90);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.web("#2C3E50"));

        overlay = new Rectangle(90, 90);
        overlay.setArcWidth(20);
        overlay.setArcHeight(20);
        overlay.setFill(Color.rgb(0,0,0,0.65));

        text = new Label("READY");

        text.setTextFill(Color.WHITE);

        text.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        StackPane pane = new StackPane(
                bg,
                overlay,
                text
        );

        getChildren().add(pane);

        new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (ability.getCooldownRemaining() > 0) {

                    double percent =
                            ability.getCooldownPercent();

                    overlay.setHeight(90 * (1 - percent));

                    text.setText(
                            String.format("%.1f",
                                    ability.getCooldownRemaining())
                    );
                }
                else {

                    overlay.setHeight(0);

                    text.setText("READY");
                }
            }
        }.start();
    }
}