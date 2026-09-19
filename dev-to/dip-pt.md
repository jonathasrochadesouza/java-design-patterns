---
title: "DIP: sua regra de negócio não deveria saber nem o nome do seu banco"
published: false
description: "Entendendo o Dependency Inversion Principle com OrderService, Spring Boot e injeção pelo construtor"
tags: [solid, java, springboot, cleancode, architecture]
canonical_url: ""
cover_image: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-dip.png
platform: devto
language: pt-BR
principle: DIP
publishOn: 2026-10-31T11:00:00-03:00
status: pending
---

Se a sua regra de negócio conhece o nome do seu banco de dados, alguma coisa está invertida — e não da forma certa.

Último post da minha série de estudos sobre o SOLID — fechando com o meu entendimento do **Dependency Inversion Principle**, o "D" da sigla. E, se você usa Spring, spoiler: você provavelmente já aplica ele todos os dias sem perceber.

## O princípio

O DIP orienta que **regras de negócio de alto nível não devem depender diretamente de detalhes técnicos de baixo nível** — banco de dados, SDKs, APIs externas, provedores de e-mail. Ambos devem depender de uma abstração, normalmente uma interface em Java.

As duas regras clássicas, que valem memorizar:

1. **Módulos de alto nível não devem depender de módulos de baixo nível; ambos devem depender de abstrações.**
2. **Abstrações não devem depender de detalhes; detalhes devem depender de abstrações.**

## O problema: o new escondido no serviço

O exemplo do meu estudo é o mais comum possível. Um `OrderService` que cria a própria dependência:

```java
class OrderService {
    private final MySqlOrderRepository repository =
        new MySqlOrderRepository(); // acoplamento direto ao detalhe

    void place(Order order) {
        // regras de negócio do pedido...
        repository.save(order);
    }
}
```

A regra de negócio (o que fazer com um pedido) está grudada num detalhe técnico (como e onde persistir). Trocar MySQL por DynamoDB? Vira cirurgia na regra de negócio. Testar sem banco? Impossível — o `new` está lá dentro.

## A inversão

A correção é **inverter a direção da dependência**:

```java
interface OrderRepository {
    void save(Order order);
}

class OrderService {
    private final OrderRepository repository;

    OrderService(OrderRepository repository) { // abstração entra por fora
        this.repository = repository;
    }

    void place(Order order) {
        // regras de negócio do pedido...
        repository.save(order);
    }
}
```

Em vez de `OrderService` descer até o `MySqlOrderRepository`, é o `MySqlOrderRepository` que sobe até o contrato `OrderRepository`. A implementação concreta é fornecida externamente — geralmente pelo Spring Boot via **injeção pelo construtor**:

```java
@Service
class OrderService {
    private final OrderRepository repository;

    OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}

@Repository
class MySqlOrderRepository implements OrderRepository {
    public void save(Order order) { /* JDBC */ }
}
```

É por isso que se chama *inversão*: a seta da dependência virou. Não é "injetar dependência" por moda — é a regra de negócio parar de apontar para o detalhe.

O UML que desenhei durante o estudo:

![Diagrama UML do Dependency Inversion Principle com OrderService e OrderRepository](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-dip.png)

## O ganho concreto

Não é estética de diagrama. O DIP paga a conta em três moedas:

- **OrderService não muda se o banco mudar.** MySQL → DynamoDB é uma nova classe implementando `OrderRepository`, zero mudança na regra de negócio.
- **Teste vira trivial.** Um fake em memória satisfaz o contrato — sem banco rodando, sem container, sem dor.
- **O domínio vira o centro.** Infraestrutura pluga no domínio, não o contrário.

## A pergunta de auditoria

Pra fechar a série inteira, uma pergunta que serve de termômetro: **a sua camada de domínio importa alguma classe concreta de infraestrutura?**

Se importa, é hora de inverter. E ao longo da série vimos que os cinco princípios se apoiam: SRP dá classes coesas, OCP as deixa extensíveis sem modificação, LSP garante contratos cumpríveis, ISP enxuga os contratos — e o DIP vira a última seta, apontando tudo para o domínio.

## Resumindo

- Alto nível e baixo nível dependem de abstrações — nunca um do outro.
- Detalhes dependem de abstrações, não o contrário.
- Injeção pelo construtor (a do Spring) é DIP materializado.
- Domínio no centro, infraestrutura plugada por contrato.

## Referências que usei no estudo

- Livro *Refactoring Guru* (Design Patterns), de Alexander Shvets
- Documentação do Spring Boot sobre injeção de dependências

Com isso fechou a série SOLID — os cinco princípios, os diagramas e as anotações completas estão no [meu repositório de estudos no GitHub](https://github.com/jonathasrochadesouza/java-design-patterns). Se a série te ajudou, compartilha com aquele colega que ainda mantém a classe que faz tudo.
