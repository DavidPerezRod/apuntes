package dabiz.me.cqrs.command.api.commands;

import dabiz.me.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
