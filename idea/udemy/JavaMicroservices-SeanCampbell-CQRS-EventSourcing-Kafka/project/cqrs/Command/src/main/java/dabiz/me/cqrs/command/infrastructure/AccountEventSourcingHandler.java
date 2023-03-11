package dabiz.me.cqrs.command.infrastructure;

import dabiz.me.cqrs.command.domain.AccountAggregate;
import dabiz.me.cqrs.core.domain.AggregateRoot;
import dabiz.me.cqrs.core.handlers.EventSourcingHandler;
import dabiz.me.cqrs.core.infrastructure.EventStore;
import dabiz.me.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;
    private final EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var accountAggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            accountAggregate.replayEvents(events);
            var latestVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
            accountAggregate.setVersion(latestVersion.get());
        }
        return accountAggregate;
    }

    @Override
    public void republishEvent() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId : aggregateIds) {
            var aggregate = getById(aggregateId);
            if (aggregate != null && aggregate.isActive()) {
                var events = eventStore.getEvents(aggregateId);
                for (var event : events) {
                    eventProducer.produce(event.getClass().getSimpleName(), event);
                }
            }
        }
    }
}