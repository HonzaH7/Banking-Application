package moneyFlow;

import datasource.DataSourceBean;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import userAccount.UserAccount;
import userAccount.UserAccountManager;
import utils.SqlExceptionUtils;

import java.sql.Connection;
import java.sql.SQLException;



public class MoneyFlowServiceImp implements MoneyFlowService{
    private final DataSourceBean dataSourceBean;
    private final UserAccountManager userAccountManager;
    private static final Logger logger = LoggerFactory.getLogger(MoneyFlowService.class);


    public MoneyFlowServiceImp(DataSourceBean dataSourceBean, UserAccountManager userAccountManager) {
        this.dataSourceBean = dataSourceBean;
        this.userAccountManager = userAccountManager;
    }

    @Override
    public void deposit(double amount) {
        UserAccount currentAccount = userAccountManager.getLoggedUser();

        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);

            if (isDepositSuccessful(amount, create, currentAccount)) {
                getUpdatedBalance(create, currentAccount);
            }
        } catch (SQLException e) {
            logger.error("Deposit failed", e);
            SqlExceptionUtils.handleException(e);
        }
    }

    @Override
    public void withdraw(double amount) {
        UserAccount currentAccount = userAccountManager.getLoggedUser();
        try (Connection conn = dataSourceBean.getConnection()) {
            DSLContext create = dataSourceBean.getDSLContext(conn, SQLDialect.POSTGRES);

            if (isSuccessfulWithdraw(amount, create, currentAccount)) {
                getUpdatedBalance(create, currentAccount);
            } else {
                throw new RuntimeException("Insufficient funds or withdrawal failed.");
            }
        } catch (SQLException e) {
            SqlExceptionUtils.handleException(e);
        }
    }

    private boolean isDepositSuccessful(double amount, DSLContext create, UserAccount currentAccount) {
        return updateDepositedBalance(amount, create, currentAccount) > 0;
    }

    private boolean isSuccessfulWithdraw(double amount, DSLContext create, UserAccount currentAccount) {
        return updateWithdrewBalance(amount, create, currentAccount) > 0;
    }

    private int updateDepositedBalance(double amount, DSLContext create, UserAccount currentAccount) {
//        return create.update(ACCOUNTS)
//               .set(ACCOUNTS.BALANCE, ACCOUNTS.BALANCE.add(amount))
//               .where(ACCOUNTS.EMAIL.eq(currentAccount.getEmail()))
//               .execute();
        return 1;
    }

    private int updateWithdrewBalance(double amount, DSLContext create, UserAccount currentAccount) {
//        return create.update(ACCOUNTS)
//                .set(ACCOUNTS.BALANCE, ACCOUNTS.BALANCE.subtract(amount))
//                .where(ACCOUNTS.EMAIL.eq(currentAccount.getEmail()))
//                .and(ACCOUNTS.BALANCE.greaterOrEqual(amount))
//                .execute();
        return 1;
    }

    private void getUpdatedBalance(DSLContext create, UserAccount currentAccount) {
//        Result<Record1<Double>> result = create
//                .select(ACCOUNTS.BALANCE)
//                .from(ACCOUNTS)
//                .where(ACCOUNTS.EMAIL.eq(currentAccount.getEmail()))
//                .fetch();
//
//        Double newBalance = result.get(0).value1();
//        System.out.println("Current balance [" + newBalance + "]");
//        currentAccount.withBalance(newBalance);
    }

}
