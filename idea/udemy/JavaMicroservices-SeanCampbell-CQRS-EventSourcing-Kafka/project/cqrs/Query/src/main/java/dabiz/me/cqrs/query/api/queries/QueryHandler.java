package dabiz.me.cqrs.query.api.queries;

import dabiz.me.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {
    List<BaseEntity> handle(FindAllAccountQuery query);
    List<BaseEntity> handle(FindAccounBytIdQuery query);
    List<BaseEntity> handle(FindAccountByHolderQuery query);
    List<BaseEntity> handle(FindAccountWithBalanceQuery query);
}
