package com.jonathas.srp;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Incorrect implementation of the Single Responsibility Principle (SRP) ❌
 *
 * All responsibilities (invalidation + persistence, document generation and sending)
 * are crammed into a single class, making the code harder to maintain and extend.
 */
public class SRP_Incorrect {
    public static void main(String[] args) {
        String customer = "Jonathas Rocha de Souza";

        List<InvoiceItem> items = List.of(
            new InvoiceItem("Headphone Logitech PRO - Version League of Legends", 1, new BigDecimal("750.00")),
            new InvoiceItem("Keyboard Razer Chroma", 2, new BigDecimal("550.00"))
        );

        Invoice invoice = create(customer, items);
        save(invoice);
        byte[] pdf = generate(invoice);
        sendByEmail(invoice, pdf, "jonathas@client.com");
    }

    // Responsibility: create a valid invoice.
    static Invoice create(String customer, List<InvoiceItem> items) {
        return new Invoice(customer, items);
    }

    // Responsibility: persist. This is only a simulation.
    static void save(Invoice invoice) {
        System.out.println("Invoice saved: " + invoice.customer() + " | Total: " + invoice.total());
    }

    // Responsibility: generate document. Replace this with a real PDF library.
    static byte[] generate(Invoice invoice) {
        String content = "Invoice for " + invoice.customer() + " | Total: " + invoice.total();
        System.out.println("Document generated (simulation): " + content);
        return content.getBytes(StandardCharsets.UTF_8);
    }

    // Responsibility: send email. Replace this with a real email provider.
    static void sendByEmail(Invoice invoice, byte[] pdf, String recipient) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient is required");
        }
        System.out.println("Sending invoice for " + invoice.customer() + " to " + recipient
                + " (simulated attachment with " + pdf.length + " bytes)");
    }

    // Invoice represents the data and rules of the invoice, not its persistence or sending.
    record Invoice(String customer, List<InvoiceItem> items) {
        Invoice {
            if (customer == null || customer.isBlank()) {
                throw new IllegalArgumentException("Client is required");
            }
            items = List.copyOf(items);
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Invoice needs at least one item");
            }
        }

        BigDecimal total() {
            return items.stream()
                    .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    record InvoiceItem(String description, int quantity, BigDecimal unitPrice) {
        InvoiceItem {
            if (description == null || description.isBlank() || quantity <= 0
                    || unitPrice == null || unitPrice.signum() < 0) {
                throw new IllegalArgumentException("Invalid item");
            }
        }
    }
}
