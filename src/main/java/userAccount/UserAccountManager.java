package userAccount;

public class UserAccountManager {

    private static UserAccountManager instance;
    private UserAccount currentUser;

    public static synchronized UserAccountManager getInstance() {
        if (instance == null) {
            instance = new UserAccountManager();
        }
        return instance;
    }

    private UserAccountManager(){};
    public UserAccount getLoggedUser(){
        return currentUser;
    }

    public void logUser(UserAccount userAccount){
        this.currentUser = userAccount;
    }

    public void logOut(){
        this.currentUser = null;
    }

}
