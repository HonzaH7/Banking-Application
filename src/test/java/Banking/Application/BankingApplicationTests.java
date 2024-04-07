package Banking.Application;

import authentication.AuthenticationEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import userAccount.UserAccountManager;
import userAccount.UserAccountModel;
import utils.EventBroker.Event;
import utils.EventBroker.EventBrokerStub;
import utils.SystemServiceStub;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static authentication.AuthenticationEvent.anAuthenticationEvent;
import static userAccount.UserAccountModel.aUserAccount;

@SpringBootTest
class BankingApplicationTests {

	private CommandLineInterface commandLineInterface;
	private EventBrokerStub eventBrokerStub;

	private SystemServiceStub systemServiceStub;

	@BeforeEach
	public void beforeEach(){
		this.eventBrokerStub = new EventBrokerStub();
		this.systemServiceStub = new SystemServiceStub();
		this.commandLineInterface = new CommandLineInterface(eventBrokerStub, systemServiceStub);
	}

	@Test
	public void shouldFailWhenIncorrectCommandSelected(){
		givenNoLoggedUser();
		givenTerminalAnswers(TerminalOption.INVALID_OPTION);

		verifyOutputPrintedToTerminal("Invalid option selected.");
	}

	@Test
	public void shouldPublishToEventBrokerWhenAllAccountIsCreated(){
		givenNoLoggedUser();
		givenTerminalAnswers(TerminalOption.CREATE_ACCOUNT, "firstName", "lastName", "email@email.com", "password");

		whenApplicationStart();

		verifyEventPublished(
				anAuthenticationEvent(AuthenticationEvent.Type.CREATE_ACCOUNT,
					aUserAccount()
							.withFirstName("firstName")
							.withLastName("lastName")
							.withEmail("email@email.com")
							.withPassword("password")
					)
		);


	}

	private void whenApplicationStart() {
		this.commandLineInterface.run();
	}

	private void givenTerminalAnswers(TerminalOption option, String ...parameters) {
		String[] allParamsWithNumber = Stream.concat(Stream.of(String.valueOf(option.getInputNumber())), Arrays.stream(parameters))
				.toArray(String[]::new);

		String concatenatedAnswer = String.join("\n", allParamsWithNumber) + "\n3";

		this.systemServiceStub.setInput(new ByteArrayInputStream(concatenatedAnswer.getBytes()));
	}

	private int getInputNumberFromEventType(AuthenticationEvent.Type type){
		return switch (type){
			case CREATE_ACCOUNT ->  2;
			case LOGIN -> 1;
			default -> throw new IllegalArgumentException("Unsupported event type: " + type);
		};
	}

	private void givenNoLoggedUser() {
	}

	private void givenLoggedUser(UserAccountModel userAccountModel) {

	}

	private void verifyEventPublished(Event event) {
		Event lastPublishedEvent = this.eventBrokerStub.getLastPublishedEvent();
		Objects.deepEquals(event, lastPublishedEvent);
	}

	public enum TerminalOption {
		LOGIN(1),
		CREATE_ACCOUNT(2),
		EXIT(3),
		INVALID_OPTION(Integer.MAX_VALUE);

		private final int inputNumber;
		TerminalOption(int inputNumber) {
			this.inputNumber = inputNumber;
		}

		public int getInputNumber() {
			return inputNumber;
		}

	}

}
