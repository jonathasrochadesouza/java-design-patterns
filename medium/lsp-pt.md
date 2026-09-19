---
platform: medium
language: pt-BR
principle: LSP
title: "LSP: quando a herança vira um problema (e como contratos por capacidade resolvem)"
subtitle: "Entendendo o Liskov Substitution Principle com contas bancárias, saques e uma InvestmentAccount que não podia sacar"
publishOn: 2026-10-14T10:00:00-03:00
tags: [solid, java, clean-code, arquitetura de software, engenharia de software]
canonicalUrl: ""
status: pending
---

Se sua classe filha lança `UnsupportedOperationException`, o problema provavelmente não está nela.

Terceiro post da minha série de estudos sobre o SOLID — agora com o meu entendimento do **Liskov Substitution Principle**, o "L" da sigla. Spoiler: é o princípio que mais revela hierarquias mal desenhadas.

## O princípio

A definição clássica: **qualquer subclasse deve poder substituir sua classe base sem quebrar o comportamento esperado.**

Isso significa que a subclasse precisa respeitar o **contrato comportamental** definido pela abstração. Não basta assinar a assinatura dos métodos — a filha precisa cumprir a promessa que a mãe fez para quem a chama.

Tem também uma regra mais técnica: o tipo de retorno de um método na subclasse deve coincidir ou ser um subtipo do tipo de retorno na superclasse. Se o método base devolve `Account`, você herda a interface `Account` até chegar ao seu serviço — você não cria um `AccountPCD` solto no ar, sem que ele herde de `Account` de alguma forma.

E tem a parte que, pra mim, é a essência: o LSP também trata os cenários em que um **comportamento herdado que não deveria acontecer** no novo serviço é executado só porque a herança obriga.

## O code smell que entrega tudo

Em termos práticos: se uma classe filha precisa lançar `UnsupportedOperationException`, ignora métodos herdados ou altera resultados esperados, **provavelmente a hierarquia está mal modelada**.

Aquele método que só existe para lançar exceção é o grito de socorro da hierarquia. Ele está dizendo: "me forçaram a assinar um contrato que não consigo cumprir".

## O exemplo: contas bancárias

O exemplo que abordei no meu estudo é bastante intuitivo.

Uma conta comum permite saque. Uma conta de investimento pode não permitir retirada imediata. Se você fizer `InvestmentAccount` herdar de uma conta cujo contrato promete saque, está violando a LSP:

```java
class Account {
    BigDecimal getBalance() { /* ... */ }
    void withdraw(BigDecimal amount) { /* ... */ }
}

class InvestmentAccount extends Account {
    @Override
    void withdraw(BigDecimal amount) {
        throw new UnsupportedOperationException(
            "Conta de investimento não permite retirada imediata");
    }
}
```

Compila? Compila. Funciona? Até o primeiro `withdraw()` numa conta de investimento em produção.

## A solução: contratos por capacidade

A saída é separar contratos por **capabilidade**, não por hierarquia de "tipos de conta":

```java
interface Account {
    BigDecimal getBalance();
}

interface WithdrawableAccount extends Account {
    void withdraw(BigDecimal amount);
}

class CheckingAccount implements WithdrawableAccount {
    private BigDecimal balance;

    public BigDecimal getBalance() { return balance; }
    public void withdraw(BigDecimal amount) { /* regras de saque */ }
}

class InvestmentAccount implements Account {
    private BigDecimal balance;

    public BigDecimal getBalance() { return balance; }
}
```

- **Toda conta** consegue consultar saldo: `Account`.
- **Apenas contas sacáveis** implementam a operação de saque: `WithdrawableAccount`.

E aqui está o detalhe que mais gostei desse design: a conta de investimento **nem consegue ser enviada acidentalmente para o serviço de saque**, porque ela simplesmente não implementa `WithdrawableAccount`. O erro é pego em tempo de compilação, não em produção. O compilador trabalha por você.

O UML que desenhei durante o estudo, com os contratos separados:

![Diagrama UML do Liskov Substitution Principle com Account e WithdrawableAccount](https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-lsp.png)

## A frase pra lembrar

No final, o que vale mesmo memorizar é:

> "Uma classe derivada deve poder ser substituída pela sua classe base."

Se não pode — se substituir quebra, lança exceção ou surpreende quem chama — o problema não está em quem chama. Está na hierarquia.

## Resumindo

- Subclasse substitui classe base sem quebrar nada. Sem exceção (literalmente).
- `UnsupportedOperationException` em método herdado = hierarquia mal modelada.
- Prefira contratos pequenos por capacidade a hierarquias grandes por tipo.
- Quando o compilador pode garantir a regra, você não precisa testar em produção.

## Referências que usei no estudo

- [SOLID Principles in PHP — Accesto](https://accesto.com/blog/solid-php-solid-principles-in-php/)
- [Liskov Substitution Principle in Java — Java Brahman](https://www.javabrahman.com/programming-principles/liskov-substitution-principal-java-example/)
- Livro *Refactoring Guru* (Design Patterns), de Alexander Shvets

No próximo post: o **Interface Segregation Principle** — aquele método vazio na sua classe que não faz nada só porque "a interface obriga".

Os diagramas e anotações completas estão no [meu repositório de estudos no GitHub](https://github.com/jonathasrochadesouza/java-design-patterns).
