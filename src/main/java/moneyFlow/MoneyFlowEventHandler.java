package moneyFlow;

import utils.EventBroker.EventBrokerImp;
import utils.EventBroker.Subscriber;
import io.vavr.NotImplementedError;

public class MoneyFlowEventHandler implements Subscriber<MoneyFlowEvent> {

    private final MoneyFlowService moneyFlowService;

    public MoneyFlowEventHandler(MoneyFlowService moneyFlowService, EventBrokerImp eventBrokerImp) {
        this.moneyFlowService = moneyFlowService;
        eventBrokerImp.subscribe(MoneyFlowEvent.class, this);
    }

    @Override
    public void update(MoneyFlowEvent event) {
        if(event == null) {
            return;
        }

        switch (event.getEventType()) {
            case DEPOSIT:
                moneyFlowService.deposit(event.getAmount());
                break;
            case WITHDRAW:
                moneyFlowService.withdraw(event.getAmount());
                break;
            default:
                throw new NotImplementedError("Not implemented");
        }
    }
}
