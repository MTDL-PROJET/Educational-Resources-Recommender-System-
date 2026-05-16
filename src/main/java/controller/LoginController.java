package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Role;
import model.User;
import service.AuthService;
import utils.AlertUtils;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService =
            new AuthService();

    @FXML
    public void initialize() {

        emailField.requestFocus();
    }

    @FXML
    public void handleLogin() {

        String email =
                emailField.getText();

        String password =
                passwordField.getText();

        if(email.isBlank() || password.isBlank()) {

            AlertUtils.showError(
                    "Error",
                    "All fields are required."
            );

            return;
        }

        User user =
                authService.login(
                        email,
                        password
                );

        if(user != null) {

            SessionManager.setCurrentUser(user);

            try {

                Stage stage =
                        (Stage) emailField
                                .getScene()
                                .getWindow();

                FXMLLoader loader;

                if(user.getRole() == Role.ADMIN) {

                    loader =
                            new FXMLLoader(
                                    getClass().getResource(
                                            "/view/admin-dashboard.fxml"
                                    )
                            );

                } else if(user.getRole() == Role.EXPERT) {

                    loader =
                            new FXMLLoader(
                                    getClass().getResource(
                                            "/view/expert-dashboard.fxml"
                                    )
                            );

                } else {

                    loader =
                            new FXMLLoader(
                                    getClass().getResource(
                                            "/view/student-dashboard.fxml"
                                    )
                            );
                }

                Scene scene =
                        new Scene(
                                loader.load(),
                                1200,
                                700
                        );

                scene.getStylesheets().add(

                        getClass()
                                .getResource(
                                        "/styles/style.css"
                                )
                                .toExternalForm()
                );

                stage.setScene(scene);

            } catch (Exception e) {

                e.printStackTrace();

                AlertUtils.showError(
                        "Error",
                        "Could not load dashboard."
                );
            }

        } else {

            AlertUtils.showError(
                    "Login Failed",
                    "Invalid email or password."
            );
        }
    }

    @FXML
    public void goToRegister() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/register.fxml"
                            )
                    );

            Scene scene =
                    new Scene(
                            loader.load(),
                            1200,
                            700
                    );

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

            AlertUtils.showError(
                    "Error",
                    "Could not load register page."
            );
        }
    }
}