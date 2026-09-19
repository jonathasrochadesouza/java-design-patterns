# Agenda de Publicação — Série SOLID (Outubro/2026)

25 publicações: 10 LinkedIn (pt + en), 10 Medium (pt + en), 5 dev.to (pt-br).
Um princípio por semana, na ordem clássica: SRP → OCP → LSP → ISP → DIP.

Horários baseados em estudos de 2026 (Buffer com 4,8M posts, Sprout Social com 2B engajamentos, Emplifi com 399M posts): LinkedIn rende mais de terça a quinta pela manhã (público tech: 10h–16h); fins de semana são os piores dias para LinkedIn — por isso nenhum post de LinkedIn cai em sábado/domingo.

## Calendário

### Semana 1 — SRP

| Data | Horário (BRT) | Plataforma | Arquivo |
|---|---|---|---|
| Qui 01/10 | 10:00 | LinkedIn (pt) | `linkedin/srp-pt.md` |
| Sex 02/10 | 10:00 | LinkedIn (en) | `linkedin/srp-en.md` |
| Sáb 03/10 | 10:00 | Medium (pt) | `medium/srp-pt.md` |
| Dom 04/10 | 10:00 | Medium (en) | `medium/srp-en.md` |
| Dom 04/10 | 11:00 | dev.to (pt) | `dev-to/srp-pt.md` |

### Semana 2 — OCP

| Data | Horário (BRT) | Plataforma | Arquivo |
|---|---|---|---|
| Ter 06/10 | 10:00 | LinkedIn (pt) | `linkedin/ocp-pt.md` |
| Qua 07/10 | 10:00 | Medium (pt) | `medium/ocp-pt.md` |
| Qui 08/10 | 10:00 | LinkedIn (en) | `linkedin/ocp-en.md` |
| Sáb 10/10 | 10:00 | Medium (en) | `medium/ocp-en.md` |
| Dom 11/10 | 11:00 | dev.to (pt) | `dev-to/ocp-pt.md` |

### Semana 3 — LSP

| Data | Horário (BRT) | Plataforma | Arquivo |
|---|---|---|---|
| Ter 13/10 | 10:00 | LinkedIn (pt) | `linkedin/lsp-pt.md` |
| Qua 14/10 | 10:00 | Medium (pt) | `medium/lsp-pt.md` |
| Qui 15/10 | 10:00 | LinkedIn (en) | `linkedin/lsp-en.md` |
| Sáb 17/10 | 10:00 | Medium (en) | `medium/lsp-en.md` |
| Dom 18/10 | 11:00 | dev.to (pt) | `dev-to/lsp-pt.md` |

### Semana 4 — ISP

| Data | Horário (BRT) | Plataforma | Arquivo |
|---|---|---|---|
| Ter 20/10 | 10:00 | LinkedIn (pt) | `linkedin/isp-pt.md` |
| Qua 21/10 | 10:00 | Medium (pt) | `medium/isp-pt.md` |
| Qui 22/10 | 10:00 | LinkedIn (en) | `linkedin/isp-en.md` |
| Sáb 24/10 | 10:00 | Medium (en) | `medium/isp-en.md` |
| Dom 25/10 | 11:00 | dev.to (pt) | `dev-to/isp-pt.md` |

### Semana 5 — DIP (fecha em 31/10)

| Data | Horário (BRT) | Plataforma | Arquivo |
|---|---|---|---|
| Ter 27/10 | 10:00 | LinkedIn (pt) | `linkedin/dip-pt.md` |
| Qua 28/10 | 10:00 | LinkedIn (en) | `linkedin/dip-en.md` |
| Qui 29/10 | 10:00 | Medium (pt) | `medium/dip-pt.md` |
| Sex 30/10 | 10:00 | Medium (en) | `medium/dip-en.md` |
| Sáb 31/10 | 11:00 | dev.to (pt) | `dev-to/dip-pt.md` |

## Publicação automática (Medium + dev.to)

O workflow `.github/workflows/publish-posts.yml` roda diariamente (13:00 e 14:00 UTC = 10:00 e 11:00 BRT) e publica, via API, todo post cujo campo `publishOn` no front matter já venceu e cujo `status` ainda é `pending`. Após publicar, ele marca `status: published` com a URL gerada, registra tudo em `.publish-state.json` e faz commit de volta na `main`.

### Secrets necessários (Settings → Secrets and variables → Actions)

| Secret | Como gerar |
|---|---|
| `MEDIUM_TOKEN` | Medium → Settings → Security → Integration tokens → "New integration token" |
| `DEVTO_API_KEY` | dev.to → Settings → Extensions → DEV Community API Keys → "Generate API Key" |

O dev.to pt recebe `canonical_url` apontando para o post correspondente do Medium (pt) automaticamente, lido de `.publish-state.json` — por isso o Medium (pt) é sempre agendado antes do dev.to na mesma semana.

Testar sem publicar nada: Actions → "Publish scheduled posts" → Run workflow → marcar "dry run". O log mostra exatamente o que seria publicado.

## LinkedIn (via MCP server)

O MCP server `linkedin-mcp-server` está configurado no opencode. Para publicar:

1. Ler o arquivo `linkedin/{principle}-{lang}.md`
2. Parsear o front matter (title, publishOn, image)
3. Chamar a tool MCP `linkedin_post_create` com o texto + imagem
4. Para agendamento: `linkedin_schedule_create` com o datetime do `publishOn`

Ou manualmente: copiar o texto do arquivo, baixar a imagem, colar no LinkedIn e agendar nativamente.

### Imagens (PNG dos diagramas Excalidraw, hospedadas no GitHub)

- SRP: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-srp.png
- OCP: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-ocp.png
- LSP: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-lsp.png
- ISP: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-isp.png
- DIP: https://raw.githubusercontent.com/jonathasrochadesouza/java-design-patterns/main/assets/diagrams/solid-dip.png

## Medium (via MCP server)

O MCP server `mcp-medium` está configurado no opencode. Para publicar:

1. Ler o arquivo `medium/{principle}-{lang}.md`
2. Parsear o front matter (title, tags, canonicalUrl) + body (markdown)
3. Chamar a tool MCP `publish_post` com title, content, tags, publishStatus

Ou manualmente: copiar o markdown, colar no editor do Medium e publicar.

## Estrutura de cada pasta

- `linkedin/` — 10 arquivos (`.md`), texto pronto para colar + front matter com data, idioma e imagem. Formato "texto seco": parágrafos curtos, sem markdown renderizado, hashtags no final.
- `medium/` — 10 arquivos em markdown completo (título, subtítulo, código Java, diagrama embutido por URL, tags no front matter).
- `dev-to/` — 5 arquivos pt-br com front matter no formato Jekyll do dev.to (`title`, `published`, `tags`, `canonical_url`, `cover_image`).
