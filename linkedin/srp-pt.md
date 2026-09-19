---
platform: linkedin
language: pt-BR
principle: SRP
title: "Single Responsibility Principle"
publishOn: 2026-10-01T10:00:00-03:00
image: assets/diagrams/solid-srp.png
status: manual (colar o texto abaixo e anexar a imagem)
---

A classe que faz tudo também é a classe que ninguém quer mexer.

O primeiro princípio do SOLID, o Single Responsibility Principle, diz o óbvio que a gente ignora todo dia: uma classe deve ter um único objetivo e uma única responsabilidade clara.

Se você definiu que uma classe faz o CRUD da sua entidade, ela não deveria carregar utilitários soltos, notificação por e-mail e geração de PDF no meio do caminho.

Eu gosto de pensar no canivete suíço. Na vida real, ele é ótimo: faz tudo ao mesmo tempo. No código, não funciona assim. No código, a gente deveria comprar cada função do canivete separadamente, cada uma com sua única responsabilidade. Tem abridor de lata? Uma classe só para o abridor de lata. Tem faca de corte? Um componente só para a faca.

Quando quebramos as responsabilidades, as coisas ficam muito mais fáceis de entender, ler, manter e refatorar. Sim, o sistema pode "crescer" — mais classes, mais objetos, mais métodos — e isso não é um problema.

Você passa a saber exatamente o que cada classe faz, e o que cada método faz. Quando tudo está numa classe só, parece mais sucinto e fácil de compreender. Mas não está.

Um sinal claro de violação: o RH pede uma correção numa classe e o time de Vendas pede outra alteração nessa mesma classe. Se públicos diferentes pedem mudanças no mesmo lugar, ela provavelmente está fazendo mais do que deveria — e é hora de quebrar.

No diagrama que anexei, uma classe Invoice que criava, salvava, gerava PDF e enviava por e-mail virou: InvoiceService, InvoiceRepository, PdfGenerator e InvoiceSender. Cada uma com a sua responsabilidade.

E aí: quantas responsabilidades tem a classe mais "cheia" do seu sistema?

#SOLID #Java #CleanCode #ArquiteturaDeSoftware #EngenhariaDeSoftware
