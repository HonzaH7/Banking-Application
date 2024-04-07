package Banking.Application;

import authentication.AuthenticationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import userAccount.UserAccountModel;
import utils.EventBroker.Event;
import utils.EventBroker.EventBrokerStub;
import utils.SystemServiceStub;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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
	public void shouldLoginWhenPublishFails(){
		givenNoLoggedUser();
		givenTerminalAnswersInLoggedPhase(TerminalOption.LOGIN, "email@email.com", "password");

		whenApplicationStart();

		verifyEventPublished(
				anAuthenticationEvent(AuthenticationEvent.Type.LOGIN,
						aUserAccount()
								.withEmail("email@email.com")
								.withPassword("password")
				)
		);

		verifyOutputPrintedToTerminal("Successfully logged in.");
	}

	@Test
	public void shouldNotLoginWhenPublishFails(){
		givenNoLoggedUser();
		givenTerminalAnswers(TerminalOption.LOGIN, "email@email.com", "password");
		givenPublishErrorMessage("error message");

		whenApplicationStart();

		verifyEventPublished(
				anAuthenticationEvent(AuthenticationEvent.Type.LOGIN,
						aUserAccount()
								.withEmail("email@email.com")
								.withPassword("password")
				)
		);

		verifyOutputPrintedToTerminal("Failed to login, please try again: [error message]");
	}


	@Test
	public void shouldFailWhenIncorrectCommandSelected(){
		givenNoLoggedUser();
		givenTerminalAnswers(TerminalOption.INVALID_OPTION);

		whenApplicationStart();

		verifyOutputPrintedToTerminal("Invalid option selected.");
	}

	@Test
	public void shouldCreateAnAccount(){
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

	private void givenTerminalAnswersInLoggedPhase(TerminalOption option, String ...parameters) {
		String[] allParamsWithNumber = Stream.concat(Stream.of(String.valueOf(option.getInputNumber())), Arrays.stream(parameters))
				.toArray(String[]::new);

		String concatenatedAnswer = String.join("\n", allParamsWithNumber) + "\n5" +"\n3";

		this.systemServiceStub.setInput(new ByteArrayInputStream(concatenatedAnswer.getBytes()));
	}

	private void givenNoLoggedUser() {
	}

	private void givenLoggedUser(UserAccountModel userAccountModel) {

	}

	private void givenPublishErrorMessage(String errorMessage) {
		eventBrokerStub.setErrorMessage(errorMessage);
	}

	private void verifyOutputPrintedToTerminal(String expected) {
		assertThat(this.systemServiceStub.getMessages()).contains(expected);
	}

	private void verifyEventPublished(Event event) {
		Event lastPublishedEvent = this.eventBrokerStub.getLastPublishedEvent();
		//TODO: deppEquals JESTLI FUNGUJE SPRÁVNĚ?
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
