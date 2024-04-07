package authentication;

import authentication.models.AuthenticationUserModel;
import userAccount.UserAccountModel;
import utils.Updatable;

public interface AuthenticationAccountRepository extends Updatable<AuthenticationUserModel> {
    void createAuthenticationAccount(AuthenticationUserModel authenticationUserModel);
    AuthenticationUserModel getAuthenticationAccountByEmail(String email);
    void deleteAuthenticationAccount(UserAccountModel userAccount);
}
