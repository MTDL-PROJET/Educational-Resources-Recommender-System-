package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.User;
import service.UserService;
import utils.AlertUtils;
import config.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> requestListView;

    private final UserService userService =
            new UserService();

    private List<User> pendingUsers;

    @FXML
    public void initialize() {

        loadRequests();
    }

    private void loadRequests() {

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

        System.out.println("Pending requests");
    }

    @FXML
    public void showUsers() {

        System.out.println("Users");
    }

    @FXML
    public void approveRequest() {

        System.out.println("Approved");
    }

    @FXML
    public void deleteUser() {

        System.out.println("Deleted");
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
                    new Scene(loader.load(), 1200, 700);

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