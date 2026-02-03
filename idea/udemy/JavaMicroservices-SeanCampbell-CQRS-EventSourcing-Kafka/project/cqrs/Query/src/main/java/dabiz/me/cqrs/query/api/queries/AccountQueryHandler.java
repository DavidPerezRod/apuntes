package dabiz.me.cqrs.query.api.queries;

import dabiz.me.cqrs.core.domain.BaseEntity;
import dabiz.me.cqrs.query.api.dto.EqualityType;
import dabiz.me.cqrs.query.domain.AccountRepository;
import dabiz.me.cqrs.query.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountQueryHandler implements QueryHandler{

    private final AccountRepository accountRepository;
    @Override
    public List<BaseEntity> handle(FindAllAccountQuery query) {
        Iterable<BankAccount> bankAccounts= accountRepository.findAll();
        List<BaseEntity> bankAcountsList= new ArrayList<>();
        bankAccounts.forEach(bankAcountsList::add);
        return bankAcountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccounBytIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());
        if (bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountsList = new ArrayList<>();
        bankAccountsList.add(bankAccount.get());
        return bankAccountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccount = accountRepository.findByAccountHolder(query.getAccountHolder());
        if (bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountsList = new ArrayList<>();
        bankAccountsList.add(bankAccount.get());
        return bankAccountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        return query.getEqualityType() == EqualityType.GREATER_THAN ?
                accountRepository.findByBalanceGreaterThan(query.getBalance()) :
                accountRepository.findByBalanceLessThan(query.getBalance());
    }
}
