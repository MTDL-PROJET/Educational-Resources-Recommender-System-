package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import service.UserService;
import utils.AlertUtils;

public class ProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField imageField;

    private final UserService userService =
            new UserService();

    @FXML
    public void initialize() {

        User currentUser =
                SessionManager.getCurrentUser();

        nameField.setText(
                currentUser.getFullName()
        );

        imageField.setText(
                currentUser.getProfilePicture()
        );
    }

    @FXML
    public void handleUpdateProfile() {

        User currentUser =
                SessionManager.getCurrentUser();

        boolean success =
                userService.updateProfile(

                        currentUser.getId(),

                        nameField.getText(),

                        imageField.getText()

                );

        if(success) {

            AlertUtils.showSuccess(
                    "Updated",
                    "Profile updated successfully."
            );

        } else {

            AlertUtils.showError(
                    "Error",
                    "Could not update profile."
            );
        }
    }
}