# BMAD Method — New Project Quickstart Guide

> How to start a brand new project from scratch using Claude Code CLI with the BMAD Method. Follow these steps in order to go from idea to fully functional app.

---

## Setup (One-Time)

### 1. Create Your Project Folder

```bash
mkdir ~/Projects/your-project-name
cd ~/Projects/your-project-name
```

### 2. Initialize Git

```bash
git init
```

### 3. Install BMAD

```bash
npx bmad-method install --modules "bmm,tea" --tools "claude-code" --user-name "YourName" --yes
```

This creates:
- `_bmad/` — BMAD installation (config, modules, workflows)
- `.claude/skills/` — 53 Claude Code skills as slash commands
- `{output_folder}/` — Where all artifacts will be saved

### 4. Start Claude Code

```bash
claude
```

---

## The Workflow (Run In Order)

**Important:** Each step below should be run in a **fresh Claude Code session** (`/clear` or restart `claude`) to keep context clean. BMAD is designed this way — each agent gets a focused context window.

---

### Phase 1 — Analysis

#### Create Product Brief

```
/bmad-product-brief
```

- **Agent:** Mary (Business Analyst)
- **What to do:** Describe your app idea. Brain dump everything — features, users, problems, constraints. Don't hold back.
- **Modes available:**
  - Default (guided) — agent walks you through 5 stages with questions
  - `--autonomous` — agent produces the brief with minimal interaction
  - `--yolo` — agent drafts everything first, then you refine
- **Output:** `{output_folder}/planning-artifacts/product-brief.md`
- **Tip:** At every pause, the agent asks "Anything else?" — this consistently draws out extra context you didn't know you had

---

### Phase 2 — Planning

#### Create PRD

```
/bmad-create-prd
```

- **Agent:** John (Product Manager)
- **What to do:** Respond to guided prompts at each of 12 steps. Press `C` to continue.
- **You provide:** Vision, success metrics, user journeys, scope decisions (what's in V1, what's out)
- **Output:** `{output_folder}/planning-artifacts/prd.md`
- **Tip:** Don't rush this. The PRD is the foundation — bad requirements = bad code downstream

#### Validate PRD (Recommended)

```
/bmad-validate-prd
```

- **Agent:** Validation Architect
- **What to do:** Let it run 13 validation checks. Fix any issues it surfaces.
- **Output:** Validation report with findings

#### Create UX Design (Recommended If Your App Has a UI)

```
/bmad-create-ux-design
```

- **Agent:** Sally (UX Designer)
- **What to do:** Collaborate on UX patterns and design specs
- **Output:** `{output_folder}/planning-artifacts/ux-design-specification.md`

---

### Phase 3 — Solutioning

#### Create Architecture

```
/bmad-create-architecture
```

- **Agent:** Winston (Architect)
- **What to do:** Walk through 8 steps. You decide tech stack, data architecture, patterns.
- **Menu at each step:**
  - `C` — Continue to next step
  - `A` — Advanced Elicitation (deeper analysis)
  - `P` — Party Mode (multi-agent review of the decision)
- **Output:** `{output_folder}/planning-artifacts/architecture.md`
- **Tip:** Choose a tech stack your team is comfortable with. Winston suggests options — pick what you know.

#### Create Epics & Stories

```
/bmad-create-epics-and-stories
```

- **Agent:** Bob (Scrum Master)
- **What to do:** Review and approve the epic structure and story breakdown
- **Output:** `{output_folder}/planning-artifacts/epics.md`

#### Check Implementation Readiness (Quality Gate — Don't Skip)

```
/bmad-check-implementation-readiness
```

- **Agent:** Expert PM + Scrum Master
- **What to do:** Let it validate all artifacts are aligned. Fix any gaps before coding.
- **Output:** Readiness report (READY / NEEDS WORK / NOT READY)
- **Tip:** This catches costly gaps before you waste time coding. Always run it.

---

### Phase 4 — Implementation

#### Sprint Planning

```
/bmad-sprint-planning
```

- **Agent:** Bob (Scrum Master)
- **What to do:** Nothing — fully autonomous. Parses epics and generates the tracking file.
- **Output:** `{output_folder}/implementation-artifacts/sprint-status.yaml`

#### The Story Loop (Repeat for Each Story)

This is the core development cycle. For each story:

**1. Create the story spec:**
```
/bmad-create-story
```
- Auto-discovers the next backlog story, or specify one: `/bmad-create-story 2-3`
- Produces a comprehensive story file with full dev context
- Output: `{output_folder}/implementation-artifacts/{story-key}.md`

**2. Implement the code:**
```
/bmad-dev-story
```
- Auto-discovers the next ready-for-dev story
- Runs autonomously using red-green-refactor cycle
- Updates task checkboxes as it progresses
- **Tip:** Run in `acceptEdits` permission mode so it writes code without asking for every file edit

**3. Review the code:**
```
/bmad-code-review
```
- Runs three parallel review layers: Blind Hunter, Edge Case Hunter, Acceptance Auditor
- If issues found → go back to `/bmad-dev-story` (it auto-detects review follow-ups)
- If approved → move to next story

**4. Check progress:**
```
/bmad-sprint-status
```
- Dashboard showing story/epic counts by status
- Recommends what to do next

**5. Commit after each story:**
```bash
git add -A && git commit -m "Story X.Y: description"
```

#### When an Epic is Complete

```
/bmad-retrospective
```

- **Agent:** Bob (Scrum Master) facilitating a simulated team (Alice, Charlie, Dana, Elena)
- **What to do:** Participate as "Project Lead" — share what went well, what didn't
- **Output:** `{output_folder}/implementation-artifacts/epic-{N}-retro-*.md`

---

## Quick Shortcut — For Bug Fixes & Small Changes

```
/bmad-quick-dev
```

Skips the full pipeline. Just describe what you want ("fix the login bug", "add rate limiting") and it creates a lightweight spec and implements.

---

## Navigation Commands (Available Anytime)

| Command | What It Does |
|---------|-------------|
| `/bmad-help` | Shows where you are in the workflow and recommends next step |
| `/bmad-sprint-status` | Dashboard of progress across all stories and epics |
| `/bmad-correct-course` | Manage significant changes mid-sprint (scope change, pivot) |
| `/bmad-quick-dev` | Bypass full pipeline for small tasks |

---

## Tips for Building a Fully Functional App

1. **Be specific during Product Brief** — the more detail you give Mary, the better everything downstream
2. **Don't rush the PRD** — this is the foundation. Spend the time here.
3. **During Architecture, choose a stack you know** — don't experiment with new tech in a demo project
4. **Always run Implementation Readiness check** — it catches gaps that would cost hours to fix later
5. **Run `/bmad-dev-story` in `acceptEdits` permission mode** — autonomous code writing without constant approval prompts
6. **Commit after each story** — clean git history per story, easy to review and rollback
7. **Fresh session per skill** — don't chain multiple skills in one session. Each agent needs clean context.
8. **Use `/bmad-help` when stuck** — it reads your current project state and tells you exactly what to do next

---

## Summary: The Complete Sequence

```
1.  /bmad-product-brief                  ← Capture your idea
2.  /bmad-create-prd                     ← Full requirements
3.  /bmad-validate-prd                   ← Quality check
4.  /bmad-create-ux-design               ← UX specs (if UI)
5.  /bmad-create-architecture            ← Technical design
6.  /bmad-create-epics-and-stories       ← Break into stories
7.  /bmad-check-implementation-readiness ← GATE: Ready to build?
8.  /bmad-sprint-planning                ← Generate sprint tracking
9.  /bmad-create-story                   ← Prepare next story
10. /bmad-dev-story                      ← Build it
11. /bmad-code-review                    ← Review it
12. /bmad-sprint-status                  ← Track progress
    ... repeat 9-12 per story ...
13. /bmad-retrospective                  ← Post-epic review
```
