package controller;

import config.SessionManager;

import dao.ResourceDAO;
import dao.ResourceInteractionDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import model.Resource;

import service.ResourceService;
import service.UserService;

import utils.AlertUtils;

import java.util.List;

public class StudentController {

    @FXML
    private TextField searchField;

    @FXML
    private TabPane tabPane;

    @FXML
    private ListView<Resource> resourceListView;

    @FXML
    private Tab resourcesTab;

    @FXML
    private Tab profileTab;

    private final ResourceDAO resourceDAO =
            new ResourceDAO();

    private final ResourceService resourceService =
            new ResourceService();

    private final ResourceInteractionDAO interactionDAO =
            new ResourceInteractionDAO();

    @FXML
    public void initialize() {

        loadPublishedResources();

        resourceListView.setOnMouseClicked(event -> {

            Resource resource =

                    resourceListView
                            .getSelectionModel()
                            .getSelectedItem();

            if(resource != null) {

                interactionDAO.saveInteraction(

                        SessionManager
                                .getCurrentUser()
                                .getId(),

                        resource.getId(),

                        "VIEW"
                );

                resourceDAO.incrementViews(
                        resource.getId()
                );

                resourceDAO.updateRecommendationScore(
                        resource.getId()
                );

                System.out.println(
                        "Tracked interaction for: "
                                + resource.getTitle()
                );
            }
        });
    }

    private void loadPublishedResources() {

        resourceListView.getItems().clear();

        List<Resource> resources =

                resourceService
                        .getPublishedResources();

        resourceListView.getItems().addAll(
                resources
        );
    }

    @FXML
    public void handleSearch() {

        String keyword =
                searchField.getText();

        if(keyword == null || keyword.isBlank()) {

            loadPublishedResources();

            return;
        }

        resourceListView.getItems().clear();

        List<Resource> resources =

                resourceService
                        .searchResources(keyword);

        for(Resource resource : resources) {

            interactionDAO.saveInteraction(

                    SessionManager
                            .getCurrentUser()
                            .getId(),

                    resource.getId(),

                    "SEARCH"
            );

            resourceDAO.incrementSearchHits(
                    resource.getId()
            );

            resourceDAO.updateRecommendationScore(
                    resource.getId()
            );
        }

        resourceListView.getItems().addAll(
                resources
        );
    }

    @FXML
    public void goToBrowse() {

        loadPublishedResources();

        tabPane.getSelectionModel()
                .select(resourcesTab);
    }

    @FXML
    public void goToProfile() {

        tabPane.getSelectionModel()
                .select(profileTab);
    }

    @FXML
    public void goToRecommendations() {

        resourceListView.getItems().clear();

        List<Resource> resources =

                resourceService
                        .getRecommendedResources(

                                SessionManager
                                        .getCurrentUser()
                                        .getId()
                        );

        resourceListView.getItems().addAll(
                resources
        );

        tabPane.getSelectionModel()
                .select(resourcesTab);
    }

    @FXML
    public void requestExpertAccess() {

        boolean success =

                new UserService()
                        .requestExpertRole(

                                SessionManager
                                        .getCurrentUser()
                                        .getId()
                        );

        if(success) {

            AlertUtils.showSuccess(
                    "Request Sent",
                    "Your request was sent to admin."
            );

        } else {

            AlertUtils.showError(
                    "Error",
                    "Could not send request."
            );
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

            scene.getStylesheets().add(

                    getClass()

                            .getResource(
                                    "/styles/style.css"
                            )

                            .toExternalForm()
            );

            Stage stage =

                    (Stage)

                            tabPane
                                    .getScene()
                                    .getWindow();

            stage.setScene(scene);

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtils.showError(
                    "Error",
                    "Could not logout."
            );
        }
    }
}