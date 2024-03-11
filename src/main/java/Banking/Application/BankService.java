package Banking.Application;

import userAccount.UserAccount;
import userAccount.UserAccountManager;
import utils.Nothing;
import io.vavr.control.Try;

import static utils.Nothing.nothing;

public class BankService {
    private final UserAccountManager userAccountManager;
    private final BankAccountRepository bankAccountRepository;

    public BankService(UserAccountManager userAccountManager, BankAccountRepository bankAccountRepository) {
        this.userAccountManager = userAccountManager;
        this.bankAccountRepository = bankAccountRepository;
    }


    public Try<Nothing> deposit(double amount) {
        UserAccount userAccount = userAccountManager.getLoggedUser();

        if (userAccount == null) {
            return Try.failure(new RuntimeException(("No user is currently logged in.")));
        }

        bankAccountRepository.depositFromUserAccount(amount, userAccount);

        return Try.success(nothing());
    }

    public Try<Nothing> withdraw(double amount) {
        UserAccount userAccount = userAccountManager.getLoggedUser();

        if(userAccount == null){
            return Try.failure(new RuntimeException(("No user is currently logged in.")));
        }

//         nevim jak to udelat zatim
//         chces tri moznosti
//         1) Failnulo neco interne, totalni fail
//         2) Ucet nema dostatek penez, muzeme nabidnout vybrat ty, ktere tam ma
//         3) Ucet ma dostatek penez, proslo to v poradku

        bankAccountRepository.withdrawFromUserAccount(amount, userAccount);

        return Try.success(nothing());
//        if(result.isFailure()){
//            result.castFailure();
//        }
    }

}
