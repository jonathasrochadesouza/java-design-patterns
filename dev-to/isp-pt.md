---
title: "ISP: nenhuma classe deveria implementar métodos que não usa"
published: false
description: "Entendendo o Interface Segregation Principle com impressoras, interfaces infladas e métodos vazios"
tags: [solid, java, cleancode, architecture, programming]
canonical_url: ""
cover_image: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-isp.png
platform: devto
language: pt-BR
principle: ISP
publishOn: 2026-10-25T11:00:00-03:00
status: pending
---

Aquele método vazio na sua classe, que não faz nada só porque "a interface obriga"? É exatamente disso que o ISP fala.

Quarto post da minha série de estudos sobre o SOLID — agora com o meu entendimento do **Interface Segregation Principle**, o "I" da sigla. É o princípio mais curto de explicar e um dos mais violados no dia a dia.

## O princípio

A definição clássica: **classes não devem ser forçadas a depender de métodos que não usam.**

O ISP evita as chamadas **interfaces infladas** — aquelas com muitos métodos que nem todo cliente, classe final ou implementação precisa usar.

Quando interfaces infladas são implementadas, algumas classes são obrigadas a implementar coisas que não usam. E o resultado você já conhece de algum código que já mexeu: métodos vazios no meio da classe, fazendo nada, apenas cumprindo contrato. Ou pior: métodos que lançam exceção porque "não faz sentido aqui".

## O exemplo: a impressora

O exemplo que usei no meu estudo é o mais visual de todos: uma impressora.

Comece com uma interface `Machine` que promete tudo:

```java
interface Machine {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
}
```

Parece útil, certo? Uma interface, três operações, pronto. Só que a impressora **básica** do escritório só imprime. Ela é forçada a implementar `scan()` e `fax()` — features que ela não tem:

```java
class BasicPrinter implements Machine {
    public void print(Document doc) { /* imprime */ }
    public void scan(Document doc) { /* ??? não tem scanner */ }
    public void fax(Document doc) { /* ??? não tem fax */ }
}
```

E aí sobram duas opções, ambas ruins: métodos vazios (que enganam quem chama) ou exceções (que explodem em runtime). A classe passou a **depender de métodos que não usa** — a violação literal do princípio.

## A segregação

A solução é segregar a interface por capacidade:

```java
interface Printer {
    void print(Document doc);
}

interface Scanner {
    void scan(Document doc);
}

interface Fax {
    void fax(Document doc);
}
```

Agora cada classe depende apenas do que realmente usa:

```java
class BasicPrinter implements Printer {
    public void print(Document doc) { /* imprime */ }
}

class MultifunctionPrinter implements Printer, Scanner, Fax {
    public void print(Document doc) { /* ... */ }
    public void scan(Document doc) { /* ... */ }
    public void fax(Document doc) { /* ... */ }
}
```

- A impressora básica implementa só `Printer`.
- A multifuncional implementa as três.
- Nenhum método vazio, nenhuma exceção mentirosa, nenhum contrato descumprido.

O desenho conceitual que fiz durante o estudo — a multifuncional (printer, scanner, fax) versus a impressora básica (só printer):

![Diagrama do Interface Segregation Principle: impressora multifuncional versus impressora básica](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-isp.png)

## A conexão com o LSP

Repare que o ISP conversa direto com o princípio anterior. No LSP, a solução para a `InvestmentAccount` foi **separar contratos por capacidade** — toda conta consulta saldo, só contas sacáveis implementam saque. Isso *é* interface segregation aplicada.

Regra de bolso que fica dos dois princípios: **quando uma classe é forçada a implementar (ou herdar) algo que não pode cumprir, o contrato está grande demais.** Quebre o contrato, não a classe.

## Resumindo

- Nenhuma classe deve ser obrigada a depender ou implementar métodos que não utilizará.
- Interface inflada = clientes pagando por features que não usam.
- Métodos vazios "cumprindo contrato" são code smell, não solução.
- Várias interfaces pequenas e focadas > uma interface grande e "completa".

## Referências que usei no estudo

- Livro *Refactoring Guru* (Design Patterns), de Alexander Shvets
- Curso de Design Patterns em Java (Udemy), que usei como base de estudo

No próximo (e último) post da série: o **Dependency Inversion Principle** — por que a sua regra de negócio não deveria saber nem o nome do seu banco de dados.

Os diagramas e anotações completas estão no [meu repositório de estudos no GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
