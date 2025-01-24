import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {
    Connection connection = null;

    // Method to establish database connection
    public Connection connect() {
        String url = "jdbc:mysql://localhost:3306/classschedulerdb";
        String username = "root";
        String password = ""; // Update password if needed
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    // Main method to test the database connection
    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.connect();  // Attempt to connect to the database
    }
}
