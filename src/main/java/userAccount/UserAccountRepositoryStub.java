package userAccount;

import io.vavr.control.Try;
import utils.Nothing;

public class UserAccountRepositoryStub implements UserAccountRepository{
    @Override
    public UserAccountModel getAccountByEmail(String email) {
        return null;
    }

    @Override
    public void createAccount(UserAccountModel userAccount) {

    }

    @Override
    public Try<Nothing> deleteAccount(UserAccountModel userAccount) {
        return null;
    }

    @Override
    public void update(UserAccountModel entity) {

    }
}
