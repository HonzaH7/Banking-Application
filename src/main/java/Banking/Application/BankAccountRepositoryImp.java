package Banking.Application;

import userAccount.UserAccount;
import utils.Nothing;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utils.Nothing.nothing;
//Nahradit Connection CONNECTION za ConnectionPool connectionPool;
public class BankAccountRepositoryImp implements BankAccountRepository {
    private final Connection CONNECTION;
    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepositoryImp.class);


    public BankAccountRepositoryImp(Connection CONNECTION) {
        this.CONNECTION = CONNECTION;
    }

    public UserAccount getAccount(String email, String password) {
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

    public Try<String> deleteAccount(UserAccount userAccount) {
        try {
            if (userAccount != null) {
                PreparedStatement statement = CONNECTION.prepareStatement("DELETE FROM accounts WHERE email = ? AND password = ?");
                statement.setString(1, userAccount.getEmail());
                statement.setString(2, userAccount.getPassword());
                if (isAccountDeleted(statement)) {
                    return Try.success("Account deleted successfully");
                } else {
                    return Try.failure(new RuntimeException("Deletion wasn't successful"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing delete account request", e);
            return Try.failure(new RuntimeException("Deletion request failed"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); // idk
    }

    private boolean isAccountDeleted(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate() > 0;
    }


    public boolean doesAccountExist(String email) {
        try {
            PreparedStatement checkStatement = CONNECTION
                    .prepareStatement("SELECT COUNT(*) FROM accounts WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            return isEmailInDatabase(resultSet);
        } catch (SQLException e) {
            logger.error("Error executing email uniqueness check request", e);
        }
        return false;
    }

    private static boolean isEmailInDatabase(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1) > 0;
    }

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
            logger.error("Error executing create account request", e);
            return Try.failure(new RuntimeException("Error executing create account request"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); //idk
    }

    @Override
    public Try<Nothing> depositFromUserAccount(double amount, UserAccount userAccount) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE email = ?");
            statement.setDouble(1, amount);
            statement.setString(2, userAccount.getEmail());
            if (isBalanceUpdated(statement)) {
                Try.success("Deposit successful.");
                System.out.println("Account balance: " + retrieveBalance(userAccount.getEmail()));
            } else {
                System.out.println("Deposit failed");
            }
        } catch (SQLException e) {
            logger.error("Error executing deposit request", e);
            return Try.failure(new RuntimeException("Error executing deposit request"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); //idk
    }

    private static boolean isBalanceUpdated(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate() > 0;
    }

    private double retrieveBalance(String email) {
        try {
            double balance = 0.0;
            PreparedStatement balanceStatement = CONNECTION.prepareStatement("SELECT balance FROM accounts WHERE email = ?");
            balanceStatement.setString(1, email);
            ResultSet resultSet = balanceStatement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
            return balance;
        } catch (SQLException e) {
            logger.error("Error executing retrieveBalance request");
        }
        System.out.println("Something went wrong");
        return 0;
    }

    @Override
    public Try<Nothing> withdrawFromUserAccount(double amount, UserAccount userAccount) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE email = ? AND balance >= ?");
            statement.setDouble(1, amount);
            statement.setString(2, userAccount.getEmail());
            statement.setDouble(3, amount);
            if (isBalanceUpdated(statement)) {
                System.out.println("Withdraw successful.");
                System.out.println("Account balance: " + retrieveBalance(userAccount.getEmail()));
                //return Try.success(true);
            } else {
                System.out.println("Withdraw failed");
                //return Try.success(false);
            }
        } catch (SQLException e) {
            logger.error("Error executing withdraw request", e);
            return Try.failure(new RuntimeException("Error executing withdraw request"));
        }
        return Try.failure(new RuntimeException(String.valueOf(nothing()))); //idk
    }


}
