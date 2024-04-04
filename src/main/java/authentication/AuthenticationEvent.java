package authentication;

import userAccount.UserAccountModel;
import utils.EventBroker.Event;

public class AuthenticationEvent implements Event {

    private final UserAccountModel userAccount;

    private final Type eventType;

    private final String password;


    private AuthenticationEvent(Type eventType, String password, UserAccountModel userAccount){
        this.eventType = eventType;
        this.password = password;
        this.userAccount = userAccount;
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType, UserAccountModel userAccount){
        return new AuthenticationEvent(eventType, null, userAccount);
    }
    public static AuthenticationEvent anAuthenticationEvent(Type eventType, String password){
        return new AuthenticationEvent(eventType, password, null);
    }

    public static AuthenticationEvent anAuthenticationEvent(Type eventType){
        return new AuthenticationEvent(eventType, null, null);
    }

    public UserAccountModel getUserAccount() {
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
