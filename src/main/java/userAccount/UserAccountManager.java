package userAccount;

public class UserAccountManager {

    private static UserAccountManager instance;
    private UserAccountModel currentUser;

    public static synchronized UserAccountManager getInstance() {
        if (instance == null) {
            instance = new UserAccountManager();
        }
        return instance;
    }

    private UserAccountManager(){};
    public UserAccountModel getLoggedUser(){
        return currentUser;
    }

    public void logUser(UserAccountModel userAccount){
        this.currentUser = userAccount;
    }

    public void logOut(){
        this.currentUser = null;
    }

}
