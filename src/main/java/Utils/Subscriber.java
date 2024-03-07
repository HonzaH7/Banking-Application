package Utils;

public interface Subscriber<T> {
    void update(T event);
}
