package dabiz.me.cqrs.command.domain;

import dabiz.me.cqrs.command.api.commands.OpenAccountCommand;
import dabiz.me.cqrs.common.event.AccountClosedEvent;
import dabiz.me.cqrs.common.event.AccountOpenedEvent;
import dabiz.me.cqrs.common.event.FundsDepositedEvent;
import dabiz.me.cqrs.common.event.FundsWithdrawnEvent;
import dabiz.me.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class AccountAggregate extends AggregateRoot {
    private boolean active;
    private double balance;
    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder().
                id(command.getId()).
                accountHolder(command.getAccountHolder()).
                createdDate(new Date()).
                accountType(command.getAccountType()).
                openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event){
        this.id= event.getId();
        this.active= true;
        this.balance= event.getOpeningBalance();
    }
    public void depositFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("Funds cannot be deposited into a closed account!");
        }

        if (amount<= 0){
            throw new IllegalStateException("The deposit amount must be greater than 0!");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event){
        this.id= event.getId();
        this.active= true;
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("Funds cannot be withdraw from a closed account!");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event){
        this.id= event.getId();
        this.balance -= event.getAmount();
    }
    public void closeAccount(){
        if (!this.active){
            throw new IllegalStateException("The Bank account has already been closed!");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        this.id= event.getId();
        this.active = false;
    }
}
