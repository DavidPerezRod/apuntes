package dabiz.me.cqrs.common.event;

import dabiz.me.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FundsWithdrawnEvent extends BaseEvent{
    private double amount;
}
