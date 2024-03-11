package authentication;

import datasource.DataSourceBean;
import userAccount.UserAccount;
import userAccount.UserAccountManager;
import io.vavr.NotImplementedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AuthenticationServiceImp implements AuthenticationService {
    private final DataSourceBean dataSourceBean;
    private final UserAccountManager userAccountManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationServiceImp(DataSourceBean dataSourceBean, UserAccountManager userAccountManager) {
        this.dataSourceBean = dataSourceBean;
        this.userAccountManager = userAccountManager;
    }

    @Override
    public void createAccount(UserAccount userAccount) {
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
//            Result<Record> result = create.select().from(accounts).fetch();
//
//            for (Record r : result) {
//                String firstName = r.getValue(accounts.FIRST_NAME);
//                String lastName = r.getValue(accounts.LAST_NAME);
//
//                System.out.println(" first name: " + firstName + " last name: " + lastName);
//            }
//        } catch (SQLException e) {
//            logger.error("Nasdsa");
//            e.printStackTrace();
//            handleException(e);
//        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (firstName, lastName, email, password, balance) VALUES (?, ?, ?, ?, ?) ");
            statement.setString(1, userAccount.getFirstName());
            statement.setString(2, userAccount.getLastName());
            statement.setString(3, userAccount.getEmail());
            statement.setString(4, userAccount.getPassword());
            statement.setDouble(5, 0.0);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Username is already in use, please choose a different one.", e);
            handleException(e);
        }
    }

    private void handleException(SQLException e) {
        final String UNIQUE_VIOLATION_SQLSTATE = "23505";

        String errorMessage;

        if (UNIQUE_VIOLATION_SQLSTATE.equals(e.getSQLState())) {
            errorMessage = "The data you are trying to insert already exists.";
        } else {
            errorMessage = "A database error occurred.";
        }
        throw new RuntimeException(errorMessage);
    }

    @Override
    public void login(String email, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE email = ? AND password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userAccountManager.logUser(UserAccount.aUserAccount()
                        .withFirstName(resultSet.getString("firstName"))
                        .withLastName(resultSet.getString("lastName"))
                        .withEmail(resultSet.getString("email"))
                        .withPassword(resultSet.getString("password"))
                        .withBalance(resultSet.getDouble("balance"))
                );
            }
        } catch (SQLException e) {
            throw new NotImplementedError("Not Implemented");
        }

    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }
}
