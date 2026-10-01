package com.jonathas.solid;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLID CHECKOUT CHALLENGE -- intentionally bad design, not production code.
 * Run from project root (with Maven):
 *   mvn exec:java -Dexec.mainClass="com.jonathas.solid.SolidCheckoutChallenge"
 * Or compile manually:
 *   javac -d target/classes src/main/java/com/jonathas/solid/SolidCheckoutChallenge.java
 *   java -cp target/classes com.jonathas.solid.SolidCheckoutChallenge
 *
 * MISSION: Refactor into multiple files and apply ALL FIVE SOLID principles.
 * Keep the successful checkout behavior, but fix invalid capability contracts.
 * Do not merely move methods or replace switches with another central registry.
 *
 * BUSINESS RULES:
 * - Amounts are positive integer cents; reject zero/negative totals.
 * - REGULAR pays full price; VIP gets 10% off using integer division.
 * - CARD and PIX support payment and refund. VOUCHER supports payment only.
 * - Save and notify exactly once after a successful payment.
 * - Failed validation/payment must not save or notify.
 * - Persistence and delivery below are fake; do not add real integrations.
 *
 * ACCEPTANCE CHALLENGES:
 * 1. SRP: Pricing, payment, persistence, and notifications change independently.
 * 2. OCP: Add a new payment method and customer pricing policy without editing
 *    the checkout orchestration or existing implementations.
 * 3. LSP: Every implementation honors its advertised contracts. Never make
 *    checkout inspect concrete subtypes to work around unsupported behavior.
 * 4. ISP: A payment-only voucher must not implement refund/report operations.
 * 5. DIP: Test checkout using in-memory fakes, with no concrete integration
 *    constructed inside the checkout orchestrator.
 *
 * TESTS TO WRITE:
 * - REGULAR CARD: 10000 -> 10000 cents; VIP PIX: 10000 -> 9000 cents.
 * - Voucher checkout succeeds; unsupported refund is not advertised.
 * - CARD/PIX refund succeeds through a valid refundable contract.
 * - Invalid totals and payment failures produce no save/notification.
 * - A new method/policy works without modifying checkout orchestration.
 *
 * Start by drawing the dependencies and identifying each violation.
 * Keep this original as a baseline; put your solution in a separate folder.
 */
public class SolidCheckoutChallenge {
    public static void main(String[] args) {
        CheckoutService checkout = new CheckoutService();
        checkout.checkout("order-1", "REGULAR", "CARD", 10000);
        checkout.checkout("order-2", "VIP", "PIX", 10000);
        checkout.checkout("order-3", "REGULAR", "VOUCHER", 5000);

        // Uncomment to expose the broken substitution contract:
        // PaymentProcessor processor = new VoucherProcessor();
        // processor.refund("order-3", 5000);
    }

    interface PaymentOperations {
        void pay(String orderId, int cents);
        void refund(String orderId, int cents);
        String settlementReport();
    }

    // Contract: pay/refund accept positive cents for any supported order.
    static class PaymentProcessor implements PaymentOperations {
        public void pay(String orderId, int cents) {
            System.out.println("CARD paid " + cents + " for " + orderId);
        }
        public void refund(String orderId, int cents) {
            System.out.println("CARD refunded " + cents + " for " + orderId);
        }
        public String settlementReport() { return "CARD settlement"; }
    }

    static class PixProcessor extends PaymentProcessor {
        public void pay(String orderId, int cents) {
            System.out.println("PIX paid " + cents + " for " + orderId);
        }
        public void refund(String orderId, int cents) {
            System.out.println("PIX refunded " + cents + " for " + orderId);
        }
        public String settlementReport() { return "PIX settlement"; }
    }

    static class VoucherProcessor extends PaymentProcessor {
        public void pay(String orderId, int cents) {
            System.out.println("VOUCHER paid " + cents + " for " + orderId);
        }
        public void refund(String orderId, int cents) {
            throw new UnsupportedOperationException("Vouchers cannot be refunded");
        }
        public String settlementReport() {
            throw new UnsupportedOperationException("Vouchers have no settlement report");
        }
    }

    static class CheckoutService {
        private final FakeSqlDatabase database = new FakeSqlDatabase();
        private final FakeEmailClient email = new FakeEmailClient();

        public void checkout(String orderId, String customerType,
                             String paymentType, int totalCents) {
            if (totalCents <= 0) throw new IllegalArgumentException("Invalid total");
            int chargedCents;
            switch (customerType) {
                case "REGULAR": chargedCents = totalCents; break;
                case "VIP": chargedCents = totalCents - totalCents / 10; break;
                default: throw new IllegalArgumentException("Unknown customer type");
            }
            PaymentProcessor processor;
            switch (paymentType) {
                case "CARD": processor = new PaymentProcessor(); break;
                case "PIX": processor = new PixProcessor(); break;
                case "VOUCHER": processor = new VoucherProcessor(); break;
                default: throw new IllegalArgumentException("Unknown payment type");
            }
            processor.pay(orderId, chargedCents);
            database.insert(orderId + ":" + chargedCents);
            email.send("Order " + orderId + " confirmed: " + chargedCents);
        }
    }

    static class FakeSqlDatabase {
        private final List<String> rows = new ArrayList<>();
        public void insert(String row) {
            rows.add(row);
            System.out.println("Saved: " + row);
        }
    }

    static class FakeEmailClient {
        public void send(String message) {
            System.out.println("Email: " + message);
        }
    }
}
