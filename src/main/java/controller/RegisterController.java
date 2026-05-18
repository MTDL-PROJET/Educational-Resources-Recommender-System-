package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AuthService;
import utils.AlertUtils;

public class RegisterController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService =
            new AuthService();

    @FXML
    public void handleRegister() {

        boolean success =
                authService.register(

                        fullNameField.getText(),
                        emailField.getText(),
                        passwordField.getText()

                );

        if(success) {

            AlertUtils.showSuccess(
                    "Success",
                    "Account created successfully!"
            );

            goToLogin();

        } else {

            AlertUtils.showError(
                    "Register Failed",
                    "Could not create account."
            );
        }
    }

    @FXML
    public void goToLogin() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/login.fxml"
                            )
                    );

            Scene scene =
                    new Scene(loader.load(), 1200, 700);

            scene.getStylesheets().add(
                    getClass()
                            .getResource(
                                    "/styles/style.css"
                            )
                            .toExternalForm()
            );

            Stage stage =
                    (Stage) emailField
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}