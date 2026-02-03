package dabiz.me.cqrs.query;

import dabiz.me.cqrs.core.infrastructure.QueryDispatcher;
import dabiz.me.cqrs.query.api.queries.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class QueryApplication {

    private final QueryDispatcher queryDispatcher;
    private final QueryHandler queryHandler;

    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }

    @PostConstruct
    public void registerHandlers(){
        queryDispatcher.registerHandler(FindAllAccountQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccounBytIdQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
    }
}
