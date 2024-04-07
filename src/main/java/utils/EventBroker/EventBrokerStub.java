package utils.EventBroker;

import io.vavr.control.Try;
import utils.Nothing;

public class EventBrokerStub implements EventBroker{
    private Event lastPublishedEvent;
    @Override
    public <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {

    }

    @Override
    public <T extends Event> Try<Nothing> publish(T event) {
        lastPublishedEvent = event;
        return Try.success(Nothing.nothing());
    }

    public Event getLastPublishedEvent() {
        return lastPublishedEvent;
    }

}
