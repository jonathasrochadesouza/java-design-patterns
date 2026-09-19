---
platform: medium
language: en-US
principle: OCP
title: "OCP: Ship New Features Without Opening Code That Already Works"
subtitle: "Understanding the Open/Closed Principle with a PaymentProcessor, Pix, bank slips, and a Refactoring Guru quote"
publishOn: 2026-10-10T10:00:00-03:00
tags: [solid, java, clean-code, software-architecture, software-engineering]
canonicalUrl: ""
status: pending
---

Shipping a new feature shouldn't mean opening code that already works.

Second post in my SOLID study series — now with my understanding of the **Open/Closed Principle**, the "O" in the acronym.

## The principle

The classic definition: **classes should be open for extension, but closed for modification.**

The core idea is to prevent existing code from breaking when you introduce new functionality. The class stays "open" in the sense that its base behavior can be extended: you create subclasses or new implementations and override only the parts that need to behave differently. Nothing your clients already rely on stops working.

## The example: PaymentProcessor

The example from my study is a use case every payment system lives through:

Imagine a `PaymentProcessor` service, responsible for initiating payment processing in the system. It doesn't know — and doesn't need to know — the specifics of credit cards, Pix, PayPal, or bank slips. It depends only on the `PaymentMethod` contract:

```java
interface PaymentMethod {
    void pay(BigDecimal amount);
}

class PaymentProcessor {
    private final PaymentMethod paymentMethod;

    PaymentProcessor(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    void process(BigDecimal amount) {
        paymentMethod.pay(amount);
    }
}
```

Each payment method implements the interface and provides its own logic in the `pay()` method:

```java
class CreditCardPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* card logic */ }
}

class PixPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* Pix logic */ }
}

class PayPalPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* PayPal logic */ }
}
```

Now the part that shows the principle working: when a new payment method is needed — bank slip, cryptocurrency, digital wallet — you just create a new class implementing the interface:

```java
class BankSlipPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* bank slip logic */ }
}
```

**Without changing a single line of `PaymentProcessor`.** The system stays open for extension and closed for modification.

The UML I drew during the study, showing `PaymentProcessor` depending only on the contract:

![UML diagram of the Open/Closed Principle with PaymentProcessor and PaymentMethod](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-ocp.png)

## The two caveats nobody tells you

This principle **should not be applied blindly to every scenario**. It wasn't designed to solve everything.

**First:** if you know there's a bug in a class, just fix the bug. Don't create a subclass to work around the problem. Fixing a defect isn't "extending behavior" — it's maintenance, and modifying the class is exactly what you should do.

**Second**, and it's my favorite quote from this study. Alexander Shvets, in the *Refactoring Guru* book (the one everyone recommends when talking about design patterns), puts it like this:

> "A child class should not be responsible for the problems of its parent class."

If the base class has a problem, pushing the solution down to the children just spreads the problem across more places.

## The connection to SRP

In my view, OCP relates directly to the previous principle.

If you have a well-rounded class — with a closed, concrete, cohesive scope, as SRP demands — it makes no sense to start adding complexity outside its domain. A class with a well-defined scope rarely needs to be *modified* to gain new behavior: the new behavior fits in another implementation of the same contract.

SRP and OCP reinforce each other: cohesion inside the class, extension outside it.

## In short

- Open for extension, closed for modification.
- New feature = new implementation of an existing contract, not another `if` inside the class.
- Bugs get fixed in the class; they don't get worked around with a subclass.
- "A child class should not be responsible for the problems of its parent class." — Alexander Shvets

## References I used in this study

- [SOLID Principles Series: Open/Closed Principle — Plain English (Medium)](https://javascript.plainenglish.io/solid-principles-series-part-2-open-closed-principle-ocp-44febc1251a8)
- *Refactoring Guru* (Design Patterns) book, by Alexander Shvets

Next post: the **Liskov Substitution Principle** — why that `UnsupportedOperationException` in the middle of a hierarchy is a cry for help.

The diagrams and full notes are in [my study repository on GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
