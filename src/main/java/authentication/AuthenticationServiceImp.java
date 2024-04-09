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
    private final Logger logger;

    public AuthenticationServiceImp(UserAccountRepository userAccountRepository,
                                    AuthenticationAccountRepository authenticationAccountRepository,
                                    UserAccountManager userAccountManager,
                                    Logger logger) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationAccountRepository = authenticationAccountRepository;
        this.userAccountManager = userAccountManager;
        this.secureRandom = new SecureRandom();
        this.hasher = BCrypt.with(secureRandom);
        this.logger = logger;
    }

    public AuthenticationServiceImp(UserAccountRepository userAccountRepository,
                                    AuthenticationAccountRepository authenticationAccountRepository,
                                    UserAccountManager userAccountManager) {
        this(userAccountRepository, authenticationAccountRepository, userAccountManager, LoggerFactory.getLogger(AuthenticationServiceImp.class));
    }

    @Override
    public void createAccount(UserAccountModel userAccount) {
        byte[] salt = createSalt();
        byte[] hashedPassword = hasher.hash(4, salt, userAccount.getPassword().getBytes());

        userAccountRepository.createAccount(userAccount);
        authenticationAccountRepository.createAuthenticationAccount(anAuthenticationUser()
                .withAuthenticationEmail(userAccount.getEmail())
                .withSalt(new String(salt))
                .withHashedPassword(new String(hashedPassword))
        );
    }

    @Override
    public void login(String email, String password){
        AuthenticationUserModel user = authenticationAccountRepository.getAuthenticationAccountByEmail(email);

        if(user == null){
            throw new RuntimeException("Wrong email or password, please try again");
        }

        // tohle nejspis pujde extrahovat do metody, delas to stejne v tom delete, musis porovnat dve hesla
        // - jedno nezahashovane (co prislo od uzivatele jako parametr) a druhe zahashovane - z databaze
        BCrypt.Verifyer verifyer = BCrypt.verifyer();
        BCrypt.Result result = verifyer.verify(password.getBytes(), 4, user.getSalt().getBytes(), user.getHashedPassword().getBytes());

        // pokud bude validni, tak prihlasit do toho user manazeru
        // pokud ne, nic se nedeje - meli by jsme veci logovat, takze pridat logger a logovat veci
    }

    @Override
    public void deleteAccount(UserAccountModel userAccount, String password){
        if (!userAccount.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }

        AuthenticationUserModel authenticationUser = authenticationAccountRepository.getAuthenticationAccountByEmail(userAccount.getEmail());

        // porovnat hesla - prichozi heslo neni hash, to v databazi je hash toho saltu a hesla

        // pokud sedi, musis smazat jak authentication account (tohle myslim musis prvni, dle toho databazoveho schematu), tak user account
    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }

    private byte[] createSalt() {
       return Bytes.random(16, this.secureRandom).array();
    }

}
