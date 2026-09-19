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

Dois MCP servers estão configurados em `~/.config/opencode/opencode.jsonc`:

- **linkedin** — `linkedin-mcp-server` (npx). Publica e agenda posts no LinkedIn via API oficial.
- **medium** — `mcp-medium` (npx). Publica artigos no Medium via integration token.

Credenciais ficam em `.env` (nunca commitado). Copie `.env.example` para `.env` e preencha.

### Fluxo de publicação

**LinkedIn** (via MCP):
- Ler `linkedin/{principle}-{lang}.md`
- Parsear front matter (title, publishOn, image)
- Chamar tool `linkedin_post_create` com texto + imagem
- Para agendamento: usar `linkedin_schedule_create` com datetime

**Medium** (via MCP):
- Ler `medium/{principle}-{lang}.md`
- Parsear front matter (title, tags, canonicalUrl) + body (markdown)
- Chamar tool `publish_post` com title, content, tags, publishStatus

**dev.to** (via GitHub Actions):
- Workflow `.github/workflows/publish-posts.yml` roda diariamente (13:00/14:00 UTC)
- Script `scripts/publish-posts.mjs` publica via Forem API
- Secrets: `DEVTO_API_KEY` em GitHub Actions

### Calendário

Ver `schedule.md` — Outubro/2026, 25 posts, 1 princípio por semana.
