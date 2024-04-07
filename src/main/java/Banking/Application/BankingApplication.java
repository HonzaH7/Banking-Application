package Banking.Application;

import authentication.*;
import datasource.DataSourceBean;
import moneyFlow.MoneyFlowEventHandler;
import moneyFlow.MoneyFlowService;
import moneyFlow.MoneyFlowServiceImp;
import userAccount.UserAccountManager;
import userAccount.UserAccountRepository;
import userAccount.UserAccountRepositoryImp;
import utils.EventBroker.EventBrokerImp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.SystemService;
import utils.SystemServiceImp;


@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		SystemService systemService = new SystemServiceImp();

		EventBrokerImp eventBrokerImp = new EventBrokerImp();
		UserAccountManager userAccountManager = UserAccountManager.getInstance();

		DataSourceBean dataSourceBean = new DataSourceBean(new DatabaseConnection());
		UserAccountRepository userAccountRepository = new UserAccountRepositoryImp(dataSourceBean);

		AuthenticationAccountRepository authenticationAccountRepository = new AuthenticationAccountRepositoryImp(dataSourceBean);

		AuthenticationService authenticationService = new AuthenticationServiceImp(userAccountRepository, authenticationAccountRepository, UserAccountManager.getInstance());
		AuthenticationEventHandler authenticationEventHandler = new AuthenticationEventHandler(authenticationService, eventBrokerImp);

		MoneyFlowService moneyFlowService = new MoneyFlowServiceImp(dataSourceBean, UserAccountManager.getInstance());
		MoneyFlowEventHandler moneyFlowEventHandler = new MoneyFlowEventHandler(moneyFlowService, eventBrokerImp);

		UserInterface userInterface = new CommandLineInterface(eventBrokerImp, systemService);
		userInterface.run();
	}

}
