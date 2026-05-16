package service;

import dao.UserDAO;
import model.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO =
            new UserDAO();

    public boolean requestExpertRole(
            int userId
    ) {

        return userDAO
                .requestExpertRole(userId);
    }

    public List<User> getPendingRequests() {

        return userDAO
                .getPendingExpertRequests();
    }

    public boolean approveExpert(
            int userId
    ) {

        return userDAO
                .approveExpert(userId);
    }

    public boolean deleteUser(
            int userId
    ) {

        return userDAO.deleteUser(userId);
    }

    public boolean updateProfile(

            int userId,
            String fullName,
            String profilePicture

    ) {

        return userDAO.updateProfile(

                userId,
                fullName,
                profilePicture

        );
    }

    public List<User> getAllUsers() {

        return userDAO.getAllUsers();
    }

    public boolean rejectExpertRequest(
            int userId
    ) {

        return userDAO
                .rejectExpertRequest(userId);
    }
}