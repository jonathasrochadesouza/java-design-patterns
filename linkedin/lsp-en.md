---
platform: linkedin
language: en-US
principle: LSP
title: "Liskov Substitution Principle"
publishOn: 2026-10-15T10:00:00-03:00
image: assets/diagrams/solid-lsp.png
status: manual (paste the text below and attach the image)
---

If your child class throws UnsupportedOperationException, the problem probably isn't the class.

The Liskov Substitution Principle — the "L" in SOLID — states that any subclass must be able to replace its base class without breaking expected behavior.

The subclass must honor the behavioral contract defined by the abstraction. A method's return type in the child must match, or be a subtype of, the return type in the parent. And above all: a child must never be forced to execute inherited behavior that makes no sense for it.

In practical terms: if a subclass needs to throw UnsupportedOperationException, ignores inherited methods, or alters expected results, the hierarchy is poorly modeled.

The classic example from my study: bank accounts.

A standard account allows withdrawals. An investment account might not allow immediate withdrawal. Making InvestmentAccount inherit from an account whose contract promises withdrawal violates the LSP — and the usual "fix" is that withdraw() method which throws an exception. The famous code smell.

The way out is to split contracts by capability:

- Every account implements Account (check balance).
- Only withdrawable accounts implement WithdrawableAccount (withdraw operation).

The best part: the investment account can't even be accidentally passed to the withdrawal service, because it simply doesn't implement WithdrawableAccount. The compiler stops it for you.

If you had to sum up LSP in one sentence: "A derived class must be substitutable for its base class."

If it isn't, the problem is in the hierarchy — not in the caller.

#SOLID #Java #CleanCode #SoftwareArchitecture #SoftwareEngineering
