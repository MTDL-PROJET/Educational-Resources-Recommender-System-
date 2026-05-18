package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.User;
import service.UserService;
import utils.AlertUtils;

public class ProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    @FXML
    private ImageView profileClippedImage;

    @FXML
    private Label statusBadgeLabel;

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

        emailField.setText(
                currentUser.getEmail()
        );

        roleField.setText(
                currentUser.getRole().name()
        );

        statusBadgeLabel.setText(
                currentUser.getRole().name() + " MEMBER"
        );

        loadAvatarImage(
                currentUser.getProfilePicture()
        );

        imageField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    loadAvatarImage(newValue);

                }
        );
    }

    private void loadAvatarImage(String url) {

        try {

            if (url != null && !url.isBlank()) {

                profileClippedImage.setImage(
                        new Image(url, true)
                );

            } else {

                profileClippedImage.setImage(
                        new Image(
                                "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                                true
                        )
                );

            }

        } catch (Exception e) {

            profileClippedImage.setImage(
                    new Image(
                            "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                            true
                    )
            );

        }

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

            currentUser.setFullName(
                    nameField.getText()
            );

            currentUser.setProfilePicture(
                    imageField.getText()
            );

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