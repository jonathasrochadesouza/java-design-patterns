package com.jonathas.dip;

/**
 * Incorrect implementation of the Dependency Inversion Principle (DIP) ❌
 *
 * The high-level policy (OrderService) constructs and depends on the low-level
 * detail (SmtpEmailSender) directly, so the dependency arrow points from the
 * top down to the concrete implementation. There is no seam: swapping the
 * transport (SendGrid, an SMS gateway, a test fake) means EDITING OrderService,
 * and the order flow cannot be exercised without really talking SMTP.
 * engineering/dip.excalidraw inverts this arrow (see DIP_Correct).
 */
public class DIP_Incorrect {
    public static void main(String[] args) {
        // Nothing can be injected: the policy is hardwired to the detail.
        new OrderService().confirmOrder("customer@example.com");
    }

    // High-level module: knows HOW orders work - and, wrongly, HOW emails go out.
    static class OrderService {
        // Violation: the concrete class is built inside the policy, so
        // OrderService depends on a detail instead of an abstraction.
        private final SmtpEmailSender emailSender = new SmtpEmailSender();

        void confirmOrder(String customerEmail) {
            emailSender.send(customerEmail, "Order confirmed", "Thanks for your purchase!");
        }
    }

    // Low-level detail: the concrete SMTP transport, with no abstraction above it.
    static class SmtpEmailSender {
        void send(String recipient, String subject, String body) {
            System.out.println("SMTP -> " + recipient + " | " + subject + " | " + body + " (simulation)");
        }
    }
}
