---
platform: medium
language: en-US
principle: DIP
title: "DIP: Your Business Rules Shouldn't Even Know Your Database's Name"
subtitle: "Understanding the Dependency Inversion Principle with OrderService, Spring Boot, and constructor injection"
publishOn: 2026-10-30T10:00:00-03:00
tags: [solid, java, spring-boot, clean-code, software-architecture]
canonicalUrl: ""
status: pending
---

If your business rule knows your database's name, something is inverted — and not in the right way.

Final post in my SOLID study series — closing with my understanding of the **Dependency Inversion Principle**, the "D" in the acronym. And if you use Spring, spoiler: you probably apply it every day without noticing.

## The principle

DIP dictates that **high-level business rules should not depend directly on low-level technical details** — databases, SDKs, external APIs, e-mail providers. Both should depend on an abstraction, typically an interface in Java.

The two classic rules, worth memorizing:

1. **High-level modules should not depend on low-level modules; both should depend on abstractions.**
2. **Abstractions should not depend on details; details should depend on abstractions.**

## The problem: the hidden new inside the service

The example from my study is as common as it gets. An `OrderService` that creates its own dependency:

```java
class OrderService {
    private final MySqlOrderRepository repository =
        new MySqlOrderRepository(); // direct coupling to the detail

    void place(Order order) {
        // order business rules...
        repository.save(order);
    }
}
```

The business rule (what to do with an order) is glued to a technical detail (how and where to persist it). Switching MySQL to DynamoDB? It becomes surgery on the business logic. Testing without a database? Impossible — the `new` is right there inside.

## The inversion

The fix is to **invert the direction of the dependency**:

```java
interface OrderRepository {
    void save(Order order);
}

class OrderService {
    private final OrderRepository repository;

    OrderService(OrderRepository repository) { // abstraction comes from outside
        this.repository = repository;
    }

    void place(Order order) {
        // order business rules...
        repository.save(order);
    }
}
```

Instead of `OrderService` reaching down to `MySqlOrderRepository`, it's `MySqlOrderRepository` that reaches up to the `OrderRepository` contract. The concrete implementation is provided externally — usually by Spring Boot via **constructor injection**:

```java
@Service
class OrderService {
    private final OrderRepository repository;

    OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}

@Repository
class MySqlOrderRepository implements OrderRepository {
    public void save(Order order) { /* JDBC */ }
}
```

That's why it's called *inversion*: the dependency arrow flipped. It's not "dependency injection" for fashion's sake — it's the business rule no longer pointing at the detail.

The UML I drew during the study:

![UML diagram of the Dependency Inversion Principle with OrderService and OrderRepository](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-dip.png)

## The concrete payoff

It's not diagram aesthetics. DIP pays off in three currencies:

- **OrderService doesn't change when the database changes.** MySQL → DynamoDB is a new class implementing `OrderRepository`, zero changes to business logic.
- **Testing becomes trivial.** An in-memory fake satisfies the contract — no running database, no container, no pain.
- **The domain becomes the center.** Infrastructure plugs into the domain, not the other way around.

## The audit question

To close the entire series, one question that works as a thermometer: **does your domain layer import any concrete infrastructure class?**

If it does, it's time to invert. And throughout the series we've seen how the five principles support each other: SRP gives us cohesive classes, OCP keeps them extensible without modification, LSP guarantees fulfillable contracts, ISP trims the contracts down — and DIP flips the last arrow, pointing everything toward the domain.

## In short

- High level and low level depend on abstractions — never on each other.
- Details depend on abstractions, not the other way around.
- Constructor injection (Spring's) is DIP made real.
- Domain at the center, infrastructure plugged in by contract.

## References I used in this study

- *Refactoring Guru* (Design Patterns) book, by Alexander Shvets
- Spring Boot documentation on dependency injection

That wraps up the SOLID series — all five principles, the diagrams, and the full notes are in [my study repository on GitHub](https://github.com/jonathasrochadesouza/java-design-patterns). If the series helped you, share it with that colleague who still maintains the class that does everything.
