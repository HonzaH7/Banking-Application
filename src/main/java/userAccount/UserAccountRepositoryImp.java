package userAccount;

import utils.Nothing;
import io.vavr.control.Try;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserAccountRepositoryImp implements UserAccountRepository {

    private final Map<Long, UserAccountModel> userAccounts = new ConcurrentHashMap<>();
//    private final DataSource

    @Override
    public Optional<UserAccountModel> getAccount() {
        return Optional.empty();
    }

    @Override
    public Try<Nothing> createAccount(UserAccountModel userAccount) {
        //        int result = dslContext.insertInto(ACCOUNTS)
//                .set(ACCOUNTS.FIRST_NAME, userAccount.getFirstName())
//                .set(ACCOUNTS.LAST_NAME, userAccount.getLastName())
//                .set(ACCOUNTS.EMAIL, userAccount.getEmail())
//                .execute();
        return null;
    }

    @Override
    public Try<Nothing> deleteAccount(UserAccountModel userAccount) {
        return null;
    }

    @Override
    public void update(UserAccountModel entity) {

    }
}
