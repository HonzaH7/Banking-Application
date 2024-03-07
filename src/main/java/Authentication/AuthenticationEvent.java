package Authentication;

import Banking.Application.UserAccount;
import Utils.Event;

public class AuthenticationEvent implements Event {

    private final UserAccount userAccount;

    private final Type eventType;

    private AuthenticationEvent(Type eventType, UserAccount userAccount){
        this.eventType = eventType;
        this.userAccount = userAccount;
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType, UserAccount userAccount){
        return new AuthenticationEvent(eventType, userAccount);
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType){
        return new AuthenticationEvent(eventType, null);
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Type getEventType() {
        return eventType;
    }

    public enum Type {
            CREATE_ACCOUNT,
            LOGOUT,
            LOGIN,
            DELETE_ACCOUNT
        }

}
