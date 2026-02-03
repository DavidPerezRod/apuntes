package dabiz.me.cqrs.core.infrastructure;

import dabiz.me.cqrs.core.queries.BaseQuery;
import dabiz.me.cqrs.core.queries.QueryHandlerMethod;
import dabiz.me.cqrs.core.domain.BaseEntity;
import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);
    <U extends BaseEntity> List<U> send(BaseQuery query);

}
