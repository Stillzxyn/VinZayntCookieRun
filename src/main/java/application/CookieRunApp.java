package application;

import entity.base.Cookie;
import gamemanager.CookieList;
import gui.CookieSelectView;
import gui.HomePageView;
import gui.StageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CookieRunApp extends Application {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 450;

    private Stage primaryStage;

    // Default to Honey Butter (index 0) — first available cookie
    private int selectedCookieIndex = 0;

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

    public void showCookieSelect() {
        CookieSelectView select = new CookieSelectView(this);
        Scene scene = new Scene(select.getRoot(), WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    public void startGame(int cookieIndex) {
        Cookie cookie = CookieList.create(cookieIndex);   // ← one clear creation point
        StageView stage = new StageView(this, cookieIndex, cookie);
        Scene scene = new Scene(stage.getRoot(), WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        stage.startGame();
    }

    public int getSelectedCookieIndex() {
        return selectedCookieIndex;
    }

    public void setSelectedCookieIndex(int index) {
        this.selectedCookieIndex = index;
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) { launch(args); }
}
