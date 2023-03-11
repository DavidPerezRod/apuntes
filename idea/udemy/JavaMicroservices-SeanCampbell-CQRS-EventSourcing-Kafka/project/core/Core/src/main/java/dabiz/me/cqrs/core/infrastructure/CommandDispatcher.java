package dabiz.me.cqrs.core.infrastructure;

import dabiz.me.cqrs.core.commands.BaseCommand;
import dabiz.me.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
