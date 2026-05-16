package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ExpertController {

    @FXML
    private BorderPane rootPane;

    @FXML
    public void goToAddResource() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/add-resource.fxml"
                            )
                    );

            rootPane.setCenter(
                    loader.load()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void goToManageResources() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/manage-resources.fxml"
                            )
                    );

            rootPane.setCenter(
                    loader.load()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {

        SessionManager.logout();

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/login.fxml"
                            )
                    );

            Scene scene =
                    new Scene(
                            loader.load(),
                            1200,
                            700
                    );

            Stage stage =
                    (Stage) rootPane
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}