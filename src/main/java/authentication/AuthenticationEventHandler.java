package authentication;

import userAccount.UserAccountModel;
import utils.EventBroker.EventBroker;
import utils.EventBroker.Subscriber;
import io.vavr.NotImplementedError;

public class AuthenticationEventHandler implements Subscriber<AuthenticationEvent> {

    private final AuthenticationService authenticationService;

    public AuthenticationEventHandler(AuthenticationService authenticationService, EventBroker eventBroker){
        this.authenticationService = authenticationService;
        eventBroker.subscribe(AuthenticationEvent.class, this);
    }


    @Override
    public void update(AuthenticationEvent event) {
        if(event == null){
            return;
        }

        switch (event.getEventType()) {
            case CREATE_ACCOUNT:
                authenticationService.createAccount(event.getUserAccount());
                break;
            case LOGIN:
                UserAccountModel userAccount = event.getUserAccount();
                authenticationService.login(userAccount.getEmail(), userAccount.getPassword());
                break;
            case LOGOUT:
                authenticationService.logout();
                break;
            default:
                throw new NotImplementedError("Not implemented");
        }
    }
}
