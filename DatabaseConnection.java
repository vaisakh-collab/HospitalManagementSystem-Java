import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "config.properties";

    private DatabaseConnection() {
        // Utility class
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();

        try (FileInputStream file = new FileInputStream(CONFIG_FILE)) {
            properties.load(file);
        } catch (IOException e) {
            throw new IOException(
                "Cannot read config.properties. Create it in the project folder using config.properties.example.",
                e
            );
        }

        String url = properties.getProperty(
            "DB_URL", "jdbc:mysql://localhost:3306/hospital"
        ).trim();
        String user = properties.getProperty("DB_USER", "").trim();
        String password = properties.getProperty("DB_PASSWORD", "");

        if (user.isEmpty()) {
            throw new SQLException("DB_USER is missing in config.properties.");
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection.isValid(3);
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
}
