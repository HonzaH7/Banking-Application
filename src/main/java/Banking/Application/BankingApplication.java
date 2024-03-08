package Banking.Application;

import Authentication.AuthenticationEventHandler;
import Authentication.AuthenticationService;
import Authentication.AuthenticationServiceImp;
import Utils.EventManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;


@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		EventManager eventManager = new EventManager();
		Connection connection = DatabaseConnection.getConnection();
		AuthenticationService authenticationService = new AuthenticationServiceImp(connection, UserAccountManager.getInstance());
		AuthenticationEventHandler authenticationEventHandler = new AuthenticationEventHandler(authenticationService, eventManager);
		UserInterface userInterface = new CommandLineInterface(eventManager);
		userInterface.run();
	}

}
