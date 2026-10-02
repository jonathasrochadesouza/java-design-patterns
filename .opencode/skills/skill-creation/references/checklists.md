# Checklists, testing, and troubleshooting

## Table of contents

1. [Pre-publish checklist](#pre-publish-checklist)
2. [Fresh-session testing protocol](#fresh-session-testing-protocol)
3. [Observe → refine → retest loop](#observe--refine--retest-loop)
4. [Troubleshooting](#troubleshooting)

## Pre-publish checklist

Before publishing (writing to an install location), fix every failing item.

### Structure

- [ ] Folder name matches `name` in frontmatter; both pass `^[a-z0-9]+(-[a-z0-9]+)*$`, 1–64 chars.
- [ ] File is exactly `SKILL.md`; frontmatter fields are only `name`, `description`, `license`, `compatibility`, `metadata` (no invented keys).
- [ ] `description` is 1–1024 chars.
- [ ] Reference tree is flat: `SKILL.md` → each reference directly; no reference→reference links.
- [ ] No empty sections or leftover template placeholders.

### Manual invocation (mandatory — Hard rule 11)

- [ ] Frontmatter has `metadata.opencode.slash: true` and `metadata.opencode.autoinvoke: false`.
- [ ] Body has a `## Invocation` section: load ONLY when explicitly invoked by name — never automatically.
- [ ] Description ends with "Manual-invocation only (slash command); never picks automatically."
- [ ] Companion command file `.opencode/commands/<name>.md` exists with the standard template (load `<name>` via the `skill` tool, `$ARGUMENTS`).
- [ ] No auto-trigger phrasing left ("Use when the user asks…") that could leak auto-invocation.

### Description

- [ ] Third person, imperative-ish frames: "Runs…", "Applies…", "Use when…" — never "I can help…" / "You can use this to…".
- [ ] States both **what** the skill does and **when** to trigger it.
- [ ] Contains the literal trigger words the user would type.
- [ ] Negative scope present when adjacent skills/artifacts exist ("Do not use for…").

### Body

- [ ] Under ~500 lines / ~5k tokens; heavier material shelved into references.
- [ ] Workflow steps numbered with explicit completion criteria where the step could be skipped.
- [ ] Every reference/script mention says **when** to load it (a condition, not "see X").
- [ ] Each fact lives in exactly one place (no body↔reference duplication).
- [ ] Templates provided for any fixed output format.
- [ ] Gotchas that the agent wouldn't foresee live in the body, not a reference.
- [ ] Hard rules separate from heuristics; loud emphasis (`MUST`/`NEVER`) only on genuine failure modes.
- [ ] Ends with completion criteria.
- [ ] No secrets, credentials, absolute local paths.

### Fresh eyes

- [ ] Reading only the description: can you tell exactly when to pick this skill against 100 competitors?
- [ ] A fresh agent could follow the workflow without the conversation context you had while writing.

## Fresh-session testing protocol

Skills are validated by agent behavior, not by reading. After drafting/nesting changes:

1. **Pick 2–3 test prompts a real user would type** — including edge cases ("half-spec" requests where the skill's quality shows). Run them in a **fresh session** (or a subagent with no memory of writing the skill). **The user must invoke the skill explicitly** (via `/<name>` or by name) — a manual-invocation skill is never triggered by plain task prompts.
2. For each run, check four things:
   - **Trigger** — did the explicit user invocation load it?
   - **No auto-trigger** — on unrelated tasks, does the agent NOT pick it (this is the point of `autoinvoke: false`)?
   - **Routing** — does it load the right reference/script for the current condition?
   - **Execution** — does it follow the workflow steps in order, respecting hard rules?
   - **Format** — if the skill prescribes output templates, does the output match?
3. If the skill can't be exercised this time (no runnable surface), test the next best proxy: run the trigger check (would fresh you pick it?) and confirm paths/regex/reference links resolve — then say explicitly that only a real run validates behavior.

## Observe → refine → retest loop

After any real use of the skill:

1. **Observe**: what did the agent do that contradicted the skill — skipped a step, misordered, missed a trigger?
2. **Refine**: adjust the skill at the exact failure point — a wrong step starts too late? — and consider whether the description failed on the trigger.
3. **Retest** in a fresh session. Each iteration reflects real agent behavior, not assumptions.

Repeat while the skill is in active use: add rules for failures actually observed, not ones you imagined.

## Troubleshooting

**Skill not showing up / not listed:**
1. `SKILL.md` all-caps? Folder name == `name`?
2. Frontmatter has `name` **and** `description`? (Missing description = filtered out.)
3. In a scanned location? Given project scoping — from your current working directory up to the git worktree root — check `.opencode/skills/<name>/SKILL.md` (or `.claude/`/`.agents/` equivalents).
4. Permission gate: `permission.skill` in `opencode.json` matching `deny` hides it; `ask` requires approval.
5. Name collision with a skill in another scope (project overrides global).
6. Optionally set explicit paths in config: `"skills": { "paths": [...] }`, then **restart**.

**Loads but never triggers:**
- Manual-invocation skills only fire on explicit user invocation (`/<name>` or by name) — that's by design. On explicit invocation, check the command file exists at `.opencode/commands/<name>.md` and points to the exact skill name.
- For legacy auto-invoked skills: description lacks trigger keywords, or is first-person/vague. Rewrite per `authoring-guide.md → Description engineering`.

**Loads but is partially followed / agency skips steps:**
- Steps lack completion criteria; verification may be in a reference (move to body); hard rules buried in prose.

**Agent reads a reference too late or not at all:**
- Loading condition missing/vague; the file might be referenced only through another reference (flatten it to link directly from `SKILL.md`).

**Everything feels ignored on long tasks:**
- Body likely bloated — context competition. Split detail into references and keep routing in the body.

**Agent auto-triggers a skill that should be manual:**
- One of the three pieces is missing: `autoinvoke: false` in frontmatter, the `## Invocation` body section, or the description's manual-only marker. Fix all three per Hard rule 11; also verify the slash command file exists so the user keeps a reliable entry point.
- See "not showing up" above; also confirm the correct scope (project vs global)? `~/.config/opencode/skills/<name>/SKILL.md` for global, and that a **new session** is required for skills added mid-run.
