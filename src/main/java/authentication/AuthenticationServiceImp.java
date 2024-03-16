package authentication;

import authentication.models.AuthenticationUserEntity;
import authentication.models.AuthenticationUserModel;
import datasource.DataSourceBean;
import io.vavr.control.Try;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import userAccount.UserAccount;
import userAccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import userAccount.UserAccountRepository;
import utils.Nothing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import static jooq.classes.Tables.ACCOUNTS;
import static jooq.classes.Tables.AUTHENTICATION_ACCOUNTS;

public class AuthenticationServiceImp implements AuthenticationService {
    private final UserAccountRepository userAccountRepository;
    private final DataSourceBean dataSourceBean;
    private final UserAccountManager userAccountManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationServiceImp(UserAccountRepository userAccountRepository,
                                    DataSourceBean dataSourceBean,
                                    UserAccountManager userAccountManager) {
        this.userAccountRepository = userAccountRepository;
        this.dataSourceBean = dataSourceBean;
        this.userAccountManager = userAccountManager;
    }

    @Override
    public void createAccount(UserAccount userAccount) {
        this.dataSourceBean.dslContext(dslContext -> this.doCreateAccount(dslContext, userAccount));
    }

    @Override
    public void login(String email, String password){
        this.dataSourceBean.dslContext(dslContext -> this.doLogin(dslContext, email, password));
    }

    @Override
    public void deleteAccount(UserAccount userAccount){
        this.dataSourceBean.dslContext(dslContext -> this.doDelete(dslContext, userAccount));
    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }


    private Nothing doCreateAccount(DSLContext dslContext, UserAccount userAccount) {
        int result = dslContext.insertInto(ACCOUNTS)
                .set(ACCOUNTS.FIRST_NAME, userAccount.getFirstName())
                .set(ACCOUNTS.LAST_NAME, userAccount.getLastName())
                .set(ACCOUNTS.EMAIL, userAccount.getEmail())
                .execute();

        if(result != 1){
            throw new RuntimeException("Failed to insert user account");
        }

        return Nothing.nothing();
    }

    private Nothing doDelete(DSLContext dslContext, UserAccount userAccount) {
        return null;
    }

    private Nothing doLogin(DSLContext dslContext, String email, String password) {
        AuthenticationUserEntity user =
                dslContext.select(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL, AUTHENTICATION_ACCOUNTS.SALT)
                .from(AUTHENTICATION_ACCOUNTS)
                .where(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL.eq(email).and(AUTHENTICATION_ACCOUNTS.PASSWORD.eq(password)))
                .fetchOneInto(AuthenticationUserEntity.class);

        if(user == null){
            throw new RuntimeException("Wrong email or password, please try again");
        }

        AuthenticationUserModel authenticationUserModel = anAuthenticationUser()
                .withAuthenticationEmail(user.getEmail())
                .withSalt(user.getEmail());

        userAccountManager.logUser(authenticationUserModel);

        return Nothing.nothing();
    }

}
