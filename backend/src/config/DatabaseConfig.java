package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // JDBC URL for connecting to the database
    private static final String JDBC_URL = "jdbc:mysql://localhost/quizapp";
    
    // Database username
    private static final String USERNAME = "root";
    
    // Database password
    private static final String PASSWORD = "password";

    /**
     * Get a connection to the database.
     * 
     * @return Connection object representing the database connection
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the MySQL JDBC driver class is not found
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Register the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Establish a connection to the database using the JDBC URL, username, and password
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
