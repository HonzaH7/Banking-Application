package userAccount;

import utils.Nothing;
import io.vavr.control.Try;
import utils.Updatable;

import java.util.Optional;

public interface UserAccountRepository extends Updatable<UserAccountModel> {

    Optional<UserAccountModel> getAccountByEmail(String email);

    Try<Nothing> createAccount(UserAccountModel userAccount);

    Try<Nothing> deleteAccount(UserAccountModel userAccount);
}
