package dabiz.me.cqrs.core.commands;

import dabiz.me.cqrs.core.messages.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseCommand extends Message {
    public BaseCommand(String id){
        super(id);
    }
}
