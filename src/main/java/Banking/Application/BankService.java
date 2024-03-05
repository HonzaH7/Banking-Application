package Banking.Application;

import io.vavr.control.Try;

import static Banking.Application.Nothing.nothing;

public class BankService {
    private final UserAccountManager userAccountManager;
    private final BankAccountRepository bankAccountRepository;

    public BankService(UserAccountManager userAccountManager, BankAccountRepository bankAccountRepository) {
        this.userAccountManager = userAccountManager;
        this.bankAccountRepository = bankAccountRepository;
    }

    public Try<Nothing> loginToBankAccount(String email, String password) {
        UserAccount userAccount = bankAccountRepository.loginToAccount(email, password);
        // jedna tabulka v databazi, kde bude username a password
        // pokud to bude matchovat, tak najit v dalsi separatni tabulce informace o uzivateli
        // pokud ne, vratit null
        if(userAccount == null){
            return Try.failure(new RuntimeException("Failed to login, please try again"));
        }

        userAccountManager.logUser(userAccount);
        return Try.success(nothing());
    }

    public Try<String> createAccount(UserAccount userAccount) {
        boolean doesAccountExist = bankAccountRepository.doesAccountExist(userAccount.getEmail());
        if(doesAccountExist){
            return Try.failure(new RuntimeException("Username already in use"));
        }

        Try<Nothing> result = bankAccountRepository.createAccount(userAccount);

        if(result.isFailure()){
            return Try.failure(new RuntimeException("Something went wrong, please try to create account again"));
        }

        return Try.success("Account created successfully");
    }

    public void logoutFromBankAccount() {
        userAccountManager.logOut();
    }

    public Try<Void> deposit(double amount) {
        if (this.currentAccount != null) {
            if (REQUEST.depositRequest(amount, currentAccount)) {
                double newBalance = this.currentAccount.getBalance() + amount;
                this.currentAccount.setBalance(newBalance);
                System.out.println("Deposit successful. New balance: " + newBalance);
            } else {
                System.out.println("Deposit failed.");
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public Try<Void> withdraw(double amount) {
        UserAccount userAccount = userAccountManager.getLoggedUser();

        if(userAccount == null){
            return "neco";
        }

//         nevim jak to udelat zatim
//         chces tri moznosti
//         1) Failnulo neco interne, totalni fail
//         2) Ucet nema dostatek penez, muzeme nabidnout vybrat ty, ktere tam ma
//         3) Ucet ma dostatek penez, proslo to v poradku

        Try<Boolean> result = bankAccountRepository.withdrawFromUserAccount(userAccount, amount);

        if(result.isFailure()){
            result.castFailure();
        }
    }

    public Try<String> deleteAccount() {
        UserAccount userAccount = userAccountManager.getLoggedUser();

        if(userAccount == null){
            return Try.failure(new RuntimeException("No user is currently logged in to delete an account."));
        }

        Try<String> result = bankAccountRepository.deleteAccount(userAccount);

//        if(result.isFailure()){
//            return result.castToFailure();
//        }

        return result;
    }

}
