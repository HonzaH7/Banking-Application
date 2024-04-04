package Banking.Application;

import userAccount.UserAccountModel;
import utils.Nothing;
import io.vavr.control.Try;

public interface BankAccountRepository {
    Try<Nothing> depositFromUserAccount(double amount, UserAccountModel userAccount);
    Try<Nothing> withdrawFromUserAccount(double amount, UserAccountModel userAccount);

}
