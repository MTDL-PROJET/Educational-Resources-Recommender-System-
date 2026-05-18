package controller;

import config.SessionManager;

import dao.ResourceDAO;
import dao.ResourceInteractionDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import model.Resource;

import service.ResourceService;
import service.UserService;

import utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentController {

    @FXML
    private TextField searchField;

    @FXML
    private TabPane tabPane;

    @FXML
    private BorderPane rootPane;

    @FXML
    private ScrollPane homeScrollPane;

    @FXML
    private VBox searchResultsContainer;

    @FXML
    private ListView<Resource> resourceListView;

    @FXML
    private ListView<Resource> recommendedListView;

    @FXML
    private ListView<Resource> trendingListView;

    @FXML
    private ListView<Resource> recentlyViewedListView;

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

        setupMultiSectionHomeData();

        configureSingleListViewFactory(resourceListView);

        configureSingleListViewFactory(recommendedListView);

        configureSingleListViewFactory(trendingListView);

        configureSingleListViewFactory(recentlyViewedListView);

    }


    private void setupMultiSectionHomeData() {

        recommendedListView.getItems().clear();

        List<Resource> recommended =
                resourceService.getRecommendedResources(
                        SessionManager.getCurrentUser().getId()
                );

        recommendedListView.getItems().addAll(recommended);

        recommendedListView.refresh();


        trendingListView.getItems().clear();

        List<Resource> trending =
                loadTrendingResourcesFromDB();

        trendingListView.getItems().addAll(trending);

        trendingListView.refresh();


        recentlyViewedListView.getItems().clear();

        List<Resource> recent =
                loadRecentlyViewedResourcesFromDB();

        recentlyViewedListView.getItems().addAll(recent);

        recentlyViewedListView.refresh();

    }


    private void configureSingleListViewFactory(ListView<Resource> listView) {

        listView.setCellFactory(param -> new ListCell<Resource>() {

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

                        setStyle("-fx-background-color: transparent; -fx-padding: 4;");

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

        });

        listView.setOnMouseClicked(event -> {

            Resource resource =
                    listView.getSelectionModel().getSelectedItem();


            if (resource != null) {

                interactionDAO.saveInteraction(
                        SessionManager.getCurrentUser().getId(),
                        resource.getId(),
                        "VIEW"
                );

                resourceDAO.incrementViews(resource.getId());

                resourceDAO.updateRecommendationScore(resource.getId());


                listView.getSelectionModel().clearSelection();


                setupMultiSectionHomeData();


                try {

                    String url =
                            resource.getExternalLink();

                    if (url != null && !url.isBlank()) {

                        if (java.awt.Desktop.isDesktopSupported() &&
                                java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {

                            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));

                        } else {

                            new ProcessBuilder("cmd", "/c", "start", url).start();

                        }

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        });

    }


    private List<Resource> loadTrendingResourcesFromDB() {

        List<Resource> list =
                new ArrayList<>();

        String sql =
                "SELECT * FROM resources " +
                        "WHERE status = 'PUBLISHED' " +
                        "ORDER BY views DESC LIMIT 5";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql);

                java.sql.ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                list.add(extractResourceFromResultSet(resultSet));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }


    private List<Resource> loadRecentlyViewedResourcesFromDB() {

        List<Resource> list =
                new ArrayList<>();

        String sqlUserColumn =
                "SELECT r.* FROM resources r " +
                        "JOIN resource_interactions ri ON r.id = ri.resource_id " +
                        "WHERE ri.user_id = ? AND ri.interaction_type = 'VIEW' " +
                        "ORDER BY ri.id DESC LIMIT 5";

        String sqlStudentColumn =
                "SELECT r.* FROM resources r " +
                        "JOIN resource_interactions ri ON r.id = ri.resource_id " +
                        "WHERE ri.student_id = ? AND ri.interaction_type = 'VIEW' " +
                        "ORDER BY ri.id DESC LIMIT 5";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection()
        ) {

            try (
                    java.sql.PreparedStatement statement =
                            connection.prepareStatement(sqlUserColumn)
            ) {

                statement.setInt(1, SessionManager.getCurrentUser().getId());

                try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        list.add(extractResourceFromResultSet(resultSet));
                    }
                    return list;
                }

            } catch (java.sql.SQLException e) {

                try (
                        java.sql.PreparedStatement fallbackStatement =
                                connection.prepareStatement(sqlStudentColumn)
                ) {

                    fallbackStatement.setInt(1, SessionManager.getCurrentUser().getId());

                    try (java.sql.ResultSet resultSet = fallbackStatement.executeQuery()) {
                        while (resultSet.next()) {
                            list.add(extractResourceFromResultSet(resultSet));
                        }
                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }


    private Resource extractResourceFromResultSet(java.sql.ResultSet resultSet) throws Exception {

        Resource resource =
                new Resource();

        resource.setId(resultSet.getInt("id"));

        resource.setTitle(resultSet.getString("title"));

        resource.setCategory(resultSet.getString("category"));

        resource.setTags(resultSet.getString("tags"));

        resource.setDifficulty(resultSet.getString("difficulty"));

        resource.setViews(resultSet.getInt("views"));

        resource.setLikesCount(resultSet.getInt("likes_count"));

        resource.setExternalLink(resultSet.getString("external_link"));

        resource.setImageUrl(resultSet.getString("image_url"));

        return resource;

    }


    private List<Resource> loadResourcesFromDBByKeyword(String keyword) {

        List<Resource> list =
                new ArrayList<>();

        String sql =
                "SELECT * FROM resources " +
                        "WHERE status = 'PUBLISHED' " +
                        "AND (title LIKE ? OR category LIKE ? OR tags LIKE ?)";


        try (
                java.sql.Connection connection =
                        config.DatabaseConfig.getConnection();

                java.sql.PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String match =
                    "%" + keyword + "%";

            statement.setString(1, match);

            statement.setString(2, match);

            statement.setString(3, match);


            try (
                    java.sql.ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    list.add(extractResourceFromResultSet(resultSet));

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }


    private void toggleViews(boolean showSearch) {

        homeScrollPane.setVisible(!showSearch);

        homeScrollPane.setManaged(!showSearch);

        searchResultsContainer.setVisible(showSearch);

        searchResultsContainer.setManaged(showSearch);

    }


    @FXML
    public void handleFilterAll() {

        toggleViews(false);

        setupMultiSectionHomeData();

    }


    private void filterResourcesBySpecificCategory(String categoryName) {

        toggleViews(true);

        resourceListView.getItems().clear();

        List<Resource> activeResources =
                resourceService.getPublishedResources();


        List<Resource> filteredList =
                activeResources.stream()
                        .filter(res -> res.getCategory() != null &&
                                res.getCategory().equalsIgnoreCase(categoryName))
                        .collect(Collectors.toList());


        resourceListView.getItems().addAll(filteredList);

        resourceListView.refresh();

    }


    private void filterResourcesByDifficulty(String difficultyLevel) {

        toggleViews(true);

        resourceListView.getItems().clear();

        List<Resource> activeResources =
                resourceService.getPublishedResources();


        List<Resource> filteredList =
                activeResources.stream()
                        .filter(res -> res.getDifficulty() != null &&
                                res.getDifficulty().equalsIgnoreCase(difficultyLevel))
                        .collect(Collectors.toList());


        resourceListView.getItems().addAll(filteredList);

        resourceListView.refresh();

    }


    private void filterResourcesByTag(String tagKeyword) {

        toggleViews(true);

        resourceListView.getItems().clear();

        List<Resource> activeResources =
                resourceService.getPublishedResources();


        List<Resource> filteredList =
                activeResources.stream()
                        .filter(res -> res.getTags() != null &&
                                res.getTags().toLowerCase().contains(tagKeyword.toLowerCase()))
                        .collect(Collectors.toList());


        resourceListView.getItems().addAll(filteredList);

        resourceListView.refresh();

    }


    @FXML
    public void handleFilterProgramming() {

        filterResourcesBySpecificCategory("Programming");

    }


    @FXML
    public void handleFilterBusiness() {

        filterResourcesBySpecificCategory("Business");

    }


    @FXML
    public void handleFilterTech() {

        filterResourcesBySpecificCategory("Tech");

    }


    @FXML
    public void handleFilterBeginner() {

        filterResourcesByDifficulty("Beginner");

    }


    @FXML
    public void handleFilterAdvanced() {

        filterResourcesByDifficulty("Advanced");

    }


    @FXML
    public void handleFilterJava() {

        filterResourcesByTag("java");

    }


    @FXML
    public void handleSearch() {

        String keyword =
                searchField.getText();


        if (keyword == null || keyword.isBlank()) {

            toggleViews(false);

            setupMultiSectionHomeData();

            return;

        }


        toggleViews(true);

        resourceListView.getItems().clear();


        List<Resource> resources =
                resourceService.searchResources(keyword);


        if (resources == null || resources.isEmpty()) {

            resources =
                    loadResourcesFromDBByKeyword(keyword);

        }


        for (Resource resource : resources) {

            interactionDAO.saveInteraction(
                    SessionManager.getCurrentUser().getId(),
                    resource.getId(),
                    "SEARCH"
            );

            resourceDAO.incrementSearchHits(resource.getId());

            resourceDAO.updateRecommendationScore(resource.getId());

        }


        resourceListView.getItems().addAll(resources);

        resourceListView.refresh();

    }


    @FXML
    public void goToBrowse() {

        toggleViews(false);

        setupMultiSectionHomeData();

        tabPane.getSelectionModel().select(resourcesTab);

    }


    @FXML
    public void goToProfile() {

        tabPane.getSelectionModel().select(profileTab);

    }


    @FXML
    public void goToRecommendations() {

        toggleViews(true);

        resourceListView.getItems().clear();

        List<Resource> resources =
                resourceService.getRecommendedResources(
                        SessionManager.getCurrentUser().getId()
                );

        resourceListView.getItems().addAll(resources);

        resourceListView.refresh();

        tabPane.getSelectionModel().select(resourcesTab);

    }


    @FXML
    public void requestExpertAccess() {

        boolean success =
                new UserService().requestExpertRole(
                        SessionManager.getCurrentUser().getId()
                );


        if (success) {

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
                            .getResource("/styles/style.css")
                            .toExternalForm()
            );


            Stage stage =
                    (Stage) tabPane
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