---
platform: medium
language: pt-BR
principle: SRP
title: "SRP na prática: por que a classe que faz tudo é a que ninguém quer mexer"
subtitle: "Entendendo o Single Responsibility Principle com Java, um canivete suíço e uma Invoice que fazia tudo"
publishOn: 2026-10-03T10:00:00-03:00
tags: [solid, java, clean-code, arquitetura de software, engenharia de software]
canonicalUrl: ""
status: pending
---

A classe que faz tudo também é a classe que ninguém quer mexer.

Esse é o primeiro post de uma série onde compartilho meus estudos sobre os cinco princípios do SOLID — não a definição de livro, mas o meu entendimento depois de estudar, desenhar UMLs e quebrar exemplos. Começando pelo S: **Single Responsibility Principle**.

## O princípio

A definição clássica: **uma classe deve ter um único objetivo e uma única responsabilidade clara.**

Parece óbvio. E é justamente por ser tão óbvio que a gente ignora todo dia.

Se você definiu que uma classe faz o CRUD da sua entidade, ela não deveria carregar utilitários soltos, notificação por e-mail e geração de PDF no meio do caminho. Cada uma dessas coisas é uma responsabilidade — e responsabilidades diferentes vivem em classes diferentes.

## A analogia do canivete suíço

Eu gosto de pensar no canivete suíço.

Na vida real, ele é ótimo: faz tudo ao mesmo tempo — abre lata, corta, aperta parafuso. Só que no código não funciona assim. No código, a gente deveria comprar cada função do canivete separadamente, cada uma com sua única responsabilidade:

- Tem abridor de lata? Uma classe só para o abridor de lata.
- Tem faca de corte? Um componente só para a faca.

Não é sobre eliminar funções. É sobre cada função ter seu próprio lugar no mundo.

## O exemplo: uma Invoice que fazia tudo

O exemplo clássico é uma classe `Invoice` responsável por... praticamente o ciclo da fatura inteira:

```java
class Invoice {
    void createInvoice() { /* ... */ }
    void saveInvoice() { /* JDBC aqui dentro */ }
    void generatePdf() { /* geração de PDF aqui dentro */ }
    void sendInvoiceByEmail() { /* SMTP aqui dentro */ }
}
```

Funciona? Funciona. Só que quatro responsabilidades moram na mesma classe: criar, persistir, formatar e notificar.

Aplicando o SRP, ela vira quatro unidades, cada uma com sua razão de existir:

```java
class InvoiceService {
    Invoice create() { /* ... */ }
}

interface InvoiceRepository {
    void save(Invoice invoice);
}

class PdfGenerator {
    byte[] generate(Invoice invoice) { /* ... */ }
}

class InvoiceSender {
    void sendByEmail(Invoice invoice, String recipient) { /* ... */ }
}
```

O diagrama abaixo mostra o antes e depois que desenhei durante o estudo — o UML incorreto com a classe que faz tudo, e o correto com as responsabilidades separadas:

![Diagrama UML do Single Responsibility Principle: antes e depois](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-srp.png)

## "Mas aí o sistema fica maior!"

Sim, e não é um problema.

Quando quebramos as responsabilidades, as coisas ficam muito mais fáceis de entender, ler, manter e refatorar. O sistema pode "crescer" — mais classes, mais objetos, mais métodos — e isso é um custo que vale a pena pagar.

Você passa a saber exatamente o que cada classe faz, e o que cada método faz. Quando tudo está numa classe só, *parece* mais sucinto e fácil de compreender. Mas não está.

## O sinal de violação que mais vejo

Essa é a parte que mais mudou minha forma de olhar código: o SRP não fala só de métodos — fala de **atores**.

Se o RH pede uma correção numa classe, e o time de Vendas pede outra alteração nessa *mesma* classe, ela provavelmente está fazendo mais do que deveria. Públicos diferentes pedindo mudanças no mesmo lugar é o sintoma. A cura é quebrar a classe em unidades alinhadas a cada ator.

No segundo exemplo do meu diagrama, uma classe `Order` que criava pedidos, salvava no banco, atualizava estoque e enviava confirmação por e-mail acaba sendo "puxada" para quatro direções: DBA, dev de estoque, dev de e-mail e quem cuida do fluxo do pedido. Quatro atores, uma classe, quatro motivos para conflito no merge.

## Resumindo

- Uma classe, uma responsabilidade, um ator.
- Quebrar responsabilidades faz o sistema crescer — e isso é saudável.
- Se duas áreas diferentes do negócio pedem mudanças na mesma classe, é hora de quebrar.
- O canivete suíço é ótimo no bolso, péssimo no código.

## Referências que usei no estudo

- [Deep Dive Single Responsibility Principle — Omar Ismail](https://www.linkedin.com/pulse/deep-dive-single-responsibility-principle-omar-ismail)
- [Writing Maintainable Java Code: SRP — Nonstop.io](https://blog.nonstopio.com/writing-maintainable-java-code-single-responsibility-principle-solid-part-1-6ee0dc9d9f79)
- [Understanding SOLID Principles: SRP — Evertop](https://www.evertop.pl/en/understanding-solid-principles-single-responsibility/)

No próximo post da série: o **Open/Closed Principle** — por que adicionar uma feature nova não deveria ser sinônimo de abrir código que já funciona.

Os diagramas e anotações completas estão no [meu repositório de estudos no GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
