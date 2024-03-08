package Banking.Application;

import Utils.EventManager;
import Utils.Nothing;
import io.vavr.control.Try;

import java.util.Scanner;

import static Authentication.AuthenticationEvent.Type.*;
import static Authentication.AuthenticationEvent.anAuthenticationEvent;
import static MoneyFlow.MoneyFlowEvent.Type.*;
import static MoneyFlow.MoneyFlowEvent.aMoneyFlowEvent;
import static Banking.Application.UserAccount.aUserAccount;

public class CommandLineInterface implements UserInterface{
    private final Scanner userInput;
    private final EventManager eventManager;

    public CommandLineInterface(EventManager eventManager){
        this.eventManager = eventManager;
        this.userInput = new Scanner(System.in);
    }

    public void run(){
        while (true) {
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
                    return;
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

        Try<Nothing> result = eventManager.publish(
                anAuthenticationEvent(
                        CREATE_ACCOUNT,
                        aUserAccount()
                                .withFirstName(firstname)
                                .withLastName(lastname)
                                .withEmail(email)
                                .withPassword(password)
        ));

        if(result.isFailure()){
            System.out.println("Failed to create account, please try again");
            return;
        }
        System.out.println("Successfully created an account");


    }

    public void loginActionAndFollowUp() {
        System.out.println("Please login.");
        System.out.println("Your email: ");
        String email = userInput.nextLine();
        System.out.println("Your password: ");
        String password = userInput.nextLine();

        Try<Nothing> result = eventManager.publish(anAuthenticationEvent(
                        LOGIN,
                        aUserAccount()
                                .withEmail(email)
                                .withPassword(password)
                )
        );

        if(result.isFailure()){
            System.out.println("Failed to login, please try again.");
            return;
        }
        System.out.println("Successfully logged in");
    }

    public void chosenAction(int action) {
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
            default:
                System.out.println("Invalid option selected.");
        }
    }

    private void logout() {
        Try<Nothing> result = eventManager.publish(anAuthenticationEvent(LOGOUT));
        if(result.isFailure()){
            System.out.println("Blalballbbl");
            return;
        }

        System.out.println("Logged out successfully.");
    }

    private void deleteAccount() {
        System.out.println("Are you sure you want to delete your account? (yes/no)");
        String confirmation = userInput.nextLine();

        if(!confirmation.equalsIgnoreCase("yes")){
            System.out.println("Account deletion cancelled.");
            return;
        }

        Try<Nothing> result = eventManager.publish(anAuthenticationEvent(DELETE_ACCOUNT));

    }

    private void withdraw() {
        System.out.println("Amount you would like to withdraw:");
        double withdrawAmount = Double.parseDouble(userInput.nextLine());
        Try<Nothing> result = eventManager.publish(aMoneyFlowEvent(WITHDRAW, withdrawAmount));
    }

    private void deposit() {
        System.out.println("Amount you would like to deposit:");
        double depositAmount = Double.parseDouble(userInput.nextLine());
        Try<Nothing> result = eventManager.publish(aMoneyFlowEvent(DEPOSIT, depositAmount));
    }

    public void options() {
        System.out.println("What action do you want to do?\n1. Deposit money\n2. Withdraw money\n3. Delete account\n4. Logout");
    }
}

