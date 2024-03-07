package Authentication;

import Banking.Application.UserAccount;
import io.vavr.NotImplementedError;

public class AuthenticationServiceImp implements AuthenticationService{
    @Override
    public void createAccount(UserAccount userAccount) {
        throw new NotImplementedError("Not Implemented");
    }

    @Override
    public void login(String name, String password) {
        throw new NotImplementedError("Not Implemented");
    }

    @Override
    public void logout() {
        throw new NotImplementedError("Not Implemented");
    }
}
