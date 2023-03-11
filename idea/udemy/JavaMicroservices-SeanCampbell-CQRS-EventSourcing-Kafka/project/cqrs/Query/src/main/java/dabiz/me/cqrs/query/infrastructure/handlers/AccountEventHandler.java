package dabiz.me.cqrs.query.infrastructure.handlers;

import dabiz.me.cqrs.common.event.AccountClosedEvent;
import dabiz.me.cqrs.common.event.AccountOpenedEvent;
import dabiz.me.cqrs.common.event.FundsDepositedEvent;
import dabiz.me.cqrs.common.event.FundsWithdrawnEvent;
import dabiz.me.cqrs.query.domain.AccountRepository;
import dabiz.me.cqrs.query.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount = BankAccount.builder().id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var optionalBankAccount = accountRepository.findById(event.getId());

        if (optionalBankAccount.isEmpty()){
            return;
        }

        var bankAccount= optionalBankAccount.get();
        var currentBalance = bankAccount.getBalance();
        var latestBalance = currentBalance+event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var optionalBankAccount = accountRepository.findById(event.getId());

        if (optionalBankAccount.isEmpty()){
            return;
        }

        var bankAccount= optionalBankAccount.get();
        var currentBalance = bankAccount.getBalance();
        var latestBalance = currentBalance-event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }
}
