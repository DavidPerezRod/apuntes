package dabiz.me.cqrs.command.infrastructure;

import dabiz.me.cqrs.core.events.BaseEvent;
import dabiz.me.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void produce(String topicName, BaseEvent event) {
        kafkaTemplate.send(topicName, event);
    }
}
