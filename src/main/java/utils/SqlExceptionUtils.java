package utils;

import java.sql.SQLException;

public class SqlExceptionUtils {

    public static void handleException(SQLException e) {
        final String UNIQUE_VIOLATION_SQLSTATE = "23505";

        String errorMessage;

        if (UNIQUE_VIOLATION_SQLSTATE.equals(e.getSQLState())) {
            errorMessage = "The data you are trying to insert already exists.";
        } else {
            errorMessage = "A database error occurred.";
        }
        throw new RuntimeException(errorMessage);
    }
}