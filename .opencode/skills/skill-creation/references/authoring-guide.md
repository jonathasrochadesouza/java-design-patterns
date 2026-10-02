# Skill Authoring Guide

Deep rules for writing skills that agents discover reliably and follow correctly. Read this before drafting any `SKILL.md`.

## Table of contents

1. [Context budget](#context-budget)
2. [Anatomy of a skill folder](#anatomy-of-a-skill-folder)
3. [What goes where](#what-goes-where)
4. [Progressive disclosure](#progressive-disclosure)
5. [Description engineering](#description-engineering)
6. [Body writing rules](#body-writing-rules)
7. [References rules](#references-rules)
8. [Scripts rules](#scripts-rules)
9. [Templates rule](#templates-rule)
10. [Frontmatter fields](#frontmatter-fields)
11. [Optional UI metadata (`agents/openai.yaml`)](#optional-ui-metadata-agentsopenaiyaml)
12. [Deriving a skill from real usage](#deriving-a-skill-from-real-usage)
13. [Anti-patterns](#anti-patterns)

## Context budget

Once a skill activates, its full `SKILL.md` body loads into the agent's context alongside the system prompt, conversation history, descriptions of all other skills, and the user's request. Every token competes for attention.

- **Add what the agent lacks; omit what it already knows.** Don't document generic habits the model has anyway (writing clean code, asking clarifying questions). Capture only the project-specific procedure, conventions, and non-obvious judgment calls.
- Target under **500 lines / ~5k tokens** for the body. Oversized bodies measurably degrade agent performance.
- Heavy detail (schemas, API docs, workflows, examples) goes into `references/` files, loaded on demand.

## Anatomy of a skill folder

```
skill-name/
├── SKILL.md              (required — frontmatter + body)
├── agents/
│   └── openai.yaml       (optional — UI metadata for skill pickers)
├── scripts/              (optional — executable, deterministic helpers)
├── references/           (optional — docs read on demand)
└── assets/               (optional — files used in output: templates, icons)
```

Order of preference when adding a resource: if it must execute with exact semantics → `scripts/`; if it is background knowledge read when needed → `references/`; if it is a byte-for-byte reusable artifact → `assets/`. When in doubt, prefer `references/` over bloating the body.

## What goes where

| Content | Lives in |
| --- | --- |
| Trigger conditions, hard rules, workflow steps, completion criteria | `SKILL.md` body |
| Detailed rules the trigger conditions can announce ("if X happens, read Y") | `references/*.md` |
| Validation logic, deterministic transformations, scaffolding | `scripts/` |
| Output formats the agent must reproduce verbatim | template file or inline template in the body |
| Reproductions of past failures and their fixes | `SKILL.md` body (short) or a reference |
| Secrets, credentials, environment-specific paths | Nowhere — never |

Danger: **non-obvious gotchas belong in the body**, not a reference. The agent reading a reference only loads it when it recognizes the need; for a trap it cannot yet see coming, the body is the only place guaranteed to be read.

## Progressive disclosure

Three levels of loading:

1. **Metadata** — `name` + `description`, always in the system prompt (~100 words). The discovery surface.
2. **`SKILL.md` body** — loaded only when the skill triggers. The workflow brain.
3. **Bundled resources** — `references/`, `scripts/`, `assets/`, loaded or executed on demand. No context cost until used.

Consequence: the body is a **router** first. It should tell the agent what to do now, and point precisely to the file that holds what it will need in step N — with the condition that triggers reading it.

## Description engineering

The description is the single most-read line of the skill: with many skills installed, the agent compares descriptions to pick one. Write it to win that comparison.

Rules:

1. **Third person always.** The string is injected into the system prompt — mixed POV breaks discovery.
2. **Structure: [what it does]. [how/when it triggers]. [negative scope if needed].**
3. **Front-load trigger keywords** — the literal words a user would type ("create a skill", "SKILL.md", "review a skill").
4. **"Pushy" descriptions win.** "Use when the user asks to create a new skill, write a SKILL.md, review or audit an existing skill…" beats "Helps with skill authoring".
5. **Gate with "Use ONLY when…"** to keep a skill quiet on adjacent topics.

Good patterns by kind:

- Tool/process skill: "Runs X against Y. Use when the user asks to X, mentions [file type], or says [trigger phrase]. Do not use for [adjacent topic]."
- Conventions/standards skill: "Applies [domain] standards when writing, reviewing, or debugging [domain]. Use when [trigger]."
- Meta-skill (like this one): "Create, modify, validate, and install other skills. Use when the user asks to create a skill, write a SKILL.md, or asks about skill authoring."
- Domain-knowledge skill: "Provides [knowledge] for [task]. Use when [task is mentioned]."

Anti-patterns:

- "I can help you process PDFs" — first person.
- "This skill is about PDFs" — no triggers.
- "Processes documents" — wins nothing against a 100-skill competitor list.

## Body writing rules

- Open with one or two sentences establishing what the skill is and its one key principle.
- State scope early: what triggers it, what is explicitly out of scope.
- Put **hard rules before heuristics**; number the non-negotiables.
- Write numbered **workflow steps** with explicit completion criteria per phase ("done when…"), so the agent cannot skip validation.
- Use imperative voice, not "you may want to consider".
- Prefer examples over prose when teaching a format — an example beats three paragraphs of explanation.
- Reserve bold/caps (`MUST`, `NEVER`) for actual failure modes — if everything is loud, nothing is.
- End with completion criteria: what must be true before declaring done.

## References rules

- **One level deep.** Every reference is linked directly from `SKILL.md`, with its loading condition ("read X when Y"). Never reference → reference: the agent may only partially read the second hop.
- Name files by content: `form_validation_rules.md`, not `doc2.md`.
- Any reference over ~100 lines opens with a table of contents.
- No duplication: a fact lives in the reference **or** the body, not both.

## Scripts rules

- Scripts execute; their source is not read. Rely on stdout/stderr as the interface.
- Keep each script single-purpose with clear stdin/args and useful exit codes.
- In the body, distinguish "run this" from "read this": `run scripts/validate.py` vs. `see references/api.md`.
- Cross-platform skills should avoid platform-locked scripts, or say explicitly which shell they require.

## Templates rule

When the agent must produce output in a fixed format, provide the template (inline or as an asset file) rather than describing the format in prose. Templates are dramatically more reliable than descriptions. Show a filled example next to the blank skeleton when possible.

## Frontmatter fields

Recognized fields (unknown ones are silently ignored):

| Field | Required | Constraint |
| --- | --- | --- |
| `name` | yes | 1–64 chars; `^[a-z0-9]+(-[a-z0-9]+)*$`; equals folder name |
| `description` | yes | 1–1024 chars; third person; triggers + scope ✓ manual-only marker |
| `license` | no | e.g. `MIT` |
| `compatibility` | no | e.g. `opencode` |
| `metadata` | no | map, e.g. `audience: maintainers` |

### Invocation contract (mandatory for new skills)

Every new skill is **user-invoked (manual) by default** — the model never triggers it on its own. Two pieces make that real, and both are required:

```yaml
metadata:
  opencode:
    slash: true        # visible in interactive slash-command catalogs
    autoinvoke: false  # excluded from model-facing discovery (agent never auto-loads)
```

Plus a `## Invocation` section in the body stating: load ONLY when explicitly invoked by name (`/<name>`, "use <name>", "load <name>") or via the `/<name>` command file — never automatically. And a companion `.opencode/commands/<name>.md` slash-command file so `/<name>` works (template in `templates.md → Slash command file`).

End the description with "Manual-invocation only (slash command); never picks automatically."

Why default to manual: zero context cost (the description never sits in the model's catalog), the user is the sole trigger, and procedures the user wants to control explicitly (deploys, scaffolding, generators) are the common case. If a user asks to make a skill auto-invoked, comply only with an explicit, unambiguous override.

## Optional UI metadata (`agents/openai.yaml`)

Some skill UIs (chips, pickers) read display metadata:

```yaml
interface:
  display_name: "Skill Creation"
  short_description: "Create and validate OpenCode skills"
```

Optional and cosmetic — only add it when the host UI consumes it, and keep `short_description` under ~60 chars.

## Deriving a skill from real usage

The best skills are extracted, not imagined:

1. Do the task once with the agent, prompting normally. As you work, you naturally repeat context — table names, conventions, filters, "always exclude test accounts".
2. Note what you repeated. That repetition is the skill.
3. Ask the agent: "Create a skill capturing this [X] pattern, including [schemas/conventions/rules] we used."
4. Test the draft on a fresh session doing the same task; observe what it misses; refine.

## Anti-patterns

- **Mega-skill**: one skill covering many unrelated domains — splits discovery and bloats the body. Split into focused skills.
- **Reference chains**: SKILL.md → A → B. The agent half-reads B. Flatten to SKILL.md → A, SKILL.md → B.
- **Duplicated facts** in body and references that drift apart on edit.
- **Wall-of-prose descriptions** with no trigger words.
- **First-person voice** anywhere in frontmatter.
- **Procedural knowledge in references only**: workflow steps the agent needs on every run must be in the body — the agent may never open the reference.
- **Speculative edge cases**: documenting hypothetical failures nobody hit. Add rules when real usage shows the need (iterate, don't pre-bloat).
- **Silent words**: "see references/ for details" with no condition. Say when to read.
- **Auto-invoked by accident**: shipping a new skill without `autoinvoke: false` + `## Invocation` + companion command file — the model starts triggering it on its own (violates Hard rule 11).
