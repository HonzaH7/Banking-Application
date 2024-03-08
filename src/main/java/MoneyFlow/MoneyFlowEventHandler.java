package MoneyFlow;

import Utils.EventManager;
import Utils.Subscriber;
import io.vavr.NotImplementedError;

public class MoneyFlowEventHandler implements Subscriber<MoneyFlowEvent> {

    private final MoneyFlowService moneyFlowService;

    public MoneyFlowEventHandler(MoneyFlowService moneyFlowService, EventManager eventManager) {
        this.moneyFlowService = moneyFlowService;
        eventManager.subscribe(MoneyFlowEvent.class, this);
    }

    @Override
    public void update(MoneyFlowEvent event) {
        if(event == null) {
            return;
        }

        switch (event.getEventType()) {
            case DEPOSIT:
                moneyFlowService.deposit(event.getAmount());
            case WITHDRAW:
                moneyFlowService.withdraw(event.getAmount());
            default:
                throw new NotImplementedError("Not implemented");
        }
    }
}
