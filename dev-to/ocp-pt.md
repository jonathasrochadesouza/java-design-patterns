---
title: "OCP: adicione features sem abrir código que já funciona"
published: false
description: "Entendendo o Open/Closed Principle com um PaymentProcessor, Pix, boleto e uma citação do Refactoring Guru"
tags: [solid, java, cleancode, architecture, programming]
canonical_url: ""
cover_image: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-ocp.png
platform: devto
language: pt-BR
principle: OCP
publishOn: 2026-10-11T11:00:00-03:00
status: pending
---

Adicionar uma feature nova não deveria ser sinônimo de abrir código que já funciona.

Segundo post da minha série de estudos sobre o SOLID — agora com o meu entendimento do **Open/Closed Principle**, o "O" da sigla.

## O princípio

A definição clássica: **classes devem estar abertas para extensão, mas fechadas para modificação.**

A ideia central é prevenir que o código existente quebre quando você implementa uma funcionalidade nova. A classe fica "aberta" no sentido de permitir estender o comportamento base: você cria subclasses ou novas implementações e sobrescreve apenas as partes que precisam se comportar de forma diferente. Assim, nada do que o cliente já usa deixa de funcionar.

## O exemplo: PaymentProcessor

O exemplo que usei no meu estudo é um caso de uso que todo sistema de pagamentos vive:

Imagine um serviço `PaymentProcessor`, responsável por iniciar o processamento do pagamento dentro do sistema. Ele não conhece — e não precisa conhecer — os detalhes específicos de cartão de crédito, Pix, PayPal ou boleto. Ele depende apenas do contrato `PaymentMethod`:

```java
interface PaymentMethod {
    void pay(BigDecimal amount);
}

class PaymentProcessor {
    private final PaymentMethod paymentMethod;

    PaymentProcessor(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    void process(BigDecimal amount) {
        paymentMethod.pay(amount);
    }
}
```

Cada forma de pagamento implementa a interface e fornece a própria lógica no método `pay()`:

```java
class CreditCardPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* lógica de cartão */ }
}

class PixPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* lógica de Pix */ }
}

class PayPalPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* lógica de PayPal */ }
}
```

Agora a parte que mostra o princípio funcionando: quando for necessário adicionar um novo meio de pagamento — boleto, criptomoeda, carteira digital — basta criar uma nova classe que implemente a interface:

```java
class BankSlipPayment implements PaymentMethod {
    public void pay(BigDecimal amount) { /* lógica de boleto */ }
}
```

**Sem alterar uma linha do `PaymentProcessor`.** O sistema ficou aberto para extensão e fechado para modificação.

O UML que desenhei durante o estudo, mostrando o `PaymentProcessor` dependendo apenas do contrato:

![Diagrama UML do Open/Closed Principle com PaymentProcessor e PaymentMethod](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-ocp.png)

## As duas ressalvas que ninguém conta

Esse princípio **não deve ser levado à risca em todos os cenários**. Ele não foi feito para ser aplicado a tudo.

**Primeira:** se você sabe que há um bug numa classe, apenas corrija o bug. Não crie uma subclasse para contornar o problema. Corrigir um defeito não é "estender comportamento" — é manutenção, e modificar a classe é exatamente o que você deve fazer.

**Segunda**, e é a citação que mais gostei do estudo. Alexander Shvets, no livro *Refactoring Guru* (aquele que todo mundo indica quando fala de design patterns), resume assim:

> "Uma classe filha não deveria ser responsável pelos problemas da classe mãe."

Se a classe base tem um problema, empurrar a solução para as filhas é apenas espalhar o problema por mais lugares.

## A conexão com o SRP

Pra mim, o OCP tem relação direta com o princípio anterior.

Se você tem uma classe bem redonda — com um escopo fechado, concreto e coeso, como manda o SRP — não faz sentido passar a adicionar complexidade fora do domínio dela. Uma classe com escopo bem definido raramente precisa ser *modificada* para ganhar comportamento novo: o comportamento novo cabe em outra implementação do mesmo contrato.

SRP e OCP se reforçam: coesão dentro da classe, extensão fora dela.

## Resumindo

- Aberto para extensão, fechado para modificação.
- Nova feature = nova implementação de um contrato existente, não `if` novo no meio da classe.
- Bug se corrige na classe; não se contorna com subclasse.
- "Uma classe filha não deveria ser responsável pelos problemas da classe mãe." — Alexander Shvets

## Referências que usei no estudo

- [SOLID Principles Series: Open/Closed Principle — Plain English (Medium)](https://javascript.plainenglish.io/solid-principles-series-part-2-open-closed-principle-ocp-44febc1251a8)
- Livro *Refactoring Guru* (Design Patterns), de Alexander Shvets

No próximo post: o **Liskov Substitution Principle** — por que aquele `UnsupportedOperationException` no meio da hierarquia é um grito de socorro.

Os diagramas e anotações completas estão no [meu repositório de estudos no GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
