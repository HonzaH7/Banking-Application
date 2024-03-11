package utils.EventBroker;

public interface Subscriber<T> {
    void update(T event);
}
