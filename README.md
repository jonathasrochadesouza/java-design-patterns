# Java Design Patterns
Java design patterns

## SOLID Principles — Visual Reference

Diagrams illustrating each SOLID principle with Java examples. Source files in [`engineering/`](engineering/) (Excalidraw).

<!-- Imagens dos diagramas — adicione abaixo -->
![SRP — One class, one job](assets/diagrams/solid-srp.png)
<!-- SRP: Invoice que faz tudo vs. classes separadas por responsabilidade -->

![OCP — Extend without modifying](assets/diagrams/solid-ocp.png)
<!-- OCP: PaymentProcessor com strategy pattern para Pix, boleto, cartão -->

![LSP — Subtypes must be substitutable](assets/diagrams/solid-lsp.png)
<!-- LSP: Contas bancárias — InvestmentAccount que não pode sacar -->

![ISP — No fat interfaces](assets/diagrams/solid-isp.png)
<!-- ISP: Impressora com interface inflada vs. interfaces segregadas -->

![DIP — Depend on abstractions](assets/diagrams/solid-dip.png)
<!-- DIP: OrderService depende de repository abstrato, não de implementação concreta -->

## Requisitos

- JDK 25 (LTS) — [`C:\Dev\Bins\Java\jdk-25.0.4.1`](C:\Dev\Bins\Java\jdk-25.0.4.1)

## Build and execution

```
mvn package
java -cp target\classes com.jonathas.HelloWorld
```

## Build by on specific java version by path!

```
$env:JAVA_HOME = "C:\Dev\Bins\Java\jdk-25.0.4.1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

java --version

mvn package
java -cp target\classes com.jonathas.HelloWorld
```

## SOLID Content

### Single-Responsibility Principle (SRP)

`com.jonathas.HelloWorld`

### Open/Closed Principle (OCP)

`com.jonathas.HelloWorld`

### Liskov Substitution Principle (LSP)

`com.jonathas.HelloWorld`

### Interface Segregation Principle (ISP)

`com.jonathas.HelloWorld`

### Dependency Inversion Principle (DIP)

`com.jonathas.HelloWorld`