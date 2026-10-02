# Project Recon

A fast, read-only check of the target project so the new ticket matches the project's real patterns instead of a generic ideal. Read-only: never write during recon.

## Steps

1. **Resolve the site once:** `atlassian:getAccessibleAtlassianResources` → cache the cloudId (skip if already cached this session). Confirm Jira access.
2. **Sample the existing hierarchy:** `atlassian:searchJiraIssuesUsingJql` with view `evidence`, `maxResults: 20`, JQL:
   `project = <KEY> AND issuetype in (Epic, Story) ORDER BY created DESC`
   Learn from `fields.customFields` and `fields.parent`: title style, whether titles start with verbs, description structure (section headers?), and **the Epic-Link mechanism** (modern `parent` field vs. a legacy "Epic Link" custom field — use whatever the project actually uses).
3. **Confirm types and fields for creation:** if the sample left doubt about issue types or priorities available, use `atlassian:discover` ("jira project issue types metadata") then `atlassian:executeRead` for the metadata; optionally `atlassian:getJiraIssue` on one recent ticket with `includeEditableFields: true` to see editable field names and allowed values.
4. **Detect the category carrier field**, in this order of preference:
   - `components` — if recent ready tickets consistently set components → this is the category field
   - `labels` — same consistency test
   - a "Category"-like custom field — when components and labels are unused/empty and a category-like custom field is consistently populated
   Record the exact field key and 3–5 typical values from the project (reuse these verbatim for consistent casing).
5. **Detect the language and style:** ticket language (PT/EN/mixed), description headers actually used, priority values in use, common link types (relates-to, blocks).

## Detection output (summary to show the user, ≈10 lines)

```text
- Project: <KEY> — <name>
- Hierarchy: Epic → Story → Task/Bug; parent mechanism: <parent field | Epic Link custom>
- Category carrier: <components|labels|customfield_X "Category"> (typical: v1, v2, v3)
- Priority scale: <values in use>
- Language: <PT/EN>
- Description style detected: <headers sections used | one-liners | none>
- Rules I will NOT copy from this project: <e.g. "stories have no epics", "statuses bypass AC">
```

Always list at least one item under "rules I will NOT copy" when the sample shows drift from this skill's standards; never silently copy bad patterns because they exist.

## Done when

- The summary above is written and shown to the user (or the project is empty → report "greenfield; drafting with defaults").
- Total recon tool calls ≤ ~6.
- Every detection cites what it is based on (keys/counts from the sample), so the user can correct a wrong inference.
