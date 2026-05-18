package controller;

import config.SessionManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import javafx.stage.Stage;

import model.User;

import service.UserService;
import utils.AlertUtils;

import java.util.List;

public class AdminController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label titleLabel;

    @FXML
    private VBox tableContainer;

    @FXML
    private VBox analyticsContainer;

    @FXML
    private HBox actionButtons;

    @FXML
    private PieChart rolePieChart;

    @FXML
    private BarChart<String, Number> resourceBarChart;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> roleEmailColumn;

    private final UserService userService =
            new UserService();

    private List<User> currentDisplayedUsers;

    private boolean isViewingRequests = true;


    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fullName")
        );

        setupRoleEmailColumnMapping();

        loadRequests();

    }


    private void setupRoleEmailColumnMapping() {

        roleEmailColumn.setCellFactory(column -> new TableCell<User, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);


                if (empty) {

                    setGraphic(null);

                    setText(null);

                } else {

                    User user =
                            getTableView()
                                    .getItems()
                                    .get(getIndex());


                    if (isViewingRequests) {

                        setText(user.getEmail());

                        setGraphic(null);

                        setStyle("-fx-text-fill: #5f5f5f; -fx-font-size: 14;");

                    } else {

                        Label badge =
                                new Label(user.getRole().name());


                        String badgeStyle =
                                "-fx-padding: 5 12 5 12; " +
                                        "-fx-background-radius: 10; " +
                                        "-fx-font-size: 11; " +
                                        "-fx-font-weight: bold; ";


                        if (user.getRole().name().equals("ADMIN")) {

                            badge.setStyle(badgeStyle + "-fx-background-color: #1f1f1f; -fx-text-fill: white;");

                        } else if (user.getRole().name().equals("EXPERT")) {

                            badge.setStyle(badgeStyle + "-fx-background-color: #c58c62; -fx-text-fill: white;");

                        } else {

                            badge.setStyle(badgeStyle + "-fx-background-color: #e7ddd1; -fx-text-fill: #7a6f66;");

                        }


                        setGraphic(badge);

                        setText(null);

                    }

                }

            }

        });

    }


    private void loadRequests() {

        isViewingRequests = true;

        titleLabel.setText("Expert Requests");


        userTableView.getItems().clear();

        currentDisplayedUsers =
                userService.getPendingRequests();


        userTableView.getItems().addAll(currentDisplayedUsers);

    }


    @FXML
    public void approveSelectedUser() {

        User selectedUser =
                userTableView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedUser == null) {

            AlertUtils.showError(
                    "Error",
                    "Select a user from the table first."
            );

            return;

        }


        if (!isViewingRequests) {

            AlertUtils.showError(
                    "Action Denied",
                    "You can only approve users inside Pending Requests tab."
            );

            return;

        }


        boolean success =
                userService.approveExpert(selectedUser.getId());


        if (success) {

            AlertUtils.showSuccess(
                    "Approved",
                    "User is now an EXPERT."
            );

            loadRequests();

        }

    }


    @FXML
    public void deleteSelectedUser() {

        User selectedUser =
                userTableView
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedUser == null) {

            AlertUtils.showError(
                    "Error",
                    "Select a user from the table first."
            );

            return;

        }


        boolean success =
                userService.deleteUser(selectedUser.getId());


        if (success) {

            AlertUtils.showSuccess(
                    "Deleted",
                    "User deleted successfully."
            );


            if (isViewingRequests) {

                loadRequests();

            } else {

                showUsers();

            }

        }

    }


    @FXML
    public void showPendingRequests() {

        if (analyticsContainer != null) {

            analyticsContainer.setVisible(false);

            analyticsContainer.setManaged(false);

        }

        if (tableContainer != null) {

            tableContainer.setVisible(true);

            tableContainer.setManaged(true);

        }

        if (actionButtons != null) {

            actionButtons.setVisible(true);

            actionButtons.setManaged(true);

        }

        loadRequests();

    }


    @FXML
    public void showUsers() {

        isViewingRequests = false;

        titleLabel.setText("All Users");


        if (analyticsContainer != null) {

            analyticsContainer.setVisible(false);

            analyticsContainer.setManaged(false);

        }

        if (tableContainer != null) {

            tableContainer.setVisible(true);

            tableContainer.setManaged(true);

        }

        if (actionButtons != null) {

            actionButtons.setVisible(false);

            actionButtons.setManaged(false);

        }


        userTableView.getItems().clear();

        currentDisplayedUsers =
                userService.getAllUsers();


        userTableView.getItems().addAll(currentDisplayedUsers);

    }


    @FXML
    public void showAnalytics() {

        if (tableContainer != null) {

            tableContainer.setVisible(false);

            tableContainer.setManaged(false);

        }

        if (analyticsContainer != null) {

            analyticsContainer.setVisible(true);

            analyticsContainer.setManaged(true);

        }

        loadPieChartData();

        loadBarChartData();

    }


    private void loadPieChartData() {

        rolePieChart.getData().clear();

        String sql =
                "SELECT role, COUNT(*) as count FROM users GROUP BY role";


        try (
                java.sql.Connection conn =
                        config.DatabaseConfig.getConnection();

                java.sql.Statement stmt =
                        conn.createStatement();

                java.sql.ResultSet rs =
                        stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                rolePieChart.getData().add(
                        new PieChart.Data(
                                rs.getString("role"),
                                rs.getInt("count")
                        )
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private void loadBarChartData() {

        resourceBarChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        series.setName("Views");


        String sql =
                "SELECT title, views FROM resources ORDER BY views DESC LIMIT 5";


        try (
                java.sql.Connection conn =
                        config.DatabaseConfig.getConnection();

                java.sql.Statement stmt =
                        conn.createStatement();

                java.sql.ResultSet rs =
                        stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                series.getData().add(
                        new XYChart.Data<>(
                                rs.getString("title"),
                                rs.getInt("views")
                        )
                );

            }

            resourceBarChart.getData().add(series);

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
                    (Stage) titleLabel
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