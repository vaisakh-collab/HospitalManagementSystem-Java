public class TestDatabaseConnection {
    public static void main(String[] args) {
        if (DatabaseConnection.testConnection()) {
            System.out.println("JDBC connection successful!");
        } else {
            System.out.println("JDBC connection failed. Read the error above.");
        }
    }
}
