package dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ResourceInteractionDAO {

    public void saveInteraction(

            int studentId,

            int resourceId,

            String interactionType

    ) {

        String sql =

                "INSERT INTO resource_interactions " +

                        "(student_id, resource_id, interaction_type) " +

                        "VALUES (?, ?, ?)";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, studentId);

            statement.setInt(2, resourceId);

            statement.setString(
                    3,
                    interactionType
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}