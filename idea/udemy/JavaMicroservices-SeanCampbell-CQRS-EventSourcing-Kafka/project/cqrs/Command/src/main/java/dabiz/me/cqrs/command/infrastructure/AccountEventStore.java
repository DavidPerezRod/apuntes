package dabiz.me.cqrs.command.infrastructure;

import dabiz.me.cqrs.command.domain.AccountAggregate;
import dabiz.me.cqrs.command.domain.EventStoreRepository;
import dabiz.me.cqrs.core.events.BaseEvent;
import dabiz.me.cqrs.core.events.EventModel;
import dabiz.me.cqrs.core.excepions.AggregationNotFoundException;
import dabiz.me.cqrs.core.excepions.ConcurrencyException;
import dabiz.me.cqrs.core.infrastructure.EventStore;
import dabiz.me.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoryRepository;
    private final EventProducer eventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoryRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion!= -1 && eventStream.get(eventStream.size()-1).getVersion()!= expectedVersion){
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for (var event: events){
            version++;
            event.setVersion(version);
            var eventModel= EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoryRepository.save(eventModel);

            if (!persistedEvent.getId().isEmpty()){
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoryRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream==null || eventStream.isEmpty()){
            throw new AggregationNotFoundException("Incorrect account ID provided!");
        }
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateIds() {
        var eventStream= eventStoryRepository.findAll();
        if (eventStream == null || eventStream.isEmpty()){
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }
}
