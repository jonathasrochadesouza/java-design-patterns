---
platform: linkedin
language: pt-BR
principle: ISP
title: "Interface Segregation Principle"
publishOn: 2026-10-20T10:00:00-03:00
image: assets/diagrams/solid-isp.png
status: manual (colar o texto abaixo e anexar a imagem)
---

Aquele método vazio na sua classe, que não faz nada só porque "a interface obriga"? É exatamente disso que o ISP fala.

O Interface Segregation Principle, o "I" do SOLID, diz: classes não devem ser forçadas a depender de métodos que não usam.

O princípio evita as chamadas interfaces infladas — aquelas com muitos métodos que nem todo cliente, classe final ou implementação precisa usar.

Quando interfaces infladas são implementadas, algumas classes são obrigadas a implementar o que não usam. E o resultado você já conhece: métodos vazios no meio da classe, fazendo nada, apenas cumprindo contrato.

O exemplo que mais gosto é a impressora:

Uma interface Machine com print(), scan() e fax() parece útil, certo? Só que a impressora básica do escritório só imprime. Ela é forçada a implementar scan() e fax() — que ela não tem — e acaba com dois métodos vazios (ou pior: lançando exceções).

A segregação resolve:

- Printer com print()
- Scanner com scan()
- Fax com fax()

A impressora básica implementa só Printer. A multifuncional implementa as três. Cada classe depende apenas do que realmente usa.

E a frase pra levar pro dia a dia: nenhuma classe deve ser obrigada a depender ou implementar métodos que não utilizará.

Interface grande é aquela que você escreve achando que está economizando código. Interface segregada é aquela que você agradece daqui a seis meses.

#SOLID #Java #CleanCode #ArquiteturaDeSoftware #EngenhariaDeSoftware
