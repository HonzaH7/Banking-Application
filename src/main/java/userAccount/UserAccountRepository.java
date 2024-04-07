package userAccount;

import utils.Nothing;
import io.vavr.control.Try;
import utils.Updatable;

import java.util.Optional;

public interface UserAccountRepository extends Updatable<UserAccountModel> {

    UserAccountModel getAccountByEmail(String email);

    void createAccount(UserAccountModel userAccount);

    Try<Nothing> deleteAccount(UserAccountModel userAccount);
}
