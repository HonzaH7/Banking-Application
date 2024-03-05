package Banking.Application;

import java.util.Scanner;

import static Banking.Application.UserAccount.aUserAccount;

public class CommandLineInterface implements UserInterface{
    private final Scanner userInput;
    private final UserAccountManager userAccountManager;
    private final BankService bankService; //Chci to tak udělat ?

    public CommandLineInterface(){
        this.userAccountManager = UserAccountManager.getInstance();
        this.userInput = new Scanner(System.in);
    }

    public void run(){
        boolean running = true;
        while (running) {
            System.out.println("Welcome, what do you want to do?");
            System.out.println("1. Login\n2. Create account\n3. Exit");
            int input = Integer.parseInt(userInput.nextLine());
            switch (input) {
                case 1:
                    loginActionAndFollowUp();
                    break;
                case 2:
                    createAnAccount();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        }
    }

    public void createAnAccount() {
        System.out.println("Please create an account.");
        System.out.println("Your firstname: ");
        String firstname = userInput.nextLine();
        System.out.println("Your lastname: ");
        String lastname = userInput.nextLine();
        System.out.println("Your email: ");
        String email = userInput.nextLine();
        System.out.println("Your password: ");
        String password = userInput.nextLine();

        bankService.createAccount(
                        aUserAccount()
                        .withFirstName(firstname)
                        .withLastName(lastname)
                        .withEmail(email)
                        .withPassword(password)
        );
    }

    public void loginActionAndFollowUp() {
        for (int attempts = 0; attempts < 3; ++attempts) {
            System.out.println("Please login.");
            System.out.println("Your username: ");
            String email = userInput.nextLine();
            System.out.println("Your password: ");
            String password = userInput.nextLine();
            if (bankService.loginToBankAccount(email, password) != null) {
                int action;
                do {
//                    if (bank.isAccountDeleted()) {
//                        return;
//                    }
                    options();
                    action = Integer.parseInt(userInput.nextLine());
                    chosenAction(action);
                } while (action != 4);
                return;
            }
            System.out.println("Login failed. Please try again.");
        }
        System.out.println("Login failed after multiple attempts.");
    }

//    public void createAnAccount() {
//        System.out.println("Please create an account.");
//        System.out.println("Your firstname: ");
//        String firstname = userInput.nextLine();
//        System.out.println("Your lastname: ");
//        String lastname = userInput.nextLine();
//        System.out.println("Your username: ");
//        String username = userInput.nextLine();
//        System.out.println("Your password: ");
//        String password = userInput.nextLine();
//
//        bankService.createAccount(userInput, firstname, lastname, username, password);
//    }

    public void chosenAction(int action) {
        switch (action) {
            case 1:
                System.out.println("Amount you would like to deposit:");
                double depositAmount = Double.parseDouble(userInput.nextLine());
//                bankService.deposit(depositAmount);
                break;
            case 2:
                System.out.println("Amount you would like to withdraw:");
                double withdrawAmount = Double.parseDouble(userInput.nextLine());
//                bankService.withdraw(withdrawAmount);
                break;
            case 3:
                System.out.println("Are you sure you want to delete your account? (yes/no)");
                String confirmation = userInput.nextLine();
                if ("yes".equalsIgnoreCase(confirmation)) {
                    bankService.deleteAccount();
                } else {
                    System.out.println("Account deletion cancelled.");
                }
                break;
            case 4:
                System.out.println("Logged out successfully.");
                bankService.logoutFromBankAccount();
                break;
            default:
                System.out.println("Invalid option selected.");
        }
    }

    public void options() {
        System.out.println("What action do you want to do?\n1. Deposit money\n2. Withdraw money\n3. Delete account\n4. Logout");
    }
}

