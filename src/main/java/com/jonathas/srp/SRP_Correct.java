package com.jonathas.srp;

import java.math.BigDecimal;
import java.util.List;

/**
 * Correct implementation of the Single Responsibility Principle (SRP) ✅
 *
 * Each class has a single responsibility, making the code easier to maintain and extend.
 */
public class SRP_Correct {
    public static void main(String[] args) {
        InvoiceRepository repository = new InvoiceRepository();
        GeneratePdf pdfGenerator = new GeneratePdf();
        SendInvoice sender = new SendInvoice();
        InvoiceService service = new InvoiceService(repository, pdfGenerator, sender);

        Invoice invoice = service.create(
            "Jonathas Rocha de Souza",
            List.of(
                new InvoiceItem("Headphone Logitech PRO - Version League of Legends", 1, new BigDecimal("750.00")),
                new InvoiceItem("Keyboard Razer Chroma", 2, new BigDecimal("550.00"))
            )
        );

        repository.save(invoice);
        byte[] pdf = pdfGenerator.generate(invoice);
        sender.sendByEmail(invoice, pdf, "jonathas@client.com");
    }
}

// Invoice record - represents the data and rules of the invoice, not its persistence or sending.
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

// Responsibility: create a valid invoice.
class InvoiceService {
    InvoiceService(InvoiceRepository repository, GeneratePdf pdfGenerator, SendInvoice sender) {
        // To keep the example close to the diagram, the other actions remain in the specialized classes.
        // See the orchestration variant below.
    }

    Invoice create(String customer, List<InvoiceItem> items) {
        return new Invoice(customer, items);
    }
}

// Responsibility: persist. This is only a simulation.
class InvoiceRepository {
    void save(Invoice invoice) {
        System.out.println("Invoice saved: " + invoice.customer() + " | Total: " + invoice.total());
    }
}

// Responsibility: generate document. Replace this with a real PDF library.
class GeneratePdf {
    byte[] generate(Invoice invoice) {
        String content = "Invoice for " + invoice.customer() + " | Total: " + invoice.total();
        System.out.println("Document generated (simulation): " + content);
        return content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
}

// Responsibility: send email. Replace this with a real email provider.
class SendInvoice {
    void sendByEmail(Invoice invoice, byte[] pdf, String recipient) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient is required");
        }
        System.out.println("Sending invoice for " + invoice.customer() + " to " + recipient
                + " (simulated attachment with " + pdf.length + " bytes)");
    }
}
