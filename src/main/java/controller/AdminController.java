package controller;

import config.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import utils.AlertUtils;

import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> requestListView;

    @FXML
    private Label titleLabel;

    private final UserService userService =
            new UserService();

    private List<User> pendingUsers;

    @FXML
    public void initialize() {

        loadRequests();
    }

    private void loadRequests() {

        titleLabel.setText(
                "Expert Requests"
        );

        requestListView.getItems().clear();

        pendingUsers =
                userService.getPendingRequests();

        for(User user : pendingUsers) {

            requestListView.getItems().add(

                    user.getId()
                            + " | "
                            + user.getFullName()
                            + " | "
                            + user.getEmail()

            );
        }
    }

    @FXML
    public void approveSelectedUser() {

        int selectedIndex =

                requestListView
                        .getSelectionModel()
                        .getSelectedIndex();

        if(selectedIndex == -1) {

            AlertUtils.showError(
                    "Error",
                    "Select a user first."
            );

            return;
        }

        User selectedUser =
                pendingUsers.get(selectedIndex);

        boolean success =
                userService.approveExpert(
                        selectedUser.getId()
                );

        if(success) {

            AlertUtils.showSuccess(
                    "Approved",
                    "User is now EXPERT."
            );

            loadRequests();
        }
    }

    @FXML
    public void deleteSelectedUser() {

        int selectedIndex =

                requestListView
                        .getSelectionModel()
                        .getSelectedIndex();

        if(selectedIndex == -1) {

            AlertUtils.showError(
                    "Error",
                    "Select a user first."
            );

            return;
        }

        User selectedUser =
                pendingUsers.get(selectedIndex);

        boolean success =
                userService.deleteUser(
                        selectedUser.getId()
                );

        if(success) {

            AlertUtils.showSuccess(
                    "Deleted",
                    "User deleted successfully."
            );

            loadRequests();
        }
    }

    @FXML
    public void showPendingRequests() {

        loadRequests();
    }

    @FXML
    public void showUsers() {

        titleLabel.setText(
                "All Users"
        );

        requestListView.getItems().clear();

        List<User> users =
                userService.getAllUsers();

        for(User user : users) {

            requestListView.getItems().add(

                    user.getId()
                            + " | "
                            + user.getFullName()
                            + " | "
                            + user.getRole()

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
                    (Stage) requestListView
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}