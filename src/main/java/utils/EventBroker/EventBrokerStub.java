package utils.EventBroker;

import io.vavr.control.Try;
import utils.Nothing;

public class EventBrokerStub implements EventBroker{
    private Event lastPublishedEvent;
    private String errorMessage;
    @Override
    public <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {

    }

    @Override
    public <T extends Event> Try<Nothing> publish(T event) {
        lastPublishedEvent = event;
        if (errorMessage != null) {
            return Try.failure(new RuntimeException(errorMessage));
        }
        return Try.success(Nothing.nothing());
    }

    public Event getLastPublishedEvent() {
        return lastPublishedEvent;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
