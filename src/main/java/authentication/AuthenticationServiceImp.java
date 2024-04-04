package authentication;

import at.favre.lib.bytes.Bytes;
import at.favre.lib.crypto.bcrypt.BCrypt;
import authentication.models.AuthenticationUserEntity;
import authentication.models.AuthenticationUserModel;
import datasource.DataSourceBean;
import io.vavr.NotImplementedError;
import io.vavr.control.Try;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import userAccount.UserAccountModel;
import userAccount.UserAccountManager;
import userAccount.UserAccountRepository;
import utils.Nothing;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static authentication.models.AuthenticationUserModel.anAuthenticationUser;
import static jooq.classes.Tables.ACCOUNTS;
import static jooq.classes.Tables.AUTHENTICATION_ACCOUNTS;

public class AuthenticationServiceImp implements AuthenticationService {
    private final UserAccountRepository userAccountRepository;
    private final DataSourceBean dataSourceBean;
    private final UserAccountManager userAccountManager;

    private final SecureRandom secureRandom;

    private final BCrypt.Hasher hasher;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationServiceImp(UserAccountRepository userAccountRepository,
                                    DataSourceBean dataSourceBean,
                                    UserAccountManager userAccountManager) {
        this.userAccountRepository = userAccountRepository;
        this.dataSourceBean = dataSourceBean;
        this.userAccountManager = userAccountManager;
        this.secureRandom = new SecureRandom();
        this.hasher = BCrypt.with(secureRandom);
    }

    @Override
    public void createAccount(UserAccountModel userAccount) {
        this.dataSourceBean.dslContext(dslContext -> this.doCreateAccount(dslContext, userAccount));
    }

    @Override
    public void login(String email, String password){
        this.dataSourceBean.dslContext(dslContext -> this.doLogin(dslContext, email, password));
    }

    @Override
    public void deleteAccount(UserAccountModel userAccount, String password){
        this.dataSourceBean.dslContext(dslContext -> this.doDelete(dslContext, userAccount, password));
    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }

    @Transactional
    protected Nothing doCreateAccount(DSLContext dslContext, UserAccountModel userAccount) {
        byte[] salt = createSalt();
        byte[] hashedPassword = hasher.hash(3, salt, userAccount.getPassword().getBytes(StandardCharsets.UTF_8));

        int isAuthenticationAccountCreated = dslContext.insertInto(AUTHENTICATION_ACCOUNTS)
                .set(AUTHENTICATION_ACCOUNTS.AUTHENTICATION_EMAIL, userAccount.getEmail())
                .set(AUTHENTICATION_ACCOUNTS.PASSWORD, new String(hashedPassword))
                .set(AUTHENTICATION_ACCOUNTS.SALT, new String(salt))
                .execute();

        if(isFailure(isAuthenticationAccountCreated)){
            throw new RuntimeException("Failed to create user account, please try again");
        }

        Try<Nothing> result =  userAccountRepository.createAccount(userAccount);
//        int result = dslContext.insertInto(ACCOUNTS)
//                .set(ACCOUNTS.FIRST_NAME, userAccount.getFirstName())
//                .set(ACCOUNTS.LAST_NAME, userAccount.getLastName())
//                .set(ACCOUNTS.EMAIL, userAccount.getEmail())
//                .execute();


        return Nothing.nothing();
    }

    private byte[] createSalt() {
       return Bytes.random(16, this.secureRandom).array();
    }

    private Nothing doDelete(DSLContext dslContext, UserAccountModel userAccount, String password) {
        if (!userAccount.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }
        int result = dslContext.deleteFrom(ACCOUNTS)
                .where(ACCOUNTS.EMAIL.eq(userAccount.getEmail()))
                .execute();
        if (isFailure(result)) {
            throw new RuntimeException("Failed to delete user account");
        }

        return Nothing.nothing();
    }

    private boolean isFailure(int result) {
        return result != 1;
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

    @Override
    public void update(AuthenticationUserModel entity) {
        throw new NotImplementedError("Not implemented");
    }
}
