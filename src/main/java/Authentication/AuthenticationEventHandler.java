package Authentication;

import Banking.Application.UserAccount;
import Utils.EventManager;
import Utils.Subscriber;
import io.vavr.NotImplementedError;

public class AuthenticationEventHandler implements Subscriber<AuthenticationEvent> {

    private final AuthenticationService authenticationService;

    public AuthenticationEventHandler(AuthenticationService authenticationService, EventManager eventManager){
        this.authenticationService = authenticationService;
        eventManager.subscribe(AuthenticationEvent.class, this);
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
                UserAccount userAccount = event.getUserAccount();
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
