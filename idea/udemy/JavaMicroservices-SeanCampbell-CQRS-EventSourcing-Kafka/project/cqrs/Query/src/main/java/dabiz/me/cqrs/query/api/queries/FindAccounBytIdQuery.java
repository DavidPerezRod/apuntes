package dabiz.me.cqrs.query.api.queries;

import dabiz.me.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccounBytIdQuery extends BaseQuery {
    private String id;
}
