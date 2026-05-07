module CookieRun {
    requires javafx.controls;
    requires javafx.fxml;

    exports cookierun;
    exports cookierun.view;
    exports cookierun.model;
    exports cookierun.controller;
    exports cookierun.util;
    exports cookierun.interfaces;

    opens cookierun to javafx.fxml;
}