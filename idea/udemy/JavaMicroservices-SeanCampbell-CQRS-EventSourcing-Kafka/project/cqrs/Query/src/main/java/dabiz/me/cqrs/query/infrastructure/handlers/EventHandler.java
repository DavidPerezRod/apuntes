package dabiz.me.cqrs.query.infrastructure.handlers;

import dabiz.me.cqrs.common.event.AccountClosedEvent;
import dabiz.me.cqrs.common.event.AccountOpenedEvent;
import dabiz.me.cqrs.common.event.FundsDepositedEvent;
import dabiz.me.cqrs.common.event.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(AccountClosedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
}
