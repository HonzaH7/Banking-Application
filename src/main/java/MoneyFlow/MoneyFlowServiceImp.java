package MoneyFlow;

import Banking.Application.UserAccount;
import Banking.Application.UserAccountManager;
import io.vavr.NotImplementedError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MoneyFlowServiceImp implements MoneyFlowService{
    private final Connection connection;
    private final UserAccountManager userAccountManager;

    public MoneyFlowServiceImp(Connection connection, UserAccountManager userAccountManager) {
        this.connection = connection;
        this.userAccountManager = userAccountManager;
    }

    @Override
    public void deposit(double amount) {
        UserAccount currentAccount = userAccountManager.getLoggedUser();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE email = ?");
            statement.setDouble(1, amount);
            statement.setString(2, currentAccount.getEmail());
            if (statement.executeUpdate() > 0) {
                double newBalance = currentAccount.getBalance() + amount;
                currentAccount.withBalance(newBalance);
                System.out.println("Deposit successful. New balance: " + newBalance);
            }
        } catch (SQLException e) {
            throw new NotImplementedError("Not Implemented");
        }
    }

    @Override
    public void withdraw(double amount) {
        UserAccount currentAccount = userAccountManager.getLoggedUser();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE email = ? AND balance >= ?");
            statement.setDouble(1, amount);
            statement.setString(2, currentAccount.getEmail());
            statement.setDouble(3, amount);
            if (statement.executeUpdate() > 0) {
                double newBalance = currentAccount.getBalance() - amount;
                currentAccount.withBalance(newBalance);
                System.out.println("Withdrawal successful. Remaining balance: " + newBalance);
            } else {
                System.out.println("Insufficient funds or withdrawal failed.");
            }
        } catch (SQLException e) {
            throw new NotImplementedError("Not Implemented");
        }
    }
}
