package dabiz.me.cqrs.command.api.commands;

import dabiz.me.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id){
        super(id);
    }
}
