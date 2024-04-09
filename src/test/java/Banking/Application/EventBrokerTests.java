package Banking.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import utils.EventBroker.EventBroker;
import utils.EventBroker.EventBrokerImp;
import utils.LoggerStub;

@SpringBootTest
public class EventBrokerTests {

    private EventBroker eventBroker;

    private Logger logger;

    @BeforeEach
    public void beforeEach(){
        this.logger = new LoggerStub();
        this.eventBroker = new EventBrokerImp(logger);
    }

    @Test
    public void shouldNotReceiveEventWhenNotSubscribed(){
        givenSubscription(notFailingSubscriber, failingSubscriber);

        var result = whenEventPublished(TestEvent.class);


        verifyLoggedMessages();
    }


}
