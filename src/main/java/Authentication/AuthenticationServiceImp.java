package Authentication;

import Banking.Application.UserAccount;
import Banking.Application.UserAccountManager;
import io.vavr.NotImplementedError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationServiceImp implements AuthenticationService {
    private final Connection connection;
    private final UserAccountManager userAccountManager;

    public AuthenticationServiceImp(Connection connection, UserAccountManager userAccountManager) {
        this.connection = connection;
        this.userAccountManager = userAccountManager;
    }

    @Override
    public void createAccount(UserAccount userAccount) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (firstName, lastName, email, password, balance) VALUES (?, ?, ?, ?, ?) ");
            statement.setString(1, userAccount.getFirstName());
            statement.setString(2, userAccount.getLastName());
            statement.setString(3, userAccount.getEmail());
            statement.setString(4, userAccount.getPassword());
            statement.setDouble(5, 0.0);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Username is already in use, please choose a different one.");
            throw new RuntimeException(e);
        }
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
