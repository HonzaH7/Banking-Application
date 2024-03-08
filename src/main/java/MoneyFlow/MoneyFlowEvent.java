package MoneyFlow;

import Utils.Event;

public class MoneyFlowEvent implements Event {
    private final double amount;
    private final Type eventType;

    public MoneyFlowEvent(Type eventType, double amount) {
        this.eventType = eventType;
        this.amount = amount;
    }

    public static MoneyFlowEvent aMoneyFlowEvent(Type eventType, double amount) {
        return new MoneyFlowEvent(eventType, amount);
    }

    public double getAmount() {
        return amount;
    }
    public Type getEventType() {
        return eventType;
    }
    public enum Type {
        DEPOSIT,
        WITHDRAW
    }
}
