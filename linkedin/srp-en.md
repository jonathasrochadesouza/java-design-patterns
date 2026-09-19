---
platform: linkedin
language: en-US
principle: SRP
title: "Single Responsibility Principle"
publishOn: 2026-10-02T10:00:00-03:00
image: assets/diagrams/solid-srp.png
status: manual (paste the text below and attach the image)
---

The class that does everything is also the class nobody wants to touch.

The first SOLID principle, the Single Responsibility Principle, states the obvious we ignore every day: a class should have one clear objective and one clear responsibility.

If you decided a class handles your entity's CRUD, it shouldn't also carry loose utilities, e-mail notifications and PDF generation along the way.

I like to think of a Swiss Army knife. In real life, it's great: it does many things at once. In code, it doesn't work that way. In code, we should buy each of the knife's functions separately, each with its own single responsibility. Got a can opener? One class just for the can opener. Got a cutting blade? A separate component just for the blade.

When we break responsibilities apart, everything becomes much easier to understand, read, maintain and refactor. Yes, the system may "grow" — more classes, more objects, more methods — and that's not a problem.

You know exactly what each class does, and what each method does. When everything lives in one class, it looks more concise and easier to grasp. It isn't.

A clear sign of violation: HR asks for a fix in a class, and Sales asks for another change in that same class. If different audiences request changes to the same place, it's probably doing more than it should — time to break it down.

In the diagram I attached, an Invoice class that created, saved, generated PDFs and sent e-mails became: InvoiceService, InvoiceRepository, PdfGenerator and InvoiceSender. Each with its own responsibility.

So: how many responsibilities does the "fullest" class in your system have?

#SOLID #Java #CleanCode #SoftwareArchitecture #SoftwareEngineering
