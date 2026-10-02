# Ticket Standards

The single source of truth for what a valid ticket looks like. Draft every issue against this file.

## Table of contents

1. [Types](#types)
2. [Title rules](#title-rules)
3. [Description template](#description-template)
4. [Acceptance criteria rules](#acceptance-criteria-rules)
5. [Priority](#priority)
6. [Category](#category)
7. [Hierarchy rules](#hierarchy-rules)
8. [Relations and links](#relations-and-links)
9. [Filled example](#filled-example)

## Types

| Requested work | Type to use |
| --- | --- |
| Broken behavior, regression | Bug |
| New feature with end-user value | Story |
| Multi-feature initiative without shipping alone | Epic |
| Internal/technical chore (upgrade, cleanup, infra) | Task |

## Title rules

- Verb first + object + qualifier: `Fix 500 on checkout when cart is emptied before submit`, `Add rate limiting to POST /api/auth/login`.
- ≤ 80 chars, no ticket-type prefix (`Bug:`, `[TASK]`), no vague words (`improve`, `optimize`, `various`, `stuff`).
- Readable in a backlog without opening the ticket.

## Description template

Use these headers exactly, in this order:

```markdown
## Context
Why this work exists, in 2–4 sentences: the problem, how it was noticed, impact.

## Objective
What the ticket achieves, when done, in 1–3 sentences.

## Scope
- Items the deliverable will include

## Out of Scope
- Adjacent work NOT included (explicitly; create separate tickets for them)

## Acceptance Criteria
- [ ] Criterion 1 (testable; see rules below)
- [ ] Criterion 2

## Notes and References
- Links, screenshots, dependencies, related tickets — or state "None".
```

For a **Bug**, replace `Scope` / `Out of Scope` with these sections before `Acceptance Criteria`:

```markdown
## Steps to Reproduce
1. Exact step
2. Exact step

## Expected vs Actual
- Expected: <correct behavior>
- Actual: <observed behavior>

## Environment
<browser/OS/app version/URL, or "All">
```

## Acceptance criteria rules

- 2–6 per ticket; binary pass/fail, no "better/faster"; include at least one edge or error-path criterion when the work touches validation, conversion, or concurrency.
- Use Given/When/Then or concrete checkbox statements; acceptance criteria describe behavior, never prescribe implementation.

## Priority

Set from business impact — not effort, not urgency to the requester — and justify in one line in `Notes and References`:

| Value | Meaning |
| --- | --- |
| Highest | System down, data loss, security incident |
| High | Major flow broken, no workaround |
| Medium | Feature degraded but has a workaround |
| Low | Polish, minor annoyance |

Do not default to `Medium` for everything; if the user says urgency words, ask what breaks without it.

## Category

- Exactly one carrier field — the one detected in the project recon.
- The value MUST come from existing project values; reuse casing from `fields.customFields` verbatim.
- If the user requests a value not found in the project, surface it: confirm before creating a novel value.
- The field is set at creation — a category missing at creation is a Hard rule 2 violation.

## Hierarchy rules

- Mapping: Epic contains Stories; a Story (or Epic, for technical chores) contains Tasks/Bugs/Technical Debt.
- Parent mechanism: the one detected in recon — modern `parent` field (preferred) or the legacy Epic Link custom field. Never parent a Story under a Task or a Task under an Epic directly (that pattern only appears inside recon when the project lacks Stories entirely; flag and confirm with the user).
- Create in hierarchy order and link each child to its direct parent (`parent` key in `createJiraIssue` where supported; Epic Link otherwise).
- A Story's description follows the same template but its `Objective` carries the user value: `As a <user>, I want <capability>, so that <benefit>`.
- An Epic's description summarizes the initiative and lists child Stories in `Scope`.

## Relations and links

- Dependencies between issues: `atlassian:addGraphContext` with `jira-work-item-links-jira-work-item` (generic link; title text says the kind) or `jira-work-item-blocks-jira-work-item` (predecessor → blocker → successor).
- When the project recon found no custom category field but later the user asks to also track datasource META, do not invent schema — app-specific datasource should not be created on the fly.
- Links also visible in prose: merge dependencies into the description's `Notes and References`.

## Filled example

**Type:** Task · **Parent:** Story `WEB-120` under Epic `WEB-100`

> **Title:** Add rate limiting to POST /api/auth/login
>
> **Context:** Login endpoint receives bursts of unauthenticated requests (ops alert, ~5k/min). No throttle exists; brute-force attempts are not slowed down.
>
> **Objective:** Reject repeated failed logins per IP+account after a threshold, using the shared Redis store.
>
> **Scope:**
> - Sliding-window limiter on failed attempts (5 per 15 min per IP+account)
> - Standard `Retry-After` header on 429 responses
> - Limiter metrics exported for the auth dashboard
>
> **Out of Scope:** CAPTCHA (WEB-130); login form UI changes (WEB-131)
>
> **Acceptance Criteria:**
> - [ ] 6th failed login within 15 min receives HTTP 429 with `Retry-After` ≥ 60
> - [ ] Successful login resets the failure counter for that IP+account
> - [ ] Limiter bypassed when the source is the allowlist
>
> **Notes and References:** Priority High justified by: public authentication flow degraded with a workaround only via support. Category value: `auth-api`.

(desc category value `auth-api` here shown as a label carrier example — the field set at creation is whatever the recon detected.)
