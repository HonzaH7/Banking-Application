package authentication;

import authentication.models.AuthenticationUserModel;
import userAccount.UserAccountModel;

public class AuthenticationAccountRepositoryStub implements  AuthenticationAccountRepository{
    @Override
    public void createAuthenticationAccount(AuthenticationUserModel authenticationUser) {

    }

    @Override
    public AuthenticationUserModel getAuthenticationAccountByEmail(String email) {
        return null;
    }

    @Override
    public void deleteAuthenticationAccount(UserAccountModel userAccount) {

    }

    @Override
    public void update(AuthenticationUserModel entity) {

    }
}
