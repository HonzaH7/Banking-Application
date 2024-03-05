package Banking.Application;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Banking.Application.Nothing.nothing;
//Nahradit Connection CONNECTION za ConnectionPool connectionPool;
public class BankAccountRepositoryImp implements BankAccountRepository {
    private final Connection CONNECTION;
    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepositoryImp.class);


    public BankAccountRepositoryImp(Connection CONNECTION) {
        this.CONNECTION = CONNECTION;
    }

    @Override
    public UserAccount loginToAccount(String email, String password) {
        try {
            PreparedStatement loginStatement = CONNECTION
                    .prepareStatement("SELECT * FROM accounts WHERE email = ? AND password = ?");
            loginStatement.setString(1, email);
            loginStatement.setString(2, password);
            ResultSet resultSet = loginStatement.executeQuery();
            if (resultSet.next()) {
                return UserAccount
                        .aUserAccount()
                        .withFirstName(resultSet.getString("firstName"))
                        .withLastName(resultSet.getString("lastName"))
                        .withEmail(resultSet.getString("email"))
                        .withPassword(resultSet.getString("password"));
            } else {
                System.out.println("Wrong username or password");
            }
        } catch (SQLException e) {
        logger.error("Error executing login request", e);
        }
        return null;
    }

    @Override
    public Try<String> deleteAccount(UserAccount userAccount) {
        try {
            if (userAccount != null) {
                PreparedStatement statement = CONNECTION.prepareStatement("DELETE FROM accounts WHERE email = ? AND password = ?");
                statement.setString(1, userAccount.getEmail());
                statement.setString(2, userAccount.getPassword());
                if (statement.executeUpdate() > 0) {
                    return Try.success("Account deleted successfully");
                } else {
                    return Try.failure(new RuntimeException("Deletion wasn't successful"));
                }
            }
        } catch (SQLException e) {
            return Try.failure(new RuntimeException("Deletion request failed"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); // idk
    }

    @Override
    public boolean doesAccountExist(String email) {
        try {
            PreparedStatement checkStatement = CONNECTION
                    .prepareStatement("SELECT COUNT(*) FROM accounts WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            return (resultSet.getInt(1) == 0);
        } catch (SQLException e) {
            logger.error("Error executing email uniqueness check request", e);
        }
        return false;
    }

    @Override
    public Try<Nothing> createAccount(UserAccount userAccount) {
        try {
            PreparedStatement statement = CONNECTION
                    .prepareStatement("INSERT INTO accounts (firstName, lastName, email, password) VALUES (?, ?, ?, ?)");
            statement.setString(1, userAccount.getFirstName());
            statement.setString(2, userAccount.getLastName());
            statement.setString(3, userAccount.getEmail());
            statement.setString(4, userAccount.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            return Try.failure(new RuntimeException("Error executing create account request"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); //idk
    }
}
