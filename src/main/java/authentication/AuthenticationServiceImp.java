package authentication;

import datasource.DataSourceBean;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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
        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);
            //TODO: vyřešit result
            int result = create.insertInto(ACCOUNTS).set(userAccount).execute();
        } catch (SQLException e) {
            logger.error("Nasdsa");
            e.printStackTrace();
            handleException(e);
        }

//        try {
//            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (firstName, lastName, email, password, balance) VALUES (?, ?, ?, ?, ?) ");
//            statement.setString(1, userAccount.getFirstName());
//            statement.setString(2, userAccount.getLastName());
//            statement.setString(3, userAccount.getEmail());
//            statement.setString(4, userAccount.getPassword());
//            statement.setDouble(5, 0.0);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Username is already in use, please choose a different one.", e);
//            handleException(e);
//        }
    }

    @Override
    public void login(String email, String password) {
        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);
            Result<UserAccount> result = create
                    .select(ACCOUNT)
                    .from(ACCOUNTS)
                    .where(ACCOUNT.email == email && ACCOUNT.password == password)
                    .fetch();

            if (result.isEmpty()) {
                throw new RuntimeException("Requested user account does not exist.");
            }
            userAccountManager.logUser(result.get(0));
        } catch (SQLException e) {
            logger.error("Nasdsa");
            e.printStackTrace();
            handleException(e);
        }

//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE email = ? AND password = ?");
//            statement.setString(1, email);
//            statement.setString(2, password);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                userAccountManager.logUser(UserAccount.aUserAccount()
//                        .withFirstName(resultSet.getString("firstName"))
//                        .withLastName(resultSet.getString("lastName"))
//                        .withEmail(resultSet.getString("email"))
//                        .withPassword(resultSet.getString("password"))
//                        .withBalance(resultSet.getDouble("balance"))
//                );
//            }
//        } catch (SQLException e) {
//            //TODO: Error změnit na něco normálního
//            throw new NotImplementedError("Not Implemented");
//        }

    }

    @Override
    public void logout() {
        userAccountManager.logOut();
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
}
