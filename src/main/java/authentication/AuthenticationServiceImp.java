package authentication;

import at.favre.lib.bytes.Bytes;
import at.favre.lib.crypto.bcrypt.BCrypt;
import authentication.models.AuthenticationUserModel;
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

import java.security.SecureRandom;

import static authentication.models.AuthenticationUserModel.anAuthenticationUser;
import static jooq.classes.Tables.ACCOUNTS;

public class AuthenticationServiceImp implements AuthenticationService {
    private final UserAccountRepository userAccountRepository;
    private final AuthenticationAccountRepository authenticationAccountRepository;
    private final UserAccountManager userAccountManager;

    private final SecureRandom secureRandom;

    private final BCrypt.Hasher hasher;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationServiceImp(UserAccountRepository userAccountRepository,
                                    AuthenticationAccountRepository authenticationAccountRepository,
                                    UserAccountManager userAccountManager) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationAccountRepository = authenticationAccountRepository;
        this.userAccountManager = userAccountManager;
        this.secureRandom = new SecureRandom();
        this.hasher = BCrypt.with(secureRandom);
    }

    @Override
    public void createAccount(UserAccountModel userAccount) {
//        this.dataSourceBean.dslContext(dslContext -> this.doCreateAccount(dslContext, userAccount));
    }

    @Override
    public void login(String email, String password){
//        this.dataSourceBean.dslContext(dslContext -> this.doLogin(dslContext, email, password));
    }

    @Override
    public void deleteAccount(UserAccountModel userAccount, String password){
//        this.dataSourceBean.dslContext(dslContext -> this.doDelete(dslContext, userAccount, password));
    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }

    @Transactional
    protected Nothing doCreateAccount(UserAccountModel userAccount) {
        byte[] salt = createSalt();
        byte[] hashedPassword = hasher.hash(4, salt, userAccount.getPassword().getBytes());

        userAccountRepository.createAccount(userAccount);
        authenticationAccountRepository.createAuthenticationAccount(anAuthenticationUser()
                .withAuthenticationEmail(userAccount.getEmail())
                .withSalt(new String(salt))
                .withHashedPassword(new String(hashedPassword))
        );

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
    
    private Nothing doLogin(String email, String password) {
        AuthenticationUserModel user = authenticationAccountRepository.getAuthenticationAccountByEmail(email);

        if(user == null){
            throw new RuntimeException("Wrong email or password, please try again");
        }

        BCrypt.Verifyer verifyer = BCrypt.verifyer();
        BCrypt.Result result = verifyer.verify(password.getBytes(), 4, user.getSalt().getBytes(), user.getHashedPassword().getBytes());


//        AuthenticationUserModel authenticationUserModel = anAuthenticationUser()
//                .withAuthenticationEmail(user.getEmail())
//                .withSalt(user.getEmail());

//        userAccountManager.logUser(authenticationUserModel);

        return Nothing.nothing();
    }
}
