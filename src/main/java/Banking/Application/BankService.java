package Banking.Application;

import io.vavr.control.Try;

import static Banking.Application.Nothing.nothing;

public class BankService {
    private final UserAccountManager userAccountManager;
    private final BankAccountRepository bankAccountRepository;
//    private final SQLRequests REQUEST;

    public BankService(UserAccountManager userAccountManager, BankAccountRepository bankAccountRepository) {
        this.userAccountManager = userAccountManager;
        this.backAccountRepository = bankAccountRepository;
    }

    public Try<Nothing> loginToBankAccount(String username, String password) {
        UserAccount userAccount = bankAccountRepository.loginToAccount(username, password);
        // jedna tabulka v databazi, kde bude username a password
        // pokud to bude matchovat, tak najit v dalsi separatni tabulce informace o uzivateli
        // pokud ne, vratit null
        if(userAccount == null){
            return Try.failure(new RuntimeException("Failed to login, please try again"));
        }

        userAccountManager.logUser(userAccount);
        return Try.success(nothing());

//        try {
//            ResultSet resultSet = REQUEST.loginRequest(username, password);
//            if (resultSet.next()) {
//                Account account = new Account(
//                        resultSet.getString("firstName"),
//                        resultSet.getString("lastName"),
//                        resultSet.getString("userName"),
//                        resultSet.getString("password")
//                );
//                account.setBalance(resultSet.getDouble("balance"));
//                this.currentAccount = account;
//                System.out.println("Login successful");
//                return account;
//            } else {
//                System.out.println("Wrong username or password! Try again.");
//                return null;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
    //Don't know how to split it correctly. Want to get rid of the try catch

    public Try<String> createAccount(UserAccount userAccount) {
        boolean doesAccountExist = bankAccountRepository.doesAccountExist(userAccount.getUserName());
        if(doesAccountExist){
            return Try.failure("Username already in use");
        }

        Try<Nothing> result = bankAccountRepository.createAccount(userAccount);

        if(result.isFailure()){
            return Try.failure("Something went wrong, please try to create account again");
        }

        return Try.success("Account created successfully");
//        boolean usernameUnique = isUsernameUnique(userName);
//
//        while (!usernameUnique) {
//            System.out.println("Username already in use.\nPlease choose a different username:");
//            userName = userInput.nextLine();
//            usernameUnique = isUsernameUnique(userName);
//        }
//
//        Try<void> result = REQUEST.createAccountRequest(firstName, lastName, userName, password);
//        if(result.isFailure()){
//            return System.out.println("Something went wrong, please try to create account again");
//        }
//
//        System.out.println("Account created successfully.");
    }

    public void logoutFromBankAccount() {
        userAccountManager.logOut();
    }

    public void deposit(double amount) {
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

        // nevim jak to udelat zatim
        // chces tri moznosti
        // 1) Failnulo neco interne, totalni fail
        // 2) Ucet nema dostatek penez, muzeme nabidnout vybrat ty, ktere tam ma
        // 3) Ucet ma dostatek penez, proslo to v poradku

//        Try<Boolean> result = bankAccountRepository.withdrawFromUserAccount(userAccount, amount);
//
//        if(result.isFailure()){
//            result.castFailure();
//        }
//
//        if (this.currentAccount != null) {
//                if (REQUEST.withdrawRequest(amount, currentAccount)) {
//                    double newBalance = this.currentAccount.getBalance() - amount;
//                    this.currentAccount.setBalance(newBalance);
//                    System.out.println("Withdrawal successful. Remaining balance: " + newBalance);
//                } else {
//                    System.out.println("Insufficient funds or withdrawal failed.");
//                }
//        } else {
//            System.out.println("No user is currently logged in.");
//        }
    }

    public Try<Void> deleteAccount() {
        UserAccount userAccount = userAccountManager.getLoggedUser();

        if(userAccount == null){
            return "neco";
        }

        Try<Void> result = bankAccountRepository.deleteAccount(userAccount);

        if(result.isFailure()){
            return result.castFailure();
        }

        return result;
//        if (this.currentAccount != null) {
//                if (REQUEST.deleteAccountRequest(currentAccount)) {
//                    System.out.println("Account deleted successfully.");
//                    this.currentAccount = null;
//                    accountDeleted = true;
//                } else {
//                    System.out.println("Delete account failed.");
//                }
//        } else {
//            System.out.println("No user is currently logged in to delete an account.");
//        }
    }

}
