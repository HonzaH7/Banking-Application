package authentication;

import datasource.DataSourceBean;
import org.jooq.*;
import org.jooq.Record;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.beans.factory.annotation.Autowired;
import userAccount.UserAccount;
import userAccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static jooq.classes.Tables.ACCOUNTS;
import static userAccount.UserAccount.aUserAccount;

import org.modelmapper.ModelMapper;
import utils.SqlExceptionUtils;

public class AuthenticationServiceImp implements AuthenticationService {
    private final DataSourceBean dataSourceBean;
    @Autowired
    private final ModelMapper modelMapper;
    private final UserAccountManager userAccountManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationServiceImp(DataSourceBean dataSourceBean, ModelMapper modelMapper, UserAccountManager userAccountManager) {
        this.dataSourceBean = dataSourceBean;
        this.modelMapper = modelMapper;
        this.userAccountManager = userAccountManager;
        this.modelMapper.getConfiguration().addValueReader(new RecordValueReader());
    }

    @Override
    public void createAccount(UserAccount userAccount) {
        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);
            int result = create.insertInto(ACCOUNTS)
                    .set(ACCOUNTS.FIRSTNAME, userAccount.getFirstName())
                    .set(ACCOUNTS.LASTNAME, userAccount.getLastName())
                    .set(ACCOUNTS.EMAIL, userAccount.getEmail())
                    .set(ACCOUNTS.PASSWORD, userAccount.getPassword())
                    .set(ACCOUNTS.BALANCE, 0.0)
                    .execute();
            if (result != 1) {
                throw new SQLException("Failed to insert user account");
            }
        } catch (SQLException e) {
            logger.error("Create account failed", e);
            SqlExceptionUtils.handleException(e);
        }
    }
    @Override
    public void login(String email, String password) {
        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);

            Record userAccountRecord = getUserAccountRecordByEmailAndPassword(email, password, create);

            if (userAccountRecord == null) {
                throw new RuntimeException("Requested user account does not exist.");
            }

            UserAccount userAccount = mapRecordToUserAccount(userAccountRecord);

            userAccountManager.logUser(userAccount);

        } catch (SQLException e) {
            logger.error("Login failed", e);
            SqlExceptionUtils.handleException(e);
        }

    }

    @Override
    public void logout() {
        userAccountManager.logOut();
    }

    private UserAccount mapRecordToUserAccount(Record userAccountRecord) {
        AuthenticationUser authenticationUser = this.modelMapper.map(userAccountRecord, AuthenticationUser.class);
        return aUserAccount()
                .withFirstName(authenticationUser.getFirstName())
                .withLastName(authenticationUser.getLastName())
                .withEmail(authenticationUser.getEmail())
                .withPassword(authenticationUser.getPassword())
                .withBalance(authenticationUser.getBalance());
    }

    private Record getUserAccountRecordByEmailAndPassword(String email, String password, DSLContext create) {
        return create
                .select(ACCOUNTS.fields())
                .from(ACCOUNTS)
                .where(ACCOUNTS.EMAIL.eq(email).and(ACCOUNTS.PASSWORD.eq(password)))
                .fetchOne();
    }

}
