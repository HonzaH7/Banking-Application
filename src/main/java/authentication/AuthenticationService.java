package authentication;

import authentication.models.AuthenticationUserModel;
import userAccount.UserAccountModel;
import utils.Updatable;

public interface AuthenticationService extends Updatable<AuthenticationUserModel> {

    void createAccount(UserAccountModel userAccount);

    void login(String email, String password);

    void deleteAccount(UserAccountModel userAccount, String password);

    void logout();
}
