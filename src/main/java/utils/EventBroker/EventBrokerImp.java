package utils.EventBroker;

import utils.Nothing;
import io.vavr.control.Try;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import static utils.Nothing.nothing;

public class EventBrokerImp implements EventBroker {
    private final Map<Class<? extends Event>, List<Subscriber<? extends Event>>> subscribersMap = new LinkedHashMap<>();

    public <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber){
        List<Subscriber<? extends Event>> subscribers = subscribersMap.get(eventType);
        if(subscribers == null){
            subscribersMap.put(eventType, List.of(subscriber));
            return;
        }
        subscribers.add(subscriber);
    }

    public <T extends Event> Try<Nothing> publish(T event) {
        List<Subscriber<? extends Event>> subscribersToEvent = subscribersMap.get(event.getClass());
        if (subscribersToEvent == null) {
            return Try.success(nothing());
        }

        List<String> errors = subscribersToEvent.stream()
                .map(subscriber -> publishToSubscriber(subscriber, event))
                .filter(Objects::nonNull)
                .toList();

        if(!errors.isEmpty()){
           return Try.failure(new RuntimeException("Publish failed: "  + String.join(",", errors)));
        }

        return Try.success(nothing());
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> String publishToSubscriber(Subscriber<? extends Event> subscriber, T event) {
        try {
            ((Subscriber<T>)subscriber).update(event);
            return null;
        } catch (Exception e){
            return e.getMessage();
        }
    }

}
