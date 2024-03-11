package userAccount;

import utils.Nothing;
import io.vavr.control.Try;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserAccountRepositoryImp implements UserAccountRepository {

    private final Map<Long, UserAccount> userAccounts = new ConcurrentHashMap<>();
//    private final DataSource

    @Override
    public Optional<UserAccount> getAccount() {
        return Optional.empty();
    }

    @Override
    public Try<Nothing> createAccount() {
        return null;
    }

    @Override
    public Try<Nothing> deleteAccount() {
        return null;
    }
}
