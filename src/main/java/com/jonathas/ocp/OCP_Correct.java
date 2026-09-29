package com.jonathas.ocp;

import java.math.BigDecimal;

/**
 * Correct implementation of the Open/Closed Principle (OCP) ✅
 *
 * PaymentProcessor depends on the PaymentMethod abstraction. New payment
 * methods are added as new implementations of the interface, without modifying
 * any existing class - open for extension, closed for modification.
 */
public class OCP_Correct {
    public static void main(String[] args) {
        // The three implementations from the diagram.
        new PaymentProcessor(new CreditCardPayment()).process(new BigDecimal("750.00"));
        new PaymentProcessor(new PixPayment()).process(new BigDecimal("550.00"));
        new PaymentProcessor(new PayPalPayment()).process(new BigDecimal("120.00"));

        // Extension (green in the diagram): BankSlipsPayment is a NEW implementation.
        // No existing class was modified - PaymentProcessor and PaymentMethod stay untouched.
        new PaymentProcessor(new BankSlipsPayment()).process(new BigDecimal("89.90"));
    }
}

// The abstraction PaymentProcessor depends on («interface» PaymentMethod in the diagram).
interface PaymentMethod {
    void pay(BigDecimal amount);
}

// Depends only on the abstraction: closed for modification, open for extension.
class PaymentProcessor {
    private final PaymentMethod paymentMethod;

    PaymentProcessor(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    void process(BigDecimal amount) {
        paymentMethod.pay(amount);
    }
}

// Existing implementation: credit card.
class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paying " + amount + " with credit card (simulation)");
    }
}

// Existing implementation: Pix.
class PixPayment implements PaymentMethod {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paying " + amount + " with Pix (simulation)");
    }
}

// Existing implementation: PayPal.
class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paying " + amount + " with PayPal (simulation)");
    }
}

// NEW implementation (green in the diagram): added later, without touching existing code.
class BankSlipsPayment implements PaymentMethod {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paying " + amount + " with bank slips (simulation)");
    }
}
