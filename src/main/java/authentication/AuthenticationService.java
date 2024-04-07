package authentication;

import userAccount.UserAccountModel;

public interface AuthenticationService {

    void createAccount(UserAccountModel userAccount);

    void login(String email, String password);

    void deleteAccount(UserAccountModel userAccount, String password);

    void logout();
}
