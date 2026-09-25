package com.jonathas.srp;

import java.math.BigDecimal;
import java.util.List;

/** Execute com: javac ExemploSRP.java && java ExemploSRP */
public class SRP {
    public static void main(String[] args) {
        InvoiceRepository repository = new InvoiceRepository();
        GeneratePdf pdfGenerator = new GeneratePdf();
        SendInvoice sender = new SendInvoice();
        InvoiceService service = new InvoiceService(repository, pdfGenerator, sender);

        Invoice invoice = service.create(
                "Cliente Exemplo",
                List.of(new InvoiceItem("Consultoria", 2, new BigDecimal("150.00"))));

        repository.save(invoice);
        byte[] pdf = pdfGenerator.generate(invoice);
        sender.sendByEmail(invoice, pdf, "cliente@example.com");
    }
}

// Invoice representa os dados e regras da fatura, não sua persistência ou envio.
record Invoice(String customer, List<InvoiceItem> items) {
    Invoice {
        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        items = List.copyOf(items);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("A fatura precisa de pelo menos um item");
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
            throw new IllegalArgumentException("Item inválido");
        }
    }
}

// Responsabilidade: criar uma fatura válida.
class InvoiceService {
    InvoiceService(InvoiceRepository repository, GeneratePdf pdfGenerator, SendInvoice sender) {
        // Para manter o exemplo próximo do diagrama, as outras ações ficam
        // nas classes especializadas. Veja a variante orquestradora abaixo.
    }

    Invoice create(String customer, List<InvoiceItem> items) {
        return new Invoice(customer, items);
    }
}

// Responsabilidade: persistir. Aqui há apenas uma simulação.
class InvoiceRepository {
    void save(Invoice invoice) {
        System.out.println("Fatura salva: " + invoice.customer() + " | Total: " + invoice.total());
    }
}

// Responsabilidade: gerar documento. Substitua pela biblioteca de PDF real.
class GeneratePdf {
    byte[] generate(Invoice invoice) {
        String content = "Fatura de " + invoice.customer() + " | Total: " + invoice.total();
        System.out.println("Documento gerado (simulação): " + content);
        return content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
}

// Responsabilidade: enviar e-mail. Substitua por um provedor de e-mail real.
class SendInvoice {
    void sendByEmail(Invoice invoice, byte[] pdf, String recipient) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Destinatário é obrigatório");
        }
        System.out.println("Enviando fatura de " + invoice.customer() + " para " + recipient
                + " (anexo simulado com " + pdf.length + " bytes)");
    }
}
