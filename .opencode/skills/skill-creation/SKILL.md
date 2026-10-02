---
name: skill-creation
# opencode/autoinvoke=false keeps this skill OUT of model-facing discovery:
# the agent never triggers it on its own. Invoke explicitly via /skill-creation.
description: Skill creation by best practicies to opencode. Considering authoring, improvement, validation, and installation of OpenCode agent skills (SKILL.md folders). Covers the full skill-creator workflow — intent capture, progressive disclosure, trigger-optimized descriptions, references/scripts layout, checklists, and testing — plus the OpenCode installation paths. Manual-invocation only (slash command); never picks automatically.
metadata:
  opencode:
    slash: true
    autoinvoke: false
---

# Skill Creation

A skill is a folder containing a `SKILL.md` (plus optional bundled resources) that extends the agent with procedural knowledge. This skill is the meta-skill: it turns authoring skills into a disciplined, repeatable process.

## Invocation

User-invoked only. Load this skill ONLY when the user explicitly invokes it by name — `/skill-creation`, "use skill-creation", "load skill-creation", or when the `/skill-creation` command file triggers it. Never load automatically or on its own initiative for any other task, including Java, coding, or general authoring work.

## Scope

Use this skill to:

- Create a new skill from scratch
- Improve, extend, or refactor an existing skill
- Audit/validate a skill against authoring best practices
- Install (move or copy) a skill into the right OpenCode location

Out of scope: editing `opencode.json`, agents, commands, plugins, MCP, or permissions — for those, use the built-in `customize-opencode` skill instead.

## Hard rules (non-negotiable)

1. Frontmatter recognizes only `name`, `description` (required) and `license`, `compatibility`, `metadata` (string→string, optional). Unknown keys are silently ignored — do not invent fields.
2. `name`: 1–64 chars, must match the regex `^[a-z0-9]+(-[a-z0-9]+)*$` (no leading/trailing/double hyphens) and must equal the folder name that contains `SKILL.md`. The file is `SKILL.md` in all caps.
3. `description`: 1–1024 chars, written in **third person** ("Use when...", never "I can help you..."), covering **what it does + when to trigger it**, with the literal keywords the user would say front-loaded. No description → the skill is never surfaced.
4. `SKILL.md` body: keep under ~500 lines / ~5k tokens. Beyond that, shelf details into `references/` files — the body is routing, not a dump.
5. References sit **one level deep** from `SKILL.md` and each mention includes when to read it. Files over ~100 lines get a table of contents. Never link reference → reference.
6. Prefer **scripts** for deterministic/repetitive operations (they execute without being read) and **templates** for any fixed output format. Name both files descriptively (`validate_fields.py`, not `doc2.py`).
7. Do not duplicate content between `SKILL.md` and references — each fact lives in exactly one place.
8. MCP tools are always fully qualified as `server-name:tool_name`.
9. One skill = one coherent capability. Avoid "mega-skills" that sprawl across unrelated domains.
10. Never embed secrets or environment-specific credentials in a skill.
11. **Every new skill is user-invoked (manual) by default — mandatory.** Always set in frontmatter plus generate the matching command file (see step 2 of the Workflow). Follow the exact block below. If the user asks to auto-invoke, do not comply without an explicit override; otherwise the skill must stay manual.

## Location model (install targets)

| Scope            | Path                                      |
| ---------------- | ----------------------------------------- |
| Project (opencode) | `.opencode/skills/<name>/SKILL.md`        |
| Global (opencode)  | `~/.config/opencode/skills/<name>/SKILL.md` |
| Project (compat)  | `.claude/skills/<name>/SKILL.md` or `.agents/skills/<name>/SKILL.md` |
| Global (compat)   | `~/.claude/skills/<name>/SKILL.md` or `~/.agents/skills/<name>/SKILL.md` |

Keep `name` unique across all locations; prefer the project copy over the global copy — if you mirror the same skill in both, keep the contents identical.

## Workflow

1. **Capture intent.** Interview the user (open-ended, not yes/no): what task, what trigger words, what context they keep repeating, what the agent gets wrong without the skill. Check available MCPs for research. Capture intent only — the agent reorders details into instructions itself.
2. **Draft.** Copy the closest matching template from `references/templates.md`, fill it, then apply the description patterns. Every new skill is user-invoked only: add the mandatory invocation block (see Hard rule 11) to the frontmatter and section headers, plus create the slash command `.opencode/commands/<name>.md` (template in `references/templates.md → Slash command file`). Explain the process briefly to the user before writing.
3. **Validate.** Walk the pre-publish checklist in `references/checklists.md`. Fix every failing item before claiming done.
4. **Test.** Run the fresh-session test protocol in `references/checklists.md` (does it trigger? does it route? did it follow the steps?). Skills are validated by agent behavior, not by reading.
5. **Iterate.** Use the observe → refine → retest loop from `references/checklists.md`. Fix gaps the real run exposes rather than imagined edge cases.
6. **Install.** Write to one of the location-model paths, then tell the user the skill only appears on session restart and confirm what remains untested. Confirm the invocation contract back to the user: "manual only — invoke via /<name>".

## Reference files (load as needed)

- `references/authoring-guide.md` — read before drafting. Deep rules: context budget, anatomy of a skill folder, description engineering with good/bad patterns, progressive disclosure, when content belongs in `SKILL.md` vs `references/` vs `scripts/`.
- `references/templates.md` — complete `SKILL.md` skeleton, description templates by skill kind, optional `agents/openai.yaml` (UI metadata for skill lists).
- `references/checklists.md` — pre-publish validation checklist, fresh-session testing protocol, the observe→refine→retest loop, and troubleshooting when a skill fails to load or trigger.

## Completion criteria

Done only when: the checklist passes (including the "Manual invocation" section), at least one fresh-session test ran with the user explicitly invoking `/<name>` (or an explicit reason it could not), the file is in a valid install location, and you told the user that a restart/new session is required for the skill to load.
