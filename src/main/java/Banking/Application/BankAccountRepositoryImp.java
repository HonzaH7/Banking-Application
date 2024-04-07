package Banking.Application;

import userAccount.UserAccountModel;
import utils.Nothing;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utils.Nothing.nothing;
public class BankAccountRepositoryImp implements BankAccountRepository {
    private final Connection CONNECTION;
    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepositoryImp.class);


    @Override
    public Try<Nothing> depositFromUserAccount(double amount, UserAccountModel userAccount) {
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
    public Try<Nothing> withdrawFromUserAccount(double amount, UserAccountModel userAccount) {
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
