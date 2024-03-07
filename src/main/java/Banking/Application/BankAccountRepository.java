package Banking.Application;

import Utils.Nothing;
import io.vavr.control.Try;

public interface BankAccountRepository {
    Try<Nothing> depositFromUserAccount(double amount, UserAccount userAccount);
    Try<Nothing> withdrawFromUserAccount(double amount, UserAccount userAccount);

}
