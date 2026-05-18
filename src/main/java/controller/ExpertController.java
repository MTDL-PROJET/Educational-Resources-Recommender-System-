package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Resource;
import model.ResourceStatus;
import service.search.ElasticResourceService;
import utils.AlertUtils;
import java.util.ArrayList;
import java.util.List;

public class ExpertController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private ListView<Resource> resourceListView;

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

            ExpertController controller =
                    loader.getController();

            controller.loadAllResourcesIntoList();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void loadAllResourcesIntoList() {

        if (resourceListView == null) {
            return;
        }


        resourceListView.getItems().clear();

        List<Resource> allResources =
                new ArrayList<>();

        String sql =
                "SELECT * FROM resources ORDER BY id DESC";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql);

                java.sql.ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setTags(
                        resultSet.getString("tags")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resource.setViews(
                        resultSet.getInt("views")
                );

                resource.setLikesCount(
                        resultSet.getInt("likes_count")
                );

                resource.setExternalLink(
                        resultSet.getString("external_link")
                );

                resource.setImageUrl(
                        resultSet.getString("image_url")
                );

                resource.setStatus(
                        ResourceStatus.valueOf(
                                resultSet.getString("status")
                        )
                );


                allResources.add(resource);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }


        resourceListView.getItems().addAll(allResources);


        resourceListView.setCellFactory(param -> new ListCell<Resource>() {

            @Override
            protected void updateItem(Resource resource, boolean empty) {

                super.updateItem(resource, empty);


                if (empty || resource == null) {

                    setText(null);

                    setGraphic(null);

                    setStyle("-fx-background-color: transparent;");

                } else {

                    try {

                        FXMLLoader loader =
                                new FXMLLoader(
                                        getClass().getResource(
                                                "/view/resource-card.fxml"
                                        )
                                );

                        javafx.scene.Node node =
                                loader.load();


                        ResourceCardController cardController =
                                loader.getController();

                        cardController.setResource(resource);


                        setText(null);

                        setGraphic(node);


                        if (resource.getStatus() == ResourceStatus.UNPUBLISHED) {

                            setStyle("-fx-background-color: transparent; -fx-padding: 8; -fx-opacity: 0.65;");

                        } else {

                            setStyle("-fx-background-color: transparent; -fx-padding: 8;");

                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

        });

    }

    @FXML
    public void handleEditMaterial() {

        Resource selected =
                resourceListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            utils.AlertUtils.showError(
                    "Selection Error",
                    "Please select a resource from the list first."
            );

            return;

        }

        javafx.scene.control.Dialog<Resource> dialog =
                new javafx.scene.control.Dialog<>();

        dialog.setTitle("Edit Resource");

        dialog.setHeaderText("Update all details for: " + selected.getTitle());


        javafx.scene.control.ButtonType saveButtonType =
                new javafx.scene.control.ButtonType(
                        "SAVE CHANGES",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButtonType,
                javafx.scene.control.ButtonType.CANCEL
        );


        javafx.scene.layout.GridPane grid =
                new javafx.scene.layout.GridPane();

        grid.setHgap(15);

        grid.setVgap(15);

        grid.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));


        // Definim câmpurile pre-completate cu datele curente
        javafx.scene.control.TextField titleField =
                new javafx.scene.control.TextField(selected.getTitle());

        javafx.scene.control.TextField categoryField =
                new javafx.scene.control.TextField(selected.getCategory());

        javafx.scene.control.TextField tagsField =
                new javafx.scene.control.TextField(selected.getTags());

        javafx.scene.control.TextField diffField =
                new javafx.scene.control.TextField(selected.getDifficulty());

        javafx.scene.control.TextField imageField =
                new javafx.scene.control.TextField(selected.getImageUrl());

        javafx.scene.control.TextField linkField =
                new javafx.scene.control.TextField(selected.getExternalLink());


        titleField.setPrefWidth(300);


        // Adăugăm rândurile în formular
        grid.add(new javafx.scene.control.Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new javafx.scene.control.Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);

        grid.add(new javafx.scene.control.Label("Tags:"), 0, 2);
        grid.add(tagsField, 1, 2);

        grid.add(new javafx.scene.control.Label("Difficulty:"), 0, 3);
        grid.add(diffField, 1, 3);

        grid.add(new javafx.scene.control.Label("Image URL:"), 0, 4);
        grid.add(imageField, 1, 4);

        grid.add(new javafx.scene.control.Label("Course Link:"), 0, 5);
        grid.add(linkField, 1, 5);


        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == saveButtonType) {

                selected.setTitle(titleField.getText());

                selected.setCategory(categoryField.getText());

                selected.setTags(tagsField.getText());

                selected.setDifficulty(diffField.getText());

                selected.setImageUrl(imageField.getText());

                selected.setExternalLink(linkField.getText());

                return selected;

            }

            return null;

        });

        java.util.Optional<Resource> result =
                dialog.showAndWait();

        result.ifPresent(updatedResource -> {

            String sql =
                    "UPDATE resources " +
                            "SET title = ?, category = ?, tags = ?, difficulty = ?, image_url = ?, external_link = ? " +
                            "WHERE id = ?";


            try (
                    java.sql.Connection connection =
                            config.DatabaseConfig.getConnection();

                    java.sql.PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(1, updatedResource.getTitle());

                statement.setString(2, updatedResource.getCategory());

                statement.setString(3, updatedResource.getTags());

                statement.setString(4, updatedResource.getDifficulty());

                statement.setString(5, updatedResource.getImageUrl());

                statement.setString(6, updatedResource.getExternalLink());

                statement.setInt(7, updatedResource.getId());


                int rows =
                        statement.executeUpdate();


                if (rows > 0) {

                    new service.search.ElasticResourceService()
                            .updateResource(updatedResource);


                    utils.AlertUtils.showSuccess(
                            "Success",
                            "Resource fully updated."
                    );


                    loadAllResourcesIntoList();

                }

            } catch (Exception e) {

                e.printStackTrace();

                utils.AlertUtils.showError(
                        "Database Error",
                        "Could not update the resource details."
                );

            }

        });

    }

    @FXML
    public void handlePublishMaterial() {

        Resource selected =
                resourceListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            AlertUtils.showError(
                    "Selection Error",
                    "Please select a resource first."
            );

            return;

        }


        String sql =
                "UPDATE resources SET status = 'PUBLISHED' WHERE id = ?";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, selected.getId());

            int rows =
                    statement.executeUpdate();


            if (rows > 0) {

                selected.setStatus(ResourceStatus.PUBLISHED);

                new ElasticResourceService()
                        .updateResource(selected);


                AlertUtils.showSuccess(
                        "Success",
                        "Resource published and visible to students!"
                );


                loadAllResourcesIntoList();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @FXML
    public void handleUnpublishMaterial() {

        Resource selected =
                resourceListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            AlertUtils.showError(
                    "Selection Error",
                    "Please select a resource first."
            );

            return;

        }


        String sql =
                "UPDATE resources SET status = 'UNPUBLISHED' WHERE id = ?";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, selected.getId());

            int rows =
                    statement.executeUpdate();


            if (rows > 0) {

                selected.setStatus(ResourceStatus.UNPUBLISHED);

                new ElasticResourceService()
                        .deleteResource(selected.getId());


                AlertUtils.showSuccess(
                        "Success",
                        "Resource unpublished successfully."
                );


                loadAllResourcesIntoList();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @FXML
    public void handleDeleteMaterial() {

        Resource selected =
                resourceListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            AlertUtils.showError(
                    "Selection Error",
                    "Please select a resource first."
            );

            return;

        }


        String sql =
                "DELETE FROM resources WHERE id = ?";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, selected.getId());

            int rows =
                    statement.executeUpdate();


            if (rows > 0) {

                new ElasticResourceService()
                        .deleteResource(selected.getId());


                AlertUtils.showSuccess(
                        "Deleted",
                        "Resource deleted permanently."
                );


                loadAllResourcesIntoList();

            }

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

            scene.getStylesheets().add(
                    getClass()
                            .getResource("/styles/style.css")
                            .toExternalForm()
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

    private boolean isCinematicMode = false;

    @FXML
    public void handleToggleCinematic() {

        isCinematicMode = !isCinematicMode;


        if (isCinematicMode) {

            rootPane.getStyleClass().add("cinematic-mode");

        } else {

            rootPane.getStyleClass().remove("cinematic-mode");

        }

    }
}