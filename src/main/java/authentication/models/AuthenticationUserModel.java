package authentication.models;

import userAccount.UserAccount;

public class AuthenticationUserModel {
    private final String email;

    private final String salt;

    private AuthenticationUserModel(UserAccount.UserAccountBuilder builder) {
        this.email = builder.email;
        this.salt = builder.salt;
    }

    public static UserAccount aUserAccount() {
        return builder().build();
    }
    private UserAccount.UserAccountBuilder toBuilder() {
        return new UserAccount.UserAccountBuilder().
                fromInstance(this);
    }

    private static UserAccount.UserAccountBuilder builder() {
        return new UserAccount.UserAccountBuilder();
    }

    public UserAccount withFirstName(String firstName) {
        return toBuilder()
                .withFirstName(firstName)
                .build();
    }

    public UserAccount withLastName(String lastName) {
        return toBuilder()
                .withLastName(lastName)
                .build();
    }

    public UserAccount withEmail(String email) {
        return toBuilder()
                .withEmail(email)
                .build();
    }

    public UserAccount withPassword(String password) {
        return toBuilder()
                .withPassword(password)
                .build();
    }

    public UserAccount withBalance(double balance) {
        return toBuilder()
                .withBalance(balance)
                .build();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    private static class UserAccountBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Double balance;

        private UserAccount.UserAccountBuilder fromInstance(UserAccount userAccount) {
            this.firstName = userAccount.firstName;
            this.lastName = userAccount.lastName;
            this.email = userAccount.email;
            this.password = userAccount.password;
            this.balance = userAccount.balance;
            return this;
        }
        private UserAccount.UserAccountBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        private UserAccount.UserAccountBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        private UserAccount.UserAccountBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        private UserAccount.UserAccountBuilder withPassword(String password) {
            this.password = password;
            return this;
        }


        private UserAccount build() {
            return new UserAccount(this);
        }

    }

}
