package Banking.Application;

public class UserAccount {

    private final String firstName;
    private final String lastName;

    private final String email;

    private final String password;

    private UserAccount(UserAccountBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
    }

    public static UserAccount aUserAccount() {
        return builder().build();
    }
    private UserAccountBuilder toBuilder() {
        return new UserAccountBuilder().
                fromInstance(this);
    }

    private static UserAccountBuilder builder() {
        return new UserAccountBuilder();
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

    private static class UserAccountBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        private UserAccountBuilder fromInstance(UserAccount userAccount) {
            this.firstName = userAccount.firstName;
            this.lastName = userAccount.lastName;
            this.email = userAccount.email;
            this.password = userAccount.password;
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

        private UserAccount build() {
            return new UserAccount(this);
        }

    }

}


