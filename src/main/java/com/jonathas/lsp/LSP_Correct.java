package com.jonathas.lsp;

import java.math.BigDecimal;

/**
 * Correct implementation of the Liskov Substitution Principle (LSP) ✅
 *
 * Matches «interface» Account / «interface» WithdrawableAccount in
 * engineering/lsp.excalidraw: the withdraw capability lives in its own
 * narrower interface, so InvestmentAccount implements only what it can honor.
 * Any Account can be substituted freely - and any WithdrawableAccount really
 * can withdraw. No overridden method throws surprises, no instanceof checks.
 */
public class LSP_Correct {
    public static void main(String[] args) {
        // Both implementations are substitutable wherever Account is expected.
        listAccountsDemo();

        // Only withdrawals are handled here - and only through the narrower
        // interface that guarantees the capability (WithdrawableAccount).
        withdrawDemo(new CheckingAccount(new BigDecimal("1000.00")), new BigDecimal("100.00"));
    }

    private static void listAccountsDemo() {
        Account checking = new CheckingAccount(new BigDecimal("1000.00"));
        Account investment = new InvestmentAccount(new BigDecimal("5000.00"));

        System.out.println("Checking balance: " + checking.getBalance());
        System.out.println("Investment balance: " + investment.getBalance());

        // No instanceof, no exceptions: InvestmentAccount never had to fake a
        // withdraw() it cannot support.
        System.out.println("Every Account can be read the same way - LSP holds.");
    }

    private static void withdrawDemo(WithdrawableAccount account, BigDecimal amount) {
        System.out.printf("Client %s is told to withdraw:%n", account.getClass().getSimpleName());
        account.withdraw(amount);
    }
}

// The abstraction read-only clients depend on («interface» Account in the diagram).
interface Account {
    BigDecimal getBalance();
}

// Extends Account with ONLY the capability some accounts support
// («interface» WithdrawableAccount in the diagram). Full substitution holds:
// every WithdrawableAccount IS an Account, and really can withdraw.
interface WithdrawableAccount extends Account {
    void withdraw(BigDecimal amount);
}

// Implementation from the diagram: balance + getBalance() only. No fake withdraw.
class InvestmentAccount implements Account {
    private final BigDecimal balance;

    InvestmentAccount(BigDecimal balance) {
        if (balance == null || balance.signum() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }
}

// Implementation from the diagram: balance + getBalance() + withdraw().
class CheckingAccount implements WithdrawableAccount {
    private final BigDecimal balance;

    CheckingAccount(BigDecimal balance) {
        if (balance == null || balance.signum() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        System.out.printf("Withdrawing %s from checking account (simulation)%n", amount);
    }
}
