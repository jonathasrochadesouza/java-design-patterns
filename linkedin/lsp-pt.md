---
platform: linkedin
language: pt-BR
principle: LSP
title: "Liskov Substitution Principle"
publishOn: 2026-10-13T10:00:00-03:00
image: assets/diagrams/solid-lsp.png
status: manual (colar o texto abaixo e anexar a imagem)
---

Se sua classe filha lança UnsupportedOperationException, o problema provavelmente não está nela.

O Liskov Substitution Principle, o "L" do SOLID, determina que qualquer subclasse deve poder substituir sua classe base sem quebrar o comportamento esperado.

A subclasse precisa respeitar o contrato comportamental definido pela abstração. O tipo de retorno de um método na filha deve coincidir ou ser um subtipo do retorno na classe mãe. E, principalmente: a filha não pode ser forçada a executar um comportamento herdado que não faz sentido para ela.

Em termos práticos: se uma classe filha precisa lançar UnsupportedOperationException, ignora métodos herdados ou altera resultados esperados, a hierarquia está mal modelada.

O exemplo clássico que abordei no meu estudo: contas bancárias.

Uma conta comum permite saque. Uma conta de investimento pode não permitir retirada imediata. Fazer InvestmentAccount herdar de uma conta cujo contrato promete saque viola a LSP — e a "solução" usual é aquele método withdraw() que lança exceção. O famoso code smell.

A saída é separar contratos por capacidade:

- Toda conta implementa Account (consultar saldo).
- Apenas contas sacáveis implementam WithdrawableAccount (operação de saque).

O melhor disso: a conta de investimento nem consegue ser enviada acidentalmente para o serviço de saque, porque ela simplesmente não implementa WithdrawableAccount. O compilador trava por você.

Se você resumir o LSP em uma frase, fica: "Uma classe derivada deve poder ser substituída pela sua classe base."

Se não pode, o problema está na hierarquia — não em quem chama.

#SOLID #Java #CleanCode #ArquiteturaDeSoftware #EngenhariaDeSoftware
