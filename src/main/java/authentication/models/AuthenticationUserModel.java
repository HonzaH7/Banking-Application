package authentication.models;

public class AuthenticationUserModel {
    private final String email;

    private final String salt;

    private AuthenticationUserModel(AuthenticationUserModelBuilder builder) {
        this.email = builder.email;
        this.salt = builder.salt;
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

    public String getEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }

    private static class AuthenticationUserModelBuilder {
        private String email;
        private String salt;

        private AuthenticationUserModelBuilder fromInstance(AuthenticationUserModel authenticationUserModel) {
            this.email = authenticationUserModel.email;
            this.salt = authenticationUserModel.salt;
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


        private AuthenticationUserModel build() {
            return new AuthenticationUserModel(this);
        }

    }

}
