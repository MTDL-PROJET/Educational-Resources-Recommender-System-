package dao;

import config.DatabaseConfig;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {

    public boolean registerUser(User user) {

        String sql =
                "INSERT INTO users(full_name, email, password_hash, role) " +
                        "VALUES (?, ?, ?, ?)";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setString(1, user.getFullName());

            statement.setString(2, user.getEmail());

            statement.setString(3, user.getPasswordHash());

            statement.setString(4, user.getRole().name());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public User loginUser(String email) {

        String sql =
                "SELECT * FROM users WHERE email = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setString(1, email);

            ResultSet resultSet =
                    statement.executeQuery();

            if(resultSet.next()) {

                User user = new User();

                user.setId(resultSet.getInt("id"));

                user.setFullName(
                        resultSet.getString("full_name")
                );

                user.setEmail(
                        resultSet.getString("email")
                );

                user.setPasswordHash(
                        resultSet.getString("password_hash")
                );

                user.setRole(
                        Role.valueOf(
                                resultSet.getString("role")
                        )
                );

                user.setExpertRequest(
                        resultSet.getBoolean("expert_request")
                );

                user.setValidated(
                        resultSet.getBoolean("validated")
                );

                user.setProfilePicture(
                        resultSet.getString("profile_picture")
                );

                return user;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean requestExpertRole(
            int userId
    ) {

        String sql =
                "UPDATE users " +
                        "SET expert_request = true " +
                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, userId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public List<User> getPendingExpertRequests() {

        List<User> users =
                new ArrayList<>();

        String sql =
                "SELECT * FROM users " +
                        "WHERE expert_request = true " +
                        "AND role = 'STUDENT'";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                User user =
                        new User();

                user.setId(
                        resultSet.getInt("id")
                );

                user.setFullName(
                        resultSet.getString("full_name")
                );

                user.setEmail(
                        resultSet.getString("email")
                );

                users.add(user);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return users;
    }

    public boolean approveExpert(
            int userId
    ) {

        String sql =
                "UPDATE users " +
                        "SET role = 'EXPERT', " +
                        "validated = true " +
                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, userId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteUser(
            int userId
    ) {

        String sql =
                "DELETE FROM users " +
                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, userId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProfile(

            int userId,
            String fullName,
            String profilePicture

    ) {

        String sql =

                "UPDATE users " +

                        "SET full_name = ?, " +

                        "profile_picture = ? " +

                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setString(1, fullName);

            statement.setString(2, profilePicture);

            statement.setInt(3, userId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean rejectExpertRequest(
            int userId
    ) {

        String sql =

                "UPDATE users " +

                        "SET expert_request = false " +

                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}