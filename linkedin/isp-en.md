---
platform: linkedin
language: en-US
principle: ISP
title: "Interface Segregation Principle"
publishOn: 2026-10-22T10:00:00-03:00
image: assets/diagrams/solid-isp.png
status: manual (paste the text below and attach the image)
---

That empty method in your class, doing nothing just because "the interface forces it"? That's exactly what ISP is about.

The Interface Segregation Principle — the "I" in SOLID — says: classes should not be forced to depend on methods they do not use.

The principle guards against bloated interfaces — the ones packed with methods that not every client, final class or implementation actually needs.

When bloated interfaces get implemented, some classes are forced to implement what they don't use. And you already know the outcome: empty methods sitting in the middle of a class, doing nothing, just fulfilling the contract.

My favorite example is the printer:

A Machine interface with print(), scan() and fax() sounds useful, right? Except the office's basic printer only prints. It's forced to implement scan() and fax() — features it doesn't have — and ends up with two empty methods (or worse: throwing exceptions).

Segregation fixes it:

- Printer with print()
- Scanner with scan()
- Fax with fax()

The basic printer implements only Printer. The multifunction one implements all three. Each class depends only on what it actually uses.

And the sentence to carry into your daily work: no class should be forced to depend on, or implement, methods it will never use.

A big interface is the one you write thinking you're saving code. A segregated interface is the one you thank yourself for six months later.

#SOLID #Java #CleanCode #SoftwareArchitecture #SoftwareEngineering
