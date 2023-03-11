package dabiz.me.cqrs.core.handlers;

import dabiz.me.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler <T> {
    void save(AggregateRoot aggregate);
    T getById(String id);

    void republishEvent();
}

