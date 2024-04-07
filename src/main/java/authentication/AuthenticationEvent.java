package authentication;

import userAccount.UserAccountModel;
import utils.EventBroker.Event;

import java.util.Objects;

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


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AuthenticationEvent authenticationEvent = (AuthenticationEvent) o;
//        return userAccount. == person.age && Objects.equals(name, person.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, age);
//    }

    public enum Type {
            CREATE_ACCOUNT,
            LOGOUT,
            LOGIN,
            DELETE_ACCOUNT
        }


}
