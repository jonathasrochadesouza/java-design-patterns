package com.jonathas.lsp;

import java.math.BigDecimal;
import java.util.List;

/**
 * Incorrect implementation of the Liskov Substitution Principle (LSP) ❌
 *
 * The base Account promises both getBalance() and withdraw() to every subtype,
 * but InvestmentAccount cannot honor the withdraw contract - it has nothing to
 * "withdraw from" in the way a checking account does. Its override throws an
 * unexpected exception, so any client holding an Account blows up at runtime
 * when it reaches that subtype. Substitutability is broken: it is impossible to
 * use an InvestmentAccount wherever an Account is expected without special
 * instanceof checks - the opposite of LSP.
 */
public class LSP_Incorrect {
    public static void main(String[] args) {
        // Both accounts travel as plain Account references.
        List<Account> accounts = List.of(
            new CheckingAccount(new BigDecimal("1000.00")),
            new InvestmentAccount(new BigDecimal("5000.00"))
        );

        // The client sees only Account, so it has no clue which subtype secretly
        // cannot withdraw. The FIRST call succeeds...
        for (Account account : accounts) {
            System.out.println("Balance: " + account.getBalance());
        }

        // ...and the SECOND withdraw reaches InvestmentAccount and explodes at
        // runtime. The compiler never warned us, because the base contract lies.
        accounts.get(1).withdraw(new BigDecimal("100.00"));
    }

    // Every subtype inherits a promise some of them cannot keep.
    static class Account {
        private final BigDecimal balance;

        Account(BigDecimal balance) {
            if (balance == null || balance.signum() < 0) {
                throw new IllegalArgumentException("Balance cannot be negative");
            }
            this.balance = balance;
        }

        BigDecimal getBalance() {
            return balance;
        }

        void withdraw(BigDecimal amount) {
            throw new UnsupportedOperationException("This account cannot withdraw");
        }
    }

    // Violation: cannot honor the inherited withdraw() contract, so it overrides
    // it just to throw. Strengthening preconditions / throwing unexpected
    // exceptions breaks every client that trusts the base type.
    static class InvestmentAccount extends Account {
        InvestmentAccount(BigDecimal balance) {
            super(balance);
        }

        @Override
        void withdraw(BigDecimal amount) {
            throw new UnsupportedOperationException("InvestmentAccount does not support withdrawals");
        }
    }

    // The only subtype that actually behaves as Account promised.
    static class CheckingAccount extends Account {
        CheckingAccount(BigDecimal balance) {
            super(balance);
        }

        @Override
        void withdraw(BigDecimal amount) {
            System.out.printf("Withdrawing %s from checking account (simulation)%n", amount);
        }
    }
}
