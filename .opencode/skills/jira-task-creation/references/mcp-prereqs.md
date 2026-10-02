# MCP Prerequisites and Fallback

The skill requires the **official Atlassian MCP server** (Atlassian AI Gateway / Rovo MCP), authenticated via **OAuth 2.1**. MCP tool names in this environment are prefixed `atlassian:` (the opencode server wraps the official tools: `getJiraIssue`, `createJiraIssue`, `searchJiraIssuesUsingJql`, `getAccessibleAtlassianResources`, `discover`, `executeRead/Write`, …).

## How to check availability

1. Attempt `atlassian:getAccessibleAtlassianResources` (requires no cloudId).
2. **Pass:** the tool exists AND the call succeeds → continue; cache the returned cloudId(s) for the session.
3. **Fail:** the tool is absent from the session (MCP not configured) OR the call fails with a connection/auth error → print the fallback message below verbatim and stop the skill run.

## Fallback message (print exactly, then stop)

> **Atlassian MCP não está disponível nesta sessão.** Não consigo criar a tarefa sem conectá-lo primeiro.
>
> **Documentação oficial:**
> - Getting started: https://support.atlassian.com/atlassian-ai-gateway/docs/get-started-with-the-atlassian-remote-mcp-server/
> - OAuth 2.1: https://support.atlassian.com/atlassian-ai-gateway/docs/configure-oauth-2-1/
> - Endpoint oficial (v2): `https://mcp.atlassian.com/v2/mcp`
> - Não use os endpoints legados `/v1/sse` ou `/v1/mcp/authv2`.
>
> **Copie o prompt abaixo em uma nova sessão para o próprio agente instalar o MCP para você** (ele edita a configuração e dispara o login OAuth no navegador):
>
> ```text
> Set up the official Atlassian MCP server for this agent (opencode).
> Follow the official setup guide:
>   https://support.atlassian.com/atlassian-ai-gateway/docs/get-started-with-the-atlassian-remote-mcp-server/
> Use the server URL:
>   https://mcp.atlassian.com/v2/mcp
> Steps:
> 1. Add a remote MCP server named "atlassian" to the opencode config
>    (~/.config/opencode/opencode.jsonc):
>    "mcp": { "atlassian": { "type": "remote", "url": "https://mcp.atlassian.com/v2/mcp", "enabled": true } }
> 2. Do NOT use legacy v1 endpoints (/v1/sse or /v1/mcp/authv2).
> 3. Start the Atlassian MCP OAuth 2.1 authentication flow so I can sign in
>    in the browser and grant consent to my Atlassian Cloud site.
> 4. After I authorize, verify the connection by listing the site(s) returned
>    by the Atlassian MCP and tell me the result.
> ```
>
> Após instalar e autorizar, **reinicie o opencode** e invoque `/jira-task-creation` novamente.

## OAuth 2.1 notes

- OAuth flow opens a browser consent screen; the MCP app installs automatically on first consent (the consenting user needs product access, e.g. Jira).
- Tokens are consent-scoped to a specific cloud site (cloudId); never attempt to reuse a cloudId across sites.
- API-token auth is a headless alternative (org-admin enabled); this skill always prefers the interactive OAuth flow.

## Post-install verification (for the agent that performs the install)

Run `atlassian:getAccessibleAtlassianResources` → a list with at least one cloudId proves the connection; report the site name(s) found.
