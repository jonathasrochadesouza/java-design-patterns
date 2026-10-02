---
name: jira-task-creation
description: Creates standardized Jira work items (Task, Bug, Story, Epic) through the official Atlassian MCP, enforcing an Epic-to-Story hierarchy, verb-first titles, a fixed structured description template, and auto-detected category and priority. Use when the user asks to create a Jira task, ticket, bug, story, or epic ("criar tarefa no Jira", "create a Jira ticket"). Manual-invocation only (slash command); never picks automatically.
metadata:
  opencode:
    slash: true
    autoinvoke: false
---

# Jira Task Creation

## Invocation

User-invoked only. Load this skill ONLY when the user explicitly invokes it by name — `/jira-task-creation`, "use jira-task-creation", "load jira-task-creation", or when the `/jira-task-creation` command file triggers it. Never load automatically or on its own initiative for any other task.

Create Jira work items that are never orphans: every ticket carries a verb-first title, a fixed structured description, an Epic and a Story in its hierarchy, a category value in the field the project actually uses, and an honest business-impact priority. Reconnoiter the target project before writing; nothing is created without the user's approval.

## Scope

Use this skill to:

- Briefly reconnoiter the target Jira project and report the conventions found
- Draft and create Task, Bug, Story, or Epic issues following the mandatory standards
- Create the missing Epic/Story chain (after user approval) so the ticket has clear parents
- Link related issues and report the final keys

Out of scope: editing or transitioning existing issues beyond linking them; sprint/board management; Confluence content; bulk migration of many tickets.

## Hard rules

1. **MCP first.** If no `atlassian:*` tool is available, or the first call fails with a connection/authentication error, do not proceed: print the fallback message from `references/mcp-prereqs.md` exactly as written and stop.
2. **No orphans.** A Task/Bug MUST have a parent Story, and a Story MUST have a parent Epic. Every created issue MUST have priority set and a non-empty value in the category field detected during recon (auto-detected in step 2; never guessed).
3. **Fixed description template.** Every description follows the template in `references/ticket-standards.md`. Free-form paragraph dumps are not acceptable.
4. **Approval gate.** Present the full draft (fields + hierarchy + description) and wait for the user's explicit confirmation before any create call.
5. **One ticket, one outcome.** A request covering several outcomes becomes several tickets under the same Epic/Story, each approved.
6. **Reuse the cloudId.** Resolve it once with `atlassian:getAccessibleAtlassianResources`, cache it for the session, and pass it explicitly on every subsequent call.

## Workflow

1. **MCP check** — run `atlassian:getAccessibleAtlassianResources` (needs no cloudId). Done when it succeeds (cache the returned cloudId) or the fallback message has been printed and the run stopped.
2. **Project recon** — read `references/project-recon.md` and execute it against the target project. Done when the recon summary (types, hierarchy mechanism, category carrier field, priority scale, language, description style) is shown to the user, using ~6 read tool calls maximum.
3. **Draft** — apply `references/ticket-standards.md`: compose the title, choose the type, write the templated description, propose the Epic and Story (existing keys from the recon, or drafts of the missing parents), the category value drawn from the detected field's existing values, priority with a one-line business-impact justification, and any relations. If the Epic or Story does not exist, show the complete intended hierarchy draft, not only the leaf ticket.
4. **Approval** — show the draft and ask for confirmation. Apply requested changes; re-show if they alter hierarchy, category, priority, or scope. Never create while the user has not confirmed.
5. **Create in hierarchy order** — Epic → Story → Task/Bug, each via `atlassian:createJiraIssue` with `parent` set to the direct parent key (and, when the recon showed an Epic Link mechanism instead, whichever mechanism the project uses), plus priority and the category field. Then verify each new key with `atlassian:getJiraIssue` and add relate links via `atlassian:addGraphContext` (`jira-work-item-links-jira-work-item` / `jira-work-item-blocks-jira-work-item`) when applicable; if that tool is unavailable on this MCP server, find the link operation with `atlassian:discover`.
6. **Report** — list each created key with its title, type, epic, story, category field + value, and priority; confirm no orphan was created; mention anything left undone.

## Reference files (load as needed)

- `references/mcp-prereqs.md` — when the MCP is not connected or authentication fails: print its fallback step verbatim.
- `references/project-recon.md` — read before recon beginning in step 2.
- `references/ticket-standards.md` — read before drafting in step 3: title rules, description template, acceptance-criteria rules, priority matrix, category rules, hierarchy and link rules.

## Completion criteria

Done only when: the recon summary was produced; every created issue has title, type, an Epic and Story parent (or is itself the approved Epic/Story), a non-empty category value, a priority, and the standardized description; the user confirmed each draft before creation; the final keys were verified with `atlassian:getJiraIssue`; and no orphan ticket exists.
