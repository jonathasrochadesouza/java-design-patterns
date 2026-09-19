# AGENTS.md

## Technology Stack

- Java 21 backend development
- Spring Boot 3.3.5 REST API development
- AWS Lambda serverless backend
- AWS API Gateway HTTP API
- AWS DynamoDB NoSQL persistence
- AWS SDK for Java v2
- Terraform infrastructure as code
- Angular 21 frontend development
- Angular Material and Angular CDK
- TypeScript
- RxJS
- Docker and Docker Compose
- OpenAPI 3.1 API documentation
- Maven build tooling
- JUnit 5 and Mockito unit testing
- CloudFront and S3 frontend hosting
- AWS IAM, CloudWatch, Route 53, and ACM infrastructure concepts

## Context
-> agents/

- Skills in 'agents/skills/' folder
- Agents and subagents in 'agents/agents/' folder

### Skills
-> agents/skills/

- committing-changes: When asked to create a Git commit or commit staged changes, read and follow `agents/skills/committing-changes/SKILL.md` before taking action.
- java-best-practices: When implementing, refactoring, debugging, testing, or reviewing Java, Spring Boot, Maven, JUnit, or Mockito code, read and follow `agents/skills/java-best-practices/SKILL.md` before taking action.

### Agents
-> agents/agents/

### Rules
-> agents/instructions/

## Infrastructure Safety

## Social Media Publishing (SOLID Series)

### MCP Servers (opencode)

Dois MCP servers Playwright estão configurados em `~/.config/opencode/opencode.jsonc`:

- **linkedin** — `@playwright/mcp` com perfil persistente em `~/.playwright-profiles/linkedin`
- **medium** — `@playwright/mcp` com perfil persistente em `~/.playwright-profiles/medium`

**Setup inicial (uma única vez):**
1. Na primeira vez que o agent tentar publicar, o navegador abre para login manual
2. Faça login no LinkedIn/Medium normalmente (2FA, captcha, etc.)
3. A sessão é salva no perfil persistente — não precisa logar de novo

### Fluxo de publicação

**LinkedIn** (via Playwright MCP — browser automation):
- Ler `linkedin/{principle}-{lang}.md`
- Parsear front matter (title, publishOn, image)
- `browser_navigate` → linkedin.com/feed/
- `browser_click` → "Start a post"
- `browser_type` → texto do post
- `browser_click` → "Post"
- Upload da imagem via `browser_click` no botão de mídia

**Medium** (via Playwright MCP — browser automation):
- Ler `medium/{principle}-{lang}.md`
- Parsear front matter (title, tags, canonicalUrl) + body (markdown)
- `browser_navigate` → medium.com/new-story
- `browser_type` → título
- `browser_paste` → conteúdo markdown
- Adicionar tags e publicar

**dev.to** (via GitHub Actions):
- Workflow `.github/workflows/publish-posts.yml` roda diariamente (14:00 UTC)
- Script `scripts/publish-posts.mjs` publica via Forem API
- Secrets: `DEVTO_API_KEY` em GitHub Actions

### Calendário

Ver `schedule.md` — Outubro/2026, 25 posts, 1 princípio por semana.
