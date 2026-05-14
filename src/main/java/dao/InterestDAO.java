package dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InterestDAO {

    public List<String> getStudentInterests(
            int studentId
    ) {

        List<String> interests =
                new ArrayList<>();

        String sql =

                "SELECT interests.name " +

                        "FROM student_interests " +

                        "JOIN interests " +

                        "ON student_interests.interest_id = interests.id " +

                        "WHERE student_interests.student_id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, studentId);

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                interests.add(
                        resultSet.getString("name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return interests;
    }

    public boolean addInterestToStudent(

            int studentId,
            int interestId

    ) {

        String sql =

                "INSERT INTO student_interests " +

                        "(student_id, interest_id) " +

                        "VALUES (?, ?)";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, studentId);

            statement.setInt(2, interestId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}