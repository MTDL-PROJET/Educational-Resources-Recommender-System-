package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.User;
import service.ResourceService;
import utils.AlertUtils;

public class ResourceController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField linkField;

    @FXML
    private TextField categoryField;

    private final ResourceService resourceService =
            new ResourceService();

    @FXML
    private TextField tagsField;

    @FXML
    private TextField difficultyField;

    @FXML
    public void handleAddResource() {

        User currentUser =
                SessionManager.getCurrentUser();

        boolean success =
                resourceService.addResource(

                        titleField.getText(),

                        descriptionField.getText(),

                        imageField.getText(),

                        linkField.getText(),

                        categoryField.getText(),

                        tagsField.getText(),

                        difficultyField.getText(),

                        currentUser.getId()

                );

        if(success) {

            AlertUtils.showSuccess(
                    "Success",
                    "Resource added successfully."
            );

            clearFields();

        } else {

            AlertUtils.showError(
                    "Error",
                    "All fields are required."
            );
        }
    }

    private void clearFields() {

        titleField.clear();

        descriptionField.clear();

        imageField.clear();

        linkField.clear();

        categoryField.clear();

        tagsField.clear();

        difficultyField.clear();
    }
}