package authentication.models;

public class AuthenticationUserModel {
    private final String email;

    private final String salt;
    private final String hashedPassword;

    private AuthenticationUserModel(AuthenticationUserModelBuilder builder) {
        this.email = builder.email;
        this.salt = builder.salt;
        this.hashedPassword = builder.hashedPassword;
    }

    public static AuthenticationUserModel anAuthenticationUser() {
        return builder().build();
    }
    private AuthenticationUserModelBuilder toBuilder() {
        return new AuthenticationUserModelBuilder().
                fromInstance(this);
    }

    private static AuthenticationUserModelBuilder builder() {
        return new AuthenticationUserModelBuilder();
    }

    public AuthenticationUserModel withAuthenticationEmail(String email) {
        return toBuilder()
                .withAuthenticationEmail(email)
                .build();
    }

    public AuthenticationUserModel withSalt(String salt) {
        return toBuilder()
                .withSalt(salt)
                .build();
    }

    public AuthenticationUserModel withHashedPassword(String hashedPassword) {
        return toBuilder()
                .withHashedPassword(hashedPassword)
                .build();
    }

    public String getAuthenticationEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    private static class AuthenticationUserModelBuilder {
        private String email;
        private String salt;
        private String hashedPassword;

        private AuthenticationUserModelBuilder fromInstance(AuthenticationUserModel authenticationUserModel) {
            this.email = authenticationUserModel.email;
            this.salt = authenticationUserModel.salt;
            this.hashedPassword = authenticationUserModel.hashedPassword;
            return this;
        }

        private AuthenticationUserModelBuilder withAuthenticationEmail(String email) {
            this.email = email;
            return this;
        }

        private AuthenticationUserModelBuilder withSalt(String salt) {
            this.salt = salt;
            return this;
        }

        private AuthenticationUserModelBuilder withHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }


        private AuthenticationUserModel build() {
            return new AuthenticationUserModel(this);
        }

    }

}
