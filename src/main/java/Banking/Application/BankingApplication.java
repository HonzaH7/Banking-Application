package Banking.Application;

import authentication.AuthenticationEventHandler;
import authentication.AuthenticationService;
import authentication.AuthenticationServiceImp;
import datasource.DataSourceBean;
import moneyFlow.MoneyFlowEventHandler;
import moneyFlow.MoneyFlowService;
import moneyFlow.MoneyFlowServiceImp;
import org.modelmapper.ModelMapper;
import userAccount.UserAccountManager;
import utils.EventBroker.EventBroker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		EventBroker eventBroker = new EventBroker();

		DataSourceBean dataSourceBean = new DataSourceBean();

		AuthenticationService authenticationService = new AuthenticationServiceImp(dataSourceBean, new ModelMapper() ,UserAccountManager.getInstance());
		AuthenticationEventHandler authenticationEventHandler = new AuthenticationEventHandler(authenticationService, eventBroker);

		MoneyFlowService moneyFlowService = new MoneyFlowServiceImp(dataSourceBean, UserAccountManager.getInstance());
		MoneyFlowEventHandler moneyFlowEventHandler = new MoneyFlowEventHandler(moneyFlowService, eventBroker);

		UserInterface userInterface = new CommandLineInterface(eventBroker);
		userInterface.run();
	}

}
