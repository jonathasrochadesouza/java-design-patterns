package com.jonathas.dip;

/**
 * Correct implementation of the Dependency Inversion Principle (DIP) ✅
 *
 * Matches OrderService / «interface» EmailSender / SmtpEmailSender in
 * engineering/dip.excalidraw: the high-level policy depends only on the
 * EmailSender abstraction ("depende de"), and the concrete SmtpEmailSender
 * ("implementa") is injected from outside. High-level and low-level now both
 * depend on the abstraction - the dependency arrow points to it from both
 * sides, which is the inversion.
 */
public class DIP_Correct {
    public static void main(String[] args) {
        // Production wiring: the concrete transport is chosen OUTSIDE the policy.
        new OrderService(new SmtpEmailSender()).confirmOrder("customer@example.com");

        // NEW detail (beyond the diagram): a different transport flows through
        // the same abstraction with ZERO changes to OrderService - and a test
        // could inject a fake exactly the same way.
        new OrderService(new SendGridEmailSender()).confirmOrder("customer@example.com");
    }
}

// The abstraction both sides depend on («interface» EmailSender in the diagram).
interface EmailSender {
    void send(String recipient, String subject, String body);
}

// High-level module from the diagram: holds only the abstraction, which is
// injected via constructor - never constructed inside the policy.
class OrderService {
    private final EmailSender emailSender;

    OrderService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    void confirmOrder(String customerEmail) {
        emailSender.send(customerEmail, "Order confirmed", "Thanks for your purchase!");
    }
}

// Low-level detail from the diagram: concrete SMTP transport ("implementa").
class SmtpEmailSender implements EmailSender {
    @Override
    public void send(String recipient, String subject, String body) {
        System.out.println("SMTP -> " + recipient + " | " + subject + " | " + body + " (simulation)");
    }
}

// NEW detail (beyond the diagram): added later without touching OrderService -
// proof that details are replaceable when both sides depend on the abstraction.
class SendGridEmailSender implements EmailSender {
    @Override
    public void send(String recipient, String subject, String body) {
        System.out.println("SendGrid -> " + recipient + " | " + subject + " | " + body + " (simulation)");
    }
}
