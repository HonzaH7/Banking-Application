package userAccount;

public class UserAccountModel {

    private final String firstName;
    private final String lastName;

    private final String email;

    private final String password;
    private final Double balance;

    private UserAccountModel(UserAccountBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.balance = builder.balance;
    }

    public static UserAccountModel aUserAccount() {
        return builder().build();
    }
    private UserAccountBuilder toBuilder() {
        return new UserAccountBuilder().
                fromInstance(this);
    }

    private static UserAccountBuilder builder() {
        return new UserAccountBuilder();
    }

    public UserAccountModel withFirstName(String firstName) {
        return toBuilder()
                .withFirstName(firstName)
                .build();
    }

    public UserAccountModel withLastName(String lastName) {
        return toBuilder()
                .withLastName(lastName)
                .build();
    }

    public UserAccountModel withEmail(String email) {
        return toBuilder()
                .withEmail(email)
                .build();
    }

    public UserAccountModel withPassword(String password) {
        return toBuilder()
                .withPassword(password)
                .build();
    }

    public UserAccountModel withBalance(double balance) {
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

        private UserAccountBuilder fromInstance(UserAccountModel userAccount) {
            this.firstName = userAccount.firstName;
            this.lastName = userAccount.lastName;
            this.email = userAccount.email;
            this.password = userAccount.password;
            this.balance = userAccount.balance;
            return this;
        }
        private UserAccountBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        private UserAccountBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        private UserAccountBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        private UserAccountBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserAccountBuilder withBalance(double balance) {
            this.balance = balance;
            return this;
        }

        private UserAccountModel build() {
            return new UserAccountModel(this);
        }

    }

}


