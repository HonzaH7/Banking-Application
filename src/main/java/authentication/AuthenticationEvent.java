package authentication;

import userAccount.UserAccount;
import utils.EventBroker.Event;

public class AuthenticationEvent implements Event {

    private final UserAccount userAccount;

    private final Type eventType;

    private final String password;


    private AuthenticationEvent(Type eventType, String password, UserAccount userAccount){
        this.eventType = eventType;
        this.password = password;
        this.userAccount = userAccount;
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType, UserAccount userAccount){
        return new AuthenticationEvent(eventType, null, userAccount);
    }
    public static AuthenticationEvent anAuthenticationEvent(Type eventType, String password){
        return new AuthenticationEvent(eventType, password, null);
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType){
        return new AuthenticationEvent(eventType, null, null);
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Type getEventType() {
        return eventType;
    }

    public String getPassword() {
        return password;
    }

    public enum Type {
            CREATE_ACCOUNT,
            LOGOUT,
            LOGIN,
            DELETE_ACCOUNT
        }

}
