package dabiz.me.cqrs.query.api.queries;

import dabiz.me.cqrs.core.queries.BaseQuery;
import dabiz.me.cqrs.query.api.dto.EqualityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountWithBalanceQuery extends BaseQuery {

    private EqualityType equalityType;
    private double balance;
}
