package cookierun;

import cookierun.view.HomePageView;
import cookierun.view.StageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CookieRunApp extends Application {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 450;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("VinZaynt's Cookie Run");
        stage.setResizable(false);
        showHomePage();
        stage.show();
    }

    public void showHomePage() {
        HomePageView home = new HomePageView(this);
        Scene scene = new Scene(home.getRoot(), WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    public void startGame(int cookieIndex) {
        StageView stage = new StageView(this, cookieIndex);
        Scene scene = new Scene(stage.getRoot(), WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        stage.startGame();
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) { launch(args); }
}
