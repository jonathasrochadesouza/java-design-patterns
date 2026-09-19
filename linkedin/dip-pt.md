---
platform: linkedin
language: pt-BR
principle: DIP
title: "Dependency Inversion Principle"
publishOn: 2026-10-27T10:00:00-03:00
image: assets/diagrams/solid-dip.png
status: manual (colar o texto abaixo e anexar a imagem)
---

Se a sua regra de negócio conhece o nome do seu banco de dados, alguma coisa está invertida — e não da forma certa.

O Dependency Inversion Principle, o "D" do SOLID (e o que fecha a série), orienta que regras de negócio de alto nível não devem depender diretamente de detalhes técnicos de baixo nível: banco de dados, SDKs, APIs externas, provedores de e-mail. Ambos devem depender de uma abstração — normalmente uma interface.

As duas regras clássicas:

- Módulos de alto nível não devem depender de módulos de baixo nível; ambos devem depender de abstrações.
- Abstrações não devem depender de detalhes; detalres devem depender de abstrações.

Na prática: em vez de OrderService fazer new MySqlOrderRepository(), ele depende de algo como OrderRepository. A implementação concreta é fornecida de fora — geralmente pelo Spring Boot via injeção pelo construtor.

Perceba a mudança de direção: não é a regra de negócio que desce até o detalhe técnico. É o detalhe técnico que sobe até a abstração. Por isso "inversão".

O ganho concreto:

- OrderService não muda se o banco mudar.
- Dá pra trocar MySql por DynamoDB sem tocar na regra de negócio.
- E testar com um mock/fake fica trivial — sem banco rodando.

Se você usa Spring, provavelmente já aplica DIP todos os dias sem perceber: toda aquela injeção pelo construtor que o framework faz por você é isso.

A pergunta pra fechar: a sua camada de domínio importa alguma classe concreta de infraestrutura? Se importa, é hora de inverter.

#SOLID #Java #SpringBoot #CleanCode #ArquiteturaDeSoftware
