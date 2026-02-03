package dabiz.me.cqrs.query.api.dto;

import dabiz.me.cqrs.common.dto.BaseResponse;
import dabiz.me.cqrs.query.domain.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountLookupResponse extends BaseResponse {
    private List<BankAccount> accountList;

    public AccountLookupResponse(String message) {
        super(message);
    }
}
