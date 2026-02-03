package dabiz.me.cqrs.common.event;

import dabiz.me.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class AccountClosedEvent extends BaseEvent {
}
