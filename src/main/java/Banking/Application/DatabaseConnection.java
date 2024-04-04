package Banking.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

//    @Value("${spring.datasource.url}")
    private String URL = "jdbc:postgresql://localhost:5432/BankingSystem";

//    @Value("${spring.datasource.username}")
    private String USER = "postgres";

//    @Value("${spring.datasource.password}")
    private String PASSWORD = "05011997";
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            logger.error("Error connecting to database.", e);
            return null;
        }
    }

}
