---
platform: medium
language: en-US
principle: SRP
title: "SRP in Practice: Why the Class That Does Everything Is the One Nobody Wants to Touch"
subtitle: "Understanding the Single Responsibility Principle with Java, a Swiss Army knife, and an Invoice that did everything"
publishOn: 2026-10-04T10:00:00-03:00
tags: [solid, java, clean-code, software-architecture, software-engineering]
canonicalUrl: ""
status: pending
---

The class that does everything is also the class nobody wants to touch.

This is the first post in a series where I share my studies on the five SOLID principles — not the textbook definition, but my understanding after studying, drawing UMLs, and breaking examples apart. Starting with the S: **Single Responsibility Principle**.

## The principle

The classic definition: **a class should have one clear objective and one clear responsibility.**

Sounds obvious. And that's exactly why we ignore it every day.

If you decided a class handles your entity's CRUD, it shouldn't also carry loose utilities, e-mail notifications, and PDF generation along the way. Each of those things is a responsibility — and different responsibilities live in different classes.

## The Swiss Army knife analogy

I like to think of a Swiss Army knife.

In real life, it's great: it does everything at once — opens cans, cuts, tightens screws. But that's not how code works. In code, we should buy each of the knife's functions separately, each with its own single responsibility:

- Got a can opener? One class just for the can opener.
- Got a cutting blade? A separate component just for the blade.

It's not about removing features. It's about every feature having its own place in the world.

## The example: an Invoice that did everything

The classic example is an `Invoice` class responsible for... pretty much the entire invoice lifecycle:

```java
class Invoice {
    void createInvoice() { /* ... */ }
    void saveInvoice() { /* JDBC in here */ }
    void generatePdf() { /* PDF generation in here */ }
    void sendInvoiceByEmail() { /* SMTP in here */ }
}
```

Does it work? It works. Except four responsibilities live in the same class: create, persist, format, and notify.

Applying SRP, it becomes four units, each with its own reason to exist:

```java
class InvoiceService {
    Invoice create() { /* ... */ }
}

interface InvoiceRepository {
    void save(Invoice invoice);
}

class PdfGenerator {
    byte[] generate(Invoice invoice) { /* ... */ }
}

class InvoiceSender {
    void sendByEmail(Invoice invoice, String recipient) { /* ... */ }
}
```

The diagram below shows the before and after I drew during my study — the incorrect UML with the do-it-all class, and the correct one with responsibilities separated:

![UML diagram of the Single Responsibility Principle: before and after](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-srp.png)

## "But then the system gets bigger!"

Yes, and that's not a problem.

When we break responsibilities apart, everything becomes much easier to understand, read, maintain, and refactor. The system may "grow" — more classes, more objects, more methods — and that's a cost worth paying.

You know exactly what each class does, and what each method does. When everything lives in one class, it *looks* more concise and easier to grasp. It isn't.

## The violation sign I see the most

This is the part that most changed how I read code: SRP isn't only about methods — it's about **actors**.

If HR asks for a fix in a class, and Sales asks for another change in that *same* class, it's probably doing more than it should. Different audiences requesting changes to the same place is the symptom. The cure is breaking the class into units aligned with each actor.

In the second example from my diagram, an `Order` class that created orders, saved them to the database, updated inventory, and sent confirmation e-mails ends up being pulled in four directions: the DBA, the inventory dev, the e-mail dev, and whoever owns the order flow. Four actors, one class, four reasons for merge conflicts.

## In short

- One class, one responsibility, one actor.
- Breaking responsibilities makes the system grow — and that's healthy.
- If two different business areas request changes to the same class, it's time to split.
- The Swiss Army knife is great in your pocket, terrible in your code.

## References I used in this study

- [Deep Dive Single Responsibility Principle — Omar Ismail](https://www.linkedin.com/pulse/deep-dive-single-responsibility-principle-omar-ismail)
- [Writing Maintainable Java Code: SRP — Nonstop.io](https://blog.nonstopio.com/writing-maintainable-java-code-single-responsibility-principle-solid-part-1-6ee0dc9d9f79)
- [Understanding SOLID Principles: SRP — Evertop](https://www.evertop.pl/en/understanding-solid-principles-single-responsibility/)

Next post in the series: the **Open/Closed Principle** — why shipping a new feature shouldn't mean opening code that already works.

The diagrams and full notes are in [my study repository on GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
