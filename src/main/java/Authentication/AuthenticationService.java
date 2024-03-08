package Authentication;

import Banking.Application.UserAccount;

public interface AuthenticationService {

    void createAccount(UserAccount userAccount);

    void login(String email, String password);

    void logout();
}
