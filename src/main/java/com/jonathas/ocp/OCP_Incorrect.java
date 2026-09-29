package com.jonathas.ocp;

import java.math.BigDecimal;

/**
 * Incorrect implementation of the Open/Closed Principle (OCP) ❌
 *
 * PaymentProcessor hardcodes every payment type in a switch. Adding a new
 * payment method (e.g., BankSlips) forces us to MODIFY this class instead of
 * just EXTENDING it with a new implementation - closed for extension, open
 * for modification, the opposite of OCP.
 */
public class OCP_Incorrect {
    public static void main(String[] args) {
        new PaymentProcessor("CREDIT_CARD").process(new BigDecimal("750.00"));
        new PaymentProcessor("PIX").process(new BigDecimal("550.00"));
        new PaymentProcessor("PAYPAL").process(new BigDecimal("120.00"));
    }

    // Violation: this class must be edited every time a new payment method appears.
    static class PaymentProcessor {
        private final String paymentType;

        PaymentProcessor(String paymentType) {
            this.paymentType = paymentType;
        }

        // To add BankSlips we MUST MODIFY this switch: case "BANK_SLIPS" -> ...
        // Each new payment method reopens and risks breaking tested code.
        void process(BigDecimal amount) {
            switch (paymentType) {
                case "CREDIT_CARD" -> System.out.println("Paying " + amount + " with credit card (simulation)");
                case "PIX" -> System.out.println("Paying " + amount + " with Pix (simulation)");
                case "PAYPAL" -> System.out.println("Paying " + amount + " with PayPal (simulation)");
                default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
            }
        }
    }
}
