package Banking.Application;

import userAccount.UserAccountManager;
import utils.EventBroker.EventBroker;
import utils.Nothing;
import io.vavr.control.Try;
import utils.SystemService;

import java.util.Scanner;

import static authentication.AuthenticationEvent.Type.*;
import static authentication.AuthenticationEvent.anAuthenticationEvent;
import static moneyFlow.MoneyFlowEvent.Type.*;
import static moneyFlow.MoneyFlowEvent.aMoneyFlowEvent;
import static userAccount.UserAccountModel.aUserAccount;

public class CommandLineInterface implements UserInterface{
    private Scanner userInput;
    private final EventBroker eventBroker;

    private final SystemService systemService;
    private boolean isLoggedIn = false;


    public CommandLineInterface(EventBroker eventBroker, SystemService systemService){
        this.eventBroker = eventBroker;
        this.systemService = systemService;
//        this.isLoggedIn = userAccountManager.getLoggedUser() != null;
    }

    public void run(){
        this.userInput = new Scanner(systemService.getInput());

        while (true) {
            if (isLoggedIn) {
                afterLoginOptions();
            } else {
                systemService.println("Welcome, what do you want to do?");
                systemService.println("1. Login\n2. Create account\n3. Exit");
                int input = Integer.parseInt(userInput.nextLine());
                switch (input) {
                    case 1:
                        loginActionAndFollowUp();
                        break;
                    case 2:
                        createAnAccount();
                        break;
                    case 3:
                        systemService.println("Goodbye!");
                        return;
                    default:
                        systemService.println("Invalid option selected.");
                }
            }
        }
    }

    private void createAnAccount() {
        systemService.println("Please create an account.");
        systemService.println("Your firstname: ");
        String firstname = userInput.nextLine();
        systemService.println("Your lastname: ");
        String lastname = userInput.nextLine();
        systemService.println("Your email: ");
        String email = userInput.nextLine();
        systemService.println("Your password: ");
        String password = userInput.nextLine();

        Try<Nothing> result = eventBroker.publish(
                anAuthenticationEvent(
                        CREATE_ACCOUNT,
                        aUserAccount()
                                .withFirstName(firstname)
                                .withLastName(lastname)
                                .withEmail(email)
                                .withPassword(password)
        ));

        if(result.isFailure()){
            systemService.println("Failed to create account: [" + result.getCause().getMessage() + "]");
            return;
        }
        systemService.println("Successfully created an account");


    }

    private void loginActionAndFollowUp() {
        systemService.println("Please login.");
        systemService.println("Your email: ");
        String email = userInput.nextLine();
        systemService.println("Your password: ");
        String password = userInput.nextLine();

        Try<Nothing> result = eventBroker.publish(anAuthenticationEvent(
                        LOGIN,
                        aUserAccount()
                                .withEmail(email)
                                .withPassword(password)
                )
        );

        if(result.isFailure()){
            systemService.println("Failed to login, please try again: [" + result.getCause().getMessage() + "]");
            return;
        }
        systemService.println("Successfully logged in");
        isLoggedIn = true;
    }

    private void afterLoginOptions() {
        systemService.println("What action do you want to do?");
        systemService.println("1. Deposit money");
        systemService.println("2. Withdraw money");
        systemService.println("3. Delete account");
        systemService.println("4. Logout");
        systemService.println("5. Exit");
        int action = Integer.parseInt(userInput.nextLine());
        switch (action) {
            case 1:
                deposit();
                break;
            case 2:
                withdraw();
                break;
            case 3:
                deleteAccount();
                break;
            case 4:
                logout();
                break;
            case 5:
                systemService.println("Goodbye!");
                return;
            default:
                systemService.println("Invalid option selected.");
        }
    }

    private void logout() {
        Try<Nothing> result = eventBroker.publish(anAuthenticationEvent(LOGOUT));
        if(result.isFailure()){
            systemService.println("See you later Alligator");
            return;
        }

        systemService.println("Logged out successfully.");
        isLoggedIn = false;
    }

    private void deleteAccount() {
        systemService.println("Are you sure you want to delete your account? Please type your password");
        String password = userInput.nextLine();

        Try<Nothing> result = eventBroker.publish(anAuthenticationEvent(DELETE_ACCOUNT, password));

        if (result.isFailure()) {
            systemService.println("Something went wrong, please try again");
            return;
        }

        systemService.println("Successfully deleted your account");
        isLoggedIn = false;
    }

    private void deposit() {
        systemService.println("Amount you would like to deposit:");
        double depositAmount = Double.parseDouble(userInput.nextLine());
        Try<Nothing> result = eventBroker.publish(aMoneyFlowEvent(DEPOSIT, depositAmount));

        if (result.isFailure()) {
            systemService.println("Something went wrong, please try again");
            return;
        }

        systemService.println("Successfully deposited [" + depositAmount + "] to your account");
    }

    private void withdraw() {
        systemService.println("Amount you would like to withdraw:");
        double withdrawAmount = Double.parseDouble(userInput.nextLine());
        Try<Nothing> result = eventBroker.publish(aMoneyFlowEvent(WITHDRAW, withdrawAmount));

        if (result.isFailure()) {
            systemService.println("Insufficient funds or withdrawal failed. Please try again.");
            return;
        }

        systemService.println("Successfully withdrew [" + withdrawAmount + "] from your account");
    }

}

