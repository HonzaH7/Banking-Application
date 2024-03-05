package Banking.Application;

import io.vavr.control.Try;

public interface BankAccountRepository {
    UserAccount loginToAccount(String email, String password);
    Try<String> deleteAccount(UserAccount userAccount);
    boolean doesAccountExist(String username);
    Try<Nothing> createAccount(UserAccount userAccount);

}
