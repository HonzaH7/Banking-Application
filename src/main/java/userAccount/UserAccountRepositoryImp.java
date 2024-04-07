package userAccount;


import authentication.models.AuthenticationUserModel;
import datasource.DataSourceBean;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Nothing;
import io.vavr.control.Try;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import static jooq.classes.Tables.ACCOUNTS;
import static jooq.classes.Tables.AUTHENTICATION_ACCOUNTS;
import static userAccount.UserAccountModel.aUserAccount;

public class UserAccountRepositoryImp implements UserAccountRepository {

    private final DataSourceBean dataSourceBean;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountRepositoryImp.class);
    private final Map<Long, UserAccountModel> userAccounts = new ConcurrentHashMap<>();

    public UserAccountRepositoryImp(DataSourceBean dataSourceBean) {
        this.dataSourceBean = dataSourceBean;
    }

    @Override
    public UserAccountModel getAccountByEmail(String email) {
        return this.dataSourceBean.dslContext(dslContext -> this.doGetAccountByEmail(dslContext, email));
    }

    @Override
    public void createAccount(UserAccountModel userAccount) {
        this.dataSourceBean.dslContext(dslContext -> this.doCreateAccount(dslContext, userAccount));
    }

    private Nothing doCreateAccount(DSLContext dslContext, UserAccountModel userAccount) {
        int result = dslContext.insertInto(ACCOUNTS)
                .set(ACCOUNTS.FIRST_NAME, userAccount.getFirstName())
                .set(ACCOUNTS.LAST_NAME, userAccount.getLastName())
                .set(ACCOUNTS.EMAIL, userAccount.getEmail())
                .execute();

        if (isFailure(result)) {
            throw new RuntimeException("Failed to create user account, please try again");
        }

        return Nothing.nothing();
    }

    private UserAccountModel doGetAccountByEmail(DSLContext dslContext, String email) {
        UserAccountEntity userAccount = dslContext.select(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL, AUTHENTICATION_ACCOUNTS.SALT, AUTHENTICATION_ACCOUNTS.PASSWORD)
                .from(AUTHENTICATION_ACCOUNTS)
                .where(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL.eq(email))
                .fetchOneInto(UserAccountEntity.class);

        if(userAccount == null){
            throw new RuntimeException("Account does not exist");
        }

        return aUserAccount().withEmail(userAccount.getEmail())
                .withFirstName(userAccount.getFirstName())
                .withLastName(userAccount.getLastName());
    }

    @Override
    public Try<Nothing> deleteAccount(UserAccountModel userAccount) {
        return null;
    }

    @Override
    public void update(UserAccountModel entity) {

    }

    private boolean isFailure(int result) {
        return result != 1;
    }
}
