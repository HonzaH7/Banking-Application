package Banking.Application;

import authentication.AuthenticationAccountRepository;
import authentication.AuthenticationAccountRepositoryStub;
import authentication.AuthenticationService;
import authentication.AuthenticationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import userAccount.UserAccountManager;
import userAccount.UserAccountRepository;
import userAccount.UserAccountRepositoryStub;
import utils.EventBroker.EventBrokerStub;

@SpringBootTest
public class AuthenticationTests {

    private EventBrokerStub eventBrokerStub;
    private AuthenticationService authenticationService;

    private AuthenticationAccountRepository authenticationAccountRepository;

    private UserAccountRepository userAccountRepository;

    private UserAccountManager userAccountManager;

    @BeforeEach
    public void beforeEach(){

        this.eventBrokerStub = new EventBrokerStub();
        this.userAccountManager = UserAccountManager.getInstance();
        this.authenticationAccountRepository = new AuthenticationAccountRepositoryStub();
        this.userAccountRepository = new UserAccountRepositoryStub();
        this.authenticationService = new AuthenticationServiceImp(userAccountRepository, authenticationAccountRepository, userAccountManager);
    }


    @Test
    public void should(){
    }

}
