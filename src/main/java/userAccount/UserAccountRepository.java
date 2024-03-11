package userAccount;

import utils.Nothing;
import io.vavr.control.Try;

import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> getAccount();

    Try<Nothing> createAccount();

    Try<Nothing> deleteAccount();
}
