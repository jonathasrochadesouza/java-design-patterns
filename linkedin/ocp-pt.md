---
platform: linkedin
language: pt-BR
principle: OCP
title: "Open/Closed Principle"
publishOn: 2026-10-06T10:00:00-03:00
image: assets/diagrams/solid-ocp.png
status: manual (colar o texto abaixo e anexar a imagem)
---

Adicionar uma feature nova não deveria ser sinônimo de abrir código que já funciona.

O Open/Closed Principle, o "O" do SOLID, diz que classes devem estar abertas para extensão, mas fechadas para modificação.

A ideia central é simples: prevenir que o código existente quebre quando você implementa algo novo. Você estende o comportamento base criando subclasses (ou novas implementações) e sobrescreve apenas as partes que precisam se comportar de forma diferente. Assim, nada do que o cliente já usa deixa de funcionar.

O exemplo que usei no meu estudo: um PaymentProcessor responsável por iniciar o processamento do pagamento — mas que não conhece detalhes de cartão de crédito, Pix, PayPal ou boleto. Ele depende apenas do contrato PaymentMethod.

Cada forma de pagamento implementa essa interface e fornece a própria lógica no método pay().

Precisa suportar boleto, cripto ou carteira digital? Basta criar uma nova classe que implemente a interface. Zero alterações no PaymentProcessor.

Duas ressalvas importantes:

1) Esse princípio não deve ser levado à risca em todos os cenários. Achou um bug na classe? Corrija o bug. Não crie uma subclasse para contornar o problema.

2) Como diz Alexander Shvets no livro Refactoring Guru: "Uma classe filha não deveria ser responsável pelos problemas da classe mãe".

E aqui entra a conexão com o SRP: se você tem uma classe bem redonda, com escopo fechado e concreto, não faz sentido adicionar complexidade fora do domínio dela.

Extensão é para comportamento novo. Correção é para bug. Não misture os dois.

#SOLID #Java #CleanCode #ArquiteturaDeSoftware #EngenhariaDeSoftware
