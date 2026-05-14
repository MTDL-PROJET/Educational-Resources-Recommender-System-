package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {

    private static final String URL =
            "jdbc:mysql://localhost:3306/errs_db";

    private static final String USER = "root";

    private static final String PASSWORD = "Root123!";

    public static Connection getConnection() {

        try {

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}