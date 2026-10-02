# Templates

Copy, fill, adapt. Descriptions here follow the patterns in `authoring-guide.md`; adjust emphasis to the skill's real audience.

## Table of contents

1. [Full SKILL.md skeleton](#full-skillmd-skeleton)
2. [Slash command file (required companion)](#slash-command-file-required-companion)
3. [Description templates by kind](#description-templates-by-kind)
4. [Filled mini example](#filled-mini-example)

## Full SKILL.md skeleton

Every new skill is **user-invoked (manual) by default** — this is mandatory (Hard rule 11 of skill-creation). The skeleton below includes the invocation block and the required `## Invocation` section:

````markdown
---
name: <folder-name>
description: <what it does>. <Manual-invocation only (slash command); never picks automatically.>
metadata:
  opencode:
    slash: true
    autoinvoke: false
---

# <Skill Title>

## Invocation

User-invoked only. Load this skill ONLY when the user explicitly invokes it by name — `/<folder-name>`, "use <folder-name>", "load <folder-name>", or when the `/<folder-name>` command file triggers it. Never load automatically or on its own initiative for any other task.

<1–2 sentences: what this skill turns the agent into, and the one principle that holds it together.>

## Scope

Use this skill to: <bullets — actions covered>.
Out of scope: <explicit exclusions, pointing to the right skill/path if one exists>.

## Hard rules

<If none apply, delete the section. Number them; keep each one testable.>

## Workflow

1. <Step with what to do, and "done when…" state.>
2. <If a step depends on data/knowledge, name the exact file and the condition to read it: "read references/X.md when you need Y".>
3. <Verification step — name the command or checklist, and what passing looks like.>

## Reference files (load as needed)

- `references/<topic>.md` — read when <specific condition>.
- `scripts/<name>.<ext>` — run when <specific condition>.

## Completion criteria

Done only when: <concrete, checkable conditions — e.g. verification ran, user informed of restart requirement>.
````

### Slash command file (required companion)

Every skill ships with `.opencode/commands/<name>.md` so the user can call it via `/<name>`:

````markdown
---
description: Load the <name> skill (manual invocation only)
agent: build
subtask: false
---

Use the native `skill` tool to load the skill `<name>`, then follow its instructions for the user's request. Do not invoke any other skill.

$ARGUMENTS
````

Notes when adapting:

- Delete sections that don't apply — never ship empty headings.
- Keep the skill at one capability; if the draft covers two, split into two skills.
- Body length: routing + rules only; details belong in `references/`.
- Keep `metadata.opencode.autoinvoke: false` and the `## Invocation` section in every skill unless the user explicitly overrides this rule.

## Description templates by kind

Fill the trigger keywords with words the user would actually type:

**Tool/process skill**
```
Runs <action> against <target>. Use when the user asks to <action>, mentions <filetype/keyword>, or says <phrase>. Do not use for <adjacent topic>.
```

**Conventions/standards skill**
```
Applies <domain> standards for <writing/reviewing/debugging> <artifact>. Use when <implementing|refactoring|reviewing> <artifact>, <config type>, or <dependency>. Do not use for <non-domain work>.
```

**Meta-skill (create/validate other skills)**
```
Create, modify, validate, and install other skills. Use when the user asks to create a skill, write a SKILL.md, review or audit a skill, or asks about skill authoring best practices.
```

**Domain-knowledge skill**
```
Provides <knowledge> for <task>. Use when the user asks about <topic>, or when <artifact/situation> appears in context.
```

**Workflow/discipline skill (multi-phase processes)**
```
A discipline for <hard task>: <phase-1 promise>, <phase-2 promise>, <phase-3 promise>. Use when the user says "<trigger 1>", "<trigger 2>", or reports <symptom>.
```

## Filled mini example

A small conventions skill — manual invocation, as every new skill must be:

````markdown
---
name: sql-review
description: Applies this repository's SQL review standards - naming, index placement, and migration safety rules - when reviewing migrations or query changes. Manual-invocation only (slash command); never picks automatically.
metadata:
  opencode:
    slash: true
    autoinvoke: false
---

# SQL Review

## Invocation

User-invoked only. Load this skill ONLY when the user explicitly invokes it by name - `/sql-review`, "use sql-review", "load sql-review", or when the `/sql-review` command file triggers it. Never load automatically.

Review migrations against fixed repo rules. Repo standards override defaults; flag every deviation as a suggestion, not a blocker.

## Workflow

1. Pin the diff: `git diff <base>...HEAD -- '**/migrations/**'`. If empty, report "nothing to review" and stop.
2. Read `references/standards.md` — naming, index rules, migration safety. It overrides any general rule here.
3. For each file: check table/column names, index placement, and destructive steps.
4. Report findings grouped by severity, each with file:line and a suggested fix.

## Completion criteria

Findings issued only on files present in the pinned diff; every finding points to repo-specific rules.
````
