package dabiz.me.cqrs.core.producers;

import dabiz.me.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topicName, BaseEvent event);
}
