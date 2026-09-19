---
platform: medium
language: en-US
principle: ISP
title: "ISP: No Class Should Implement Methods It Never Uses"
subtitle: "Understanding the Interface Segregation Principle with printers, bloated interfaces, and empty methods"
publishOn: 2026-10-24T10:00:00-03:00
tags: [solid, java, clean-code, software-architecture, software-engineering]
canonicalUrl: ""
status: pending
---

That empty method in your class, doing nothing just because "the interface forces it"? That's exactly what ISP is about.

Fourth post in my SOLID study series — now with my understanding of the **Interface Segregation Principle**, the "I" in the acronym. It's the shortest principle to explain and one of the most violated in day-to-day code.

## The principle

The classic definition: **classes should not be forced to depend on methods they do not use.**

ISP guards against so-called **bloated interfaces** — the ones packed with methods that not every client, final class, or implementation actually needs.

When bloated interfaces get implemented, some classes are forced to implement things they don't use. And you already know the outcome from some codebase you've touched: empty methods sitting in the middle of a class, doing nothing, just fulfilling the contract. Or worse: methods that throw exceptions because "it doesn't make sense here."

## The example: the printer

The example from my study is the most visual of all: a printer.

Start with a `Machine` interface that promises everything:

```java
interface Machine {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
}
```

Sounds useful, right? One interface, three operations, done. Except the office's **basic** printer only prints. It's forced to implement `scan()` and `fax()` — features it doesn't have:

```java
class BasicPrinter implements Machine {
    public void print(Document doc) { /* prints */ }
    public void scan(Document doc) { /* ??? no scanner */ }
    public void fax(Document doc) { /* ??? no fax */ }
}
```

That leaves two options, both bad: empty methods (which mislead callers) or exceptions (which blow up at runtime). The class now **depends on methods it doesn't use** — the literal violation of the principle.

## The segregation

The fix is to segregate the interface by capability:

```java
interface Printer {
    void print(Document doc);
}

interface Scanner {
    void scan(Document doc);
}

interface Fax {
    void fax(Document doc);
}
```

Now each class depends only on what it actually uses:

```java
class BasicPrinter implements Printer {
    public void print(Document doc) { /* prints */ }
}

class MultifunctionPrinter implements Printer, Scanner, Fax {
    public void print(Document doc) { /* ... */ }
    public void scan(Document doc) { /* ... */ }
    public void fax(Document doc) { /* ... */ }
}
```

- The basic printer implements only `Printer`.
- The multifunction one implements all three.
- No empty methods, no lying exceptions, no broken contracts.

The conceptual drawing I made during the study — the multifunction printer (printer, scanner, fax) versus the basic printer (printer only):

![Diagram of the Interface Segregation Principle: multifunction printer versus basic printer](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-isp.png)

## The connection to LSP

Notice how ISP talks directly to the previous principle. In LSP, the fix for `InvestmentAccount` was to **separate contracts by capability** — every account checks its balance, only withdrawable accounts implement withdrawals. That *is* interface segregation applied.

A rule of thumb that emerges from both principles: **when a class is forced to implement (or inherit) something it can't fulfill, the contract is too big.** Break the contract, not the class.

## In short

- No class should be forced to depend on, or implement, methods it will never use.
- Bloated interface = clients paying for features they don't use.
- Empty methods "fulfilling the contract" are a code smell, not a solution.
- Several small, focused interfaces > one big, "complete" interface.

## References I used in this study

- *Refactoring Guru* (Design Patterns) book, by Alexander Shvets
- Java Design Patterns course (Udemy), which I used as my study base

Next (and final) post in the series: the **Dependency Inversion Principle** — why your business rules shouldn't even know your database's name.

The diagrams and full notes are in [my study repository on GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
