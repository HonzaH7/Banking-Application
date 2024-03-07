package Authentication;

import Banking.Application.UserAccount;

public interface AuthenticationService {

    void createAccount(UserAccount userAccount);

    void login(String name, String password);

    void logout();
}
