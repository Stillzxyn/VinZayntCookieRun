package application;

import core.entities.base.Cookie;
import core.entities.cookies.CookieList;
import core.stages.StageList;
import ui.views.pages.HomePageView;
import ui.views.pages.CookieSelectView;
import ui.views.pages.StageSelectView;
import ui.views.pages.GameStage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CookieRunApp extends Application {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 450;

    private Stage primaryStage;

    // Default to Honey Butter (index 0) — first available cookie
    private int selectedCookieIndex = 0;

    // Default to World 1 (index 0) — first available stage
    private int selectedStageIndex = 0;

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
        Scene scene = new Scene(home, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    public void showCookieSelect() {
        CookieSelectView select = new CookieSelectView(this);
        Scene scene = new Scene(select, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    public void showStageSelection() {
        StageSelectView stageSelect = new StageSelectView(this);
        Scene scene = new Scene(stageSelect, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }

    public void startGame(int cookieIndex, int stageIndex) {
        setSelectedStageIndex(stageIndex);
        Cookie cookie = CookieList.get(cookieIndex);
        GameStage stage = new GameStage(this, cookieIndex, cookie);
        Scene scene = new Scene(stage, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        stage.startGame();
    }

    public void startGame(int cookieIndex) {
        startGame(cookieIndex, selectedStageIndex);
    }

    public int getSelectedCookieIndex() {
        return selectedCookieIndex;
    }

    public void setSelectedCookieIndex(int index) {
        this.selectedCookieIndex = index;
    }

    public int getSelectedStageIndex() {
        return selectedStageIndex;
    }

    public void setSelectedStageIndex(int index) {
        this.selectedStageIndex = index;
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public static void main(String[] args) { launch(args); }
}
