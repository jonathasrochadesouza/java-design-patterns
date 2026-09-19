---
platform: medium
language: en-US
principle: LSP
title: "LSP: When Inheritance Becomes a Problem (and How Capability Contracts Fix It)"
subtitle: "Understanding the Liskov Substitution Principle with bank accounts, withdrawals, and an InvestmentAccount that couldn't withdraw"
publishOn: 2026-10-17T10:00:00-03:00
tags: [solid, java, clean-code, software-architecture, software-engineering]
canonicalUrl: ""
status: pending
---

If your child class throws `UnsupportedOperationException`, the problem probably isn't the class.

Third post in my SOLID study series — now with my understanding of the **Liskov Substitution Principle**, the "L" in the acronym. Spoiler: it's the principle that most reveals poorly designed hierarchies.

## The principle

The classic definition: **any subclass must be able to replace its base class without breaking expected behavior.**

That means the subclass must honor the **behavioral contract** defined by the abstraction. Signing the method signatures isn't enough — the child must keep the promise the parent made to callers.

There's also a more technical rule: a method's return type in the subclass must match, or be a subtype of, the return type in the superclass. If the base method returns `Account`, you inherit the `Account` interface all the way down to your service — you don't create a standalone `AccountPCD` that doesn't inherit from `Account` in some form.

And there's the part that, to me, is the essence: LSP also addresses scenarios where **inherited behavior that shouldn't exist** in the new service gets executed anyway, just because inheritance forces it.

## The code smell that gives it all away

In practical terms: if a subclass needs to throw `UnsupportedOperationException`, ignores inherited methods, or alters expected results, **the hierarchy is likely poorly modeled**.

That method which exists only to throw an exception is the hierarchy's cry for help. It's saying: "I was forced to sign a contract I can't fulfill."

## The example: bank accounts

The example I used in my study is quite intuitive.

A standard account allows withdrawals. An investment account might not allow immediate withdrawal. If you make `InvestmentAccount` inherit from an account whose contract promises withdrawals, you're violating the LSP:

```java
class Account {
    BigDecimal getBalance() { /* ... */ }
    void withdraw(BigDecimal amount) { /* ... */ }
}

class InvestmentAccount extends Account {
    @Override
    void withdraw(BigDecimal amount) {
        throw new UnsupportedOperationException(
            "Investment account does not allow immediate withdrawal");
    }
}
```

Does it compile? It compiles. Does it work? Until the first `withdraw()` on an investment account in production.

## The fix: contracts by capability

The way out is to separate contracts by **capability**, not by a "types of account" hierarchy:

```java
interface Account {
    BigDecimal getBalance();
}

interface WithdrawableAccount extends Account {
    void withdraw(BigDecimal amount);
}

class CheckingAccount implements WithdrawableAccount {
    private BigDecimal balance;

    public BigDecimal getBalance() { return balance; }
    public void withdraw(BigDecimal amount) { /* withdrawal rules */ }
}

class InvestmentAccount implements Account {
    private BigDecimal balance;

    public BigDecimal getBalance() { return balance; }
}
```

- **Every account** can check its balance: `Account`.
- **Only withdrawable accounts** implement the withdrawal operation: `WithdrawableAccount`.

And here's the detail I loved most about this design: the investment account **can't even be accidentally passed to the withdrawal service**, because it simply doesn't implement `WithdrawableAccount`. The error is caught at compile time, not in production. The compiler works for you.

The UML I drew during the study, with the contracts separated:

![UML diagram of the Liskov Substitution Principle with Account and WithdrawableAccount](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-lsp.png)

## The sentence to remember

Ultimately, what's really worth remembering is:

> "A derived class must be substitutable for its base class."

If it isn't — if substituting breaks things, throws exceptions, or surprises callers — the problem isn't in the caller. It's in the hierarchy.

## In short

- Subclass replaces base class without breaking anything. No exceptions (literally).
- `UnsupportedOperationException` in an inherited method = poorly modeled hierarchy.
- Prefer small contracts by capability over big hierarchies by type.
- When the compiler can enforce the rule, you don't need to find out in production.

## References I used in this study

- [SOLID Principles in PHP — Accesto](https://accesto.com/blog/solid-php-solid-principles-in-php/)
- [Liskov Substitution Principle in Java — Java Brahman](https://www.javabrahman.com/programming-principles/liskov-substitution-principal-java-example/)
- *Refactoring Guru* (Design Patterns) book, by Alexander Shvets

Next post: the **Interface Segregation Principle** — that empty method in your class doing nothing just because "the interface forces it."

The diagrams and full notes are in [my study repository on GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
