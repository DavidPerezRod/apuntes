package dabiz.me.cqrs.core.queries;

import dabiz.me.cqrs.core.domain.BaseEntity;

import java.util.List;

@FunctionalInterface
public interface QueryHandlerMethod <T extends BaseQuery>{
    List<BaseEntity> handle(T query);
}
