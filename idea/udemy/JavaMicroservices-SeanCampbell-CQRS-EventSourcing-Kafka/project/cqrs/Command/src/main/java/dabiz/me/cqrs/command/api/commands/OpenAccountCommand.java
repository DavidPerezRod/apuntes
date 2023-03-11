package dabiz.me.cqrs.command.api.commands;

import dabiz.me.cqrs.common.dto.AccountType;
import dabiz.me.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
