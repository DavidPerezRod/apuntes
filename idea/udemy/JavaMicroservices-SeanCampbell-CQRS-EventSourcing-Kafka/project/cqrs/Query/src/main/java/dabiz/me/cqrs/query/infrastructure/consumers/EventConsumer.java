package dabiz.me.cqrs.query.infrastructure.consumers;

import dabiz.me.cqrs.common.event.AccountClosedEvent;
import dabiz.me.cqrs.common.event.AccountOpenedEvent;
import dabiz.me.cqrs.common.event.FundsDepositedEvent;
import dabiz.me.cqrs.common.event.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);
    void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);
    void consume(@Payload FundsWithdrawnEvent event, Acknowledgment ack);
    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
}
