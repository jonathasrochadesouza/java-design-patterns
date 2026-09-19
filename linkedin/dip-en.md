---
platform: linkedin
language: en-US
principle: DIP
title: "Dependency Inversion Principle"
publishOn: 2026-10-29T10:00:00-03:00
image: assets/diagrams/solid-dip.png
status: manual (paste the text below and attach the image)
---

If your business rule knows your database's name, something is inverted — and not in the right way.

The Dependency Inversion Principle — the "D" in SOLID (and the one that closes the series) — dictates that high-level business rules should not depend directly on low-level technical details: databases, SDKs, external APIs, e-mail providers. Both should depend on an abstraction — typically an interface.

The two classic rules:

- High-level modules should not depend on low-level modules; both should depend on abstractions.
- Abstractions should not depend on details; details should depend on abstractions.

In practice: instead of OrderService calling new MySqlOrderRepository(), it depends on something like OrderRepository. The concrete implementation is provided from the outside — usually by Spring Boot via constructor injection.

Notice the direction change: the business rule doesn't reach down to the technical detail. The technical detail reaches up to the abstraction. That's why it's called "inversion".

The concrete payoff:

- OrderService doesn't change when the database changes.
- You can swap MySQL for DynamoDB without touching business logic.
- And testing with a mock/fake becomes trivial — no database required.

If you use Spring, you probably apply DIP every day without noticing: all that constructor injection the framework does for you is exactly this.

One question to close: does your domain layer import any concrete infrastructure class? If it does, it's time to invert.

#SOLID #Java #SpringBoot #CleanCode #SoftwareArchitecture
