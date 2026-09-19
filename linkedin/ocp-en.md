---
platform: linkedin
language: en-US
principle: OCP
title: "Open/Closed Principle"
publishOn: 2026-10-08T10:00:00-03:00
image: assets/diagrams/solid-ocp.png
status: manual (paste the text below and attach the image)
---

Shipping a new feature shouldn't mean opening code that already works.

The Open/Closed Principle — the "O" in SOLID — says classes should be open for extension, but closed for modification.

The core idea is simple: prevent existing code from breaking when you introduce something new. You extend the base behavior by creating subclasses (or new implementations) and override only the parts that need to behave differently. Nothing your clients already rely on stops working.

The example from my study: a PaymentProcessor responsible for initiating payment processing — but it knows nothing about credit cards, Pix, PayPal or bank slips. It depends only on the PaymentMethod contract.

Each payment method implements that interface and provides its own logic in the pay() method.

Need to support bank slips, crypto or digital wallets? Just create a new class implementing the interface. Zero changes to PaymentProcessor.

Two important caveats:

1) This principle should not be applied blindly to every scenario. Found a bug in the class? Fix the bug. Don't create a subclass to work around it.

2) As Alexander Shvets puts it in the Refactoring Guru book: "A child class should not be responsible for the problems of its parent class."

And here's the connection to SRP: if you have a well-rounded class with a clear, cohesive scope, it makes no sense to add complexity outside its domain.

Extension is for new behavior. Fixes are for bugs. Don't mix the two.

#SOLID #Java #CleanCode #SoftwareArchitecture #SoftwareEngineering
