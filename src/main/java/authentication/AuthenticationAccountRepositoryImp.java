package authentication;

import authentication.models.AuthenticationUserModel;
import datasource.DataSourceBean;
import org.jooq.DSLContext;
import userAccount.UserAccountModel;
import utils.Nothing;

import static jooq.classes.Tables.AUTHENTICATION_ACCOUNTS;

public class AuthenticationAccountRepositoryImp implements AuthenticationAccountRepository{
    private final DataSourceBean dataSourceBean;

    public AuthenticationAccountRepositoryImp(DataSourceBean dataSourceBean) {
        this.dataSourceBean = dataSourceBean;
    }

    @Override
    public void update(AuthenticationUserModel entity) {

    }

    @Override
    public void createAuthenticationAccount(AuthenticationUserModel authenticationUser) {
        this.dataSourceBean.dslContext(dslContext -> this.doCreateAccountAuthenticationAccount(dslContext, authenticationUser));
    }

    @Override
    public AuthenticationUserModel getAuthenticationAccountByEmail(String email) {
        this.dataSourceBean.dslContext(dslContext -> this.doGetAuthenticationAccountByEmail(dslContext, email));
    }

    @Override
    public void deleteAuthenticationAccount(UserAccountModel userAccount) {

    }

    private Nothing doCreateAccountAuthenticationAccount(DSLContext dslContext, AuthenticationUserModel authenticationUser) {
        int isAuthenticationAccountCreated = dslContext.insertInto(AUTHENTICATION_ACCOUNTS)
                .set(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL, authenticationUser.getAuthenticationEmail())
                .set(AUTHENTICATION_ACCOUNTS.PASSWORD, authenticationUser.getHashedPassword())
                .set(AUTHENTICATION_ACCOUNTS.SALT, authenticationUser.getSalt())
                .execute();
    }

    private Nothing doGetAuthenticationAccountByEmail(DSLContext dslContext, String email) {
        dslContext.select(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL, AUTHENTICATION_ACCOUNTS.SALT, AUTHENTICATION_ACCOUNTS.PASSWORD)
                .from(AUTHENTICATION_ACCOUNTS)
                .where(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL.eq(email))
                .fetchOneInto(AuthenticationUserModel.class);
    }

}
