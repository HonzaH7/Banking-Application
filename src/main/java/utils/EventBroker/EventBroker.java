package utils.EventBroker;

import io.vavr.control.Try;
import utils.Nothing;

public interface EventBroker {

    <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber);

    <T extends Event> Try<Nothing> publish(T event);

}
