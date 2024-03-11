package Banking.Application;

import userAccount.UserAccount;
import utils.Nothing;
import io.vavr.control.Try;

public interface BankAccountRepository {
    Try<Nothing> depositFromUserAccount(double amount, UserAccount userAccount);
    Try<Nothing> withdrawFromUserAccount(double amount, UserAccount userAccount);

}
