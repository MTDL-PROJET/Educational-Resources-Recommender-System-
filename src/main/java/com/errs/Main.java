package com.errs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/view/login.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load(), 900, 600);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/styles/style.css")
                        .toExternalForm()
        );

        stage.setTitle(
                "Educational Resources Recommender"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}