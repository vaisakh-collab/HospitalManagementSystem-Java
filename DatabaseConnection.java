import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital";

    public static Connection getConnection() throws SQLException, IOException {

        Properties properties = new Properties();

        try (FileInputStream file = new FileInputStream("config.properties")) {
            
            properties.load(file);
        }

        String user = properties.getProperty("DB_USER");
        String password = properties.getProperty("DB_PASSWORD");

        return DriverManager.getConnection(URL, user, password);
    }
}