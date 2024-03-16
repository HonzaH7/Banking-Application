package Banking.Application;

import authentication.*;
import datasource.DataSourceBean;
import moneyFlow.MoneyFlowEventHandler;
import moneyFlow.MoneyFlowService;
import moneyFlow.MoneyFlowServiceImp;
import org.modelmapper.ModelMapper;
import userAccount.UserAccountManager;
import userAccount.UserAccountRepository;
import userAccount.UserAccountRepositoryImp;
import utils.EventBroker.EventBroker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		EventBroker eventBroker = new EventBroker();

		DataSourceBean dataSourceBean = new DataSourceBean(new DatabaseConnection());
		UserAccountRepository userAccountRepository = new UserAccountRepositoryImp();

		AuthenticationService authenticationService = new AuthenticationServiceImp(userAccountRepository, dataSourceBean, UserAccountManager.getInstance());
		AuthenticationEventHandler authenticationEventHandler = new AuthenticationEventHandler(authenticationService, eventBroker);

		MoneyFlowService moneyFlowService = new MoneyFlowServiceImp(dataSourceBean, UserAccountManager.getInstance());
		MoneyFlowEventHandler moneyFlowEventHandler = new MoneyFlowEventHandler(moneyFlowService, eventBroker);

		UserInterface userInterface = new CommandLineInterface(eventBroker);
		userInterface.run();
	}

}
