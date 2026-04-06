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
npx bmad-method install --modules "bmm,tea" --tools "claude-code" --user-name "Saidul" --yes
```

| Flag | Purpose |
|------|---------|
| `--modules "bmm,tea"` | Install BMad Method (agile SDLC) + Test Architect (quality gates) |
| `--tools "claude-code"` | Integrate with Claude Code IDE (53 slash commands) |
| `--user-name "Saidul"` | Name agents use to greet and address you in conversations |
| `--yes` | Accept all defaults without interactive prompts — repeatable and scriptable |

This creates:
- `_bmad/` — BMAD installation (config, modules, workflows)
- `.claude/skills/` — 53 Claude Code skills as slash commands
- `{output_folder}/` — Where all artifacts will be saved

### 4. Add the CLAUDE.md Template

Copy the default template into your project root. This gives Claude Code project context, auto-commit directives, and self-updating instructions:

```bash
cp /path/to/bmad-demo/docs/CLAUDE-template.md ./CLAUDE.md
```

Or if starting from scratch, grab it from the [CLAUDE-template.md](CLAUDE-template.md) in this repo.

**What the template does:**
- **Auto-commits** after every completed BMAD phase (no git commands needed)
- **Auto-updates itself** after each phase with project-specific details (tech stack, requirements count, epic list, etc.)
- Includes workflow orchestration rules, core principles, and task management directives
- Starts generic — becomes project-specific as you progress through phases

### 5. Set Up GitHub Remote

```bash
gh repo create your-project-name --public --source=. --push
```

Or if the repo already exists:

```bash
git remote add origin https://github.com/youruser/your-project-name.git
```

### 6. Start Claude Code

```bash
claude
```

---

## The Workflow (Run In Order)

**Auto-commit:** If you added the CLAUDE.md template (Step 4), Claude Code will **automatically commit and push to GitHub** after each completed phase. No git commands needed — your work is saved automatically. It also **auto-updates CLAUDE.md** with project details (tech stack, requirements, epics) as you progress. See [Git Commit Best Practices](#git-commit-best-practices) for the manual approach if you prefer.

### Why Fresh Sessions Matter

Each BMAD skill should ideally run in a **fresh Claude Code session** (`/clear` or restart `claude`). Here's why:

**The context window problem:** Each session has a finite context window. If you chain all skills in one session, earlier conversations (discovery prompts, drafts, debates) fill up the context. By the time you hit `/bmad-dev-story`, Claude may forget architecture constraints or cut corners because it's working with a stuffed context.

**Artifacts carry the context, not conversations.** When you start a fresh session, the agent reads the *artifacts* (the markdown files you produced) — not the conversation history. This gives each agent full attention on its task with clean context.

### What You Can Safely Chain vs. What Needs Fresh Sessions

| OK to Chain (Lightweight Pairs) | Why |
|---------------------------------|-----|
| `/bmad-product-brief` → `/bmad-create-prd` | Brief is short, PRD reads the artifact |
| `/bmad-sprint-planning` → `/bmad-create-story` | Sprint planning is autonomous and tiny |
| `/bmad-dev-story` → `/bmad-code-review` | Natural pair — review needs the dev context |

| Use Fresh Sessions (Heavy Skills) | Why |
|-----------------------------------|-----|
| `/bmad-create-prd` → `/bmad-create-architecture` | Both are multi-step, heavy context consumers |
| `/bmad-create-architecture` → `/bmad-create-epics-and-stories` | Both generate large documents with detailed decisions |
| Any skill after `/bmad-dev-story` completes | Code implementation fills context fast |
| `/bmad-create-epics-and-stories` → `/bmad-check-implementation-readiness` | Readiness check needs clean context to validate objectively |

### Rule of Thumb

- **Simple project** (small scope, few features) — chaining works fine
- **Real app** (10+ FRs, multiple epics) — fresh sessions produce noticeably better output
- **When in doubt** — start fresh. It costs 5 seconds and prevents quality degradation

---

## Understanding the A/P/C Menu

Throughout the BMAD workflow, you'll see this menu at decision points:

```
Select: [A] Advanced Elicitation  [P] Party Mode  [C] Continue
```

### `C` — Continue (Default, Use Most of the Time)

Accepts the agent's proposal and moves to the next step. This is your answer 90% of the time. The agent proposed something reasonable — move forward.

### `A` — Advanced Elicitation (When You Disagree or Want Deeper Analysis)

Pushes the agent to reconsider using structured critique methods — Socratic questioning, first principles, pre-mortem, red team analysis. Use it when:

- You disagree with a classification or decision but aren't sure how to articulate why
- The agent's proposal feels too surface-level and you want rigorous analysis
- You want to stress-test an assumption before locking it in

### `P` — Party Mode (Simulated Team Review)

Triggers a multi-agent discussion where all BMAD personas debate the decision from their perspectives. You'll see dialogue like:

```
John (PM): From a product perspective...
Winston (Architect): Technically, I'd push back on...
Sally (UX): For the user experience, we should consider...
Quinn (QA): From a testability standpoint...
```

When the discussion ends, you get the same `A/P/C` menu again — nothing changes automatically. You decide whether to incorporate their feedback or just press `C`.

#### When Party Mode Is Worth It

| Situation | Why It Helps |
|-----------|-------------|
| Architecture decisions you're unsure about | Architect and Developer might disagree on a tech choice — surfaces trade-offs you hadn't considered |
| Scope debates | PM might argue a feature is essential while Scrum Master says it bloats the MVP — helps you cut scope with confidence |
| Complex domain requirements | For regulated industries (fintech, healthcare), multiple perspectives catch compliance gaps early |
| You're the only person on the project | Simulates the peer review you'd normally get from colleagues — breaks you out of tunnel vision |
| Before a major decision you can't easily reverse | Database choice, auth architecture, monolith vs microservices — worth 2 minutes of simulated debate |

#### When to Skip Party Mode

- The decision is obvious (e.g., a personal tool is clearly a web app)
- You already know the answer and just want to move forward
- The project is low-complexity with straightforward requirements
- You're deep into implementation — Party Mode is most useful during planning and solutioning

#### Rule of Thumb

**Use `P` when you'd normally want a second opinion from a colleague.** If you wouldn't pause to ask someone, just press `C`.

---

## Parallel Execution: Multiple Agents Working in Parallel

BMAD's `/bmad-dev-story` is designed for **sequential, single-agent execution** — one story at a time, with strict state tracking. This is intentional: it ensures quality, validation, and clean code review per story.

But what if you want to ship faster? Here are your options.

### Option 1: Multiple Claude Code Sessions (Manual Parallelism)

Open multiple terminal windows, each running `claude` from the same project directory:

```bash
# Terminal 1
cd ~/Projects/your-project
claude
> /bmad-dev-story 1-2

# Terminal 2 (separate window)
cd ~/Projects/your-project
claude
> /bmad-dev-story 1-3
```

**Best for:** Stories with no overlapping files (e.g., one frontend story + one backend story).

**Caveats:**
- You manage merge conflicts manually if they touch the same code
- Sprint-status.yaml updates may collide — review before committing

### Option 2: Git Worktrees + Multiple Sessions (Cleaner Parallelism)

Each story gets its own working directory and branch — zero file conflicts during execution:

```bash
git worktree add ../your-project-story-1-2 -b story/1-2
git worktree add ../your-project-story-1-3 -b story/1-3

# Terminal 1
cd ../your-project-story-1-2
claude
> /bmad-dev-story 1-2

# Terminal 2
cd ../your-project-story-1-3
claude
> /bmad-dev-story 1-3
```

When done, merge each branch back to main. **This is the cleanest manual parallel approach.**

### Option 3: Bypass BMAD's Workflow — Ask Claude to Plan and Parallelize

If you want maximum speed, skip `/bmad-dev-story` entirely for the implementation phase. Ask Claude directly:

```
Read all the BMAD artifacts (PRD, architecture, epics).
Analyze the dependency graph between stories.
Build the entire app as fast as possible using subagents in parallel
where dependencies allow. Tell me your wave plan first, then execute.
Commit after each wave.
```

Claude will:

1. **Analyze dependencies** — produce a wave plan like:
   ```
   Wave 1 (sequential):  Story 1.1 (scaffolding) — must complete first
   Wave 2 (parallel x4): Story 1.2, 1.3, 1.4, 2.1 — independent
   Wave 3 (parallel x6): Story 2.2, 2.3, 2.4, 3.1, 7.1, 7.2
   Wave 4 (parallel x8): Story 3.2, 3.3, 3.4, 4.1, 4.2, 5.1, 6.1, 6.2
   ```

2. **Launch subagents per wave** — multiple agents in parallel using the Agent tool

3. **Brief each subagent directly** with the story spec and architecture context

4. **Commit after each wave** completes

### What You Trade Off with Parallel Execution

| BMAD Sequential (`/bmad-dev-story`) | Parallel Execution |
|------------------------------------|-------------------|
| Per-story `/bmad-code-review` runs after each implementation | Code review happens at the end (or per wave) |
| Sprint-status.yaml updates per story (visible progress) | Sprint-status updated in batch |
| Red-green-refactor cycle enforced | Subagents do their best but skip strict TDD cycle |
| Single point of failure per story | If one subagent fails, others may need rework |
| Easy to track which agent broke what | Harder to attribute issues to a specific agent |

### When to Use Each Approach

| Scenario | Recommended Approach |
|----------|---------------------|
| **Learning BMAD / training your team** | Sequential `/bmad-dev-story` — the discipline IS the lesson |
| **Production code with strict quality bars** | Sequential — validation gates catch bugs |
| **Personal projects, prototypes, demos** | Parallel via Option 3 — speed wins |
| **First epic of any project** | Sequential — build the foundation carefully |
| **Subsequent epics with established patterns** | Parallel — patterns are set, isolated stories ship fast |

### The Hybrid Approach (Best of Both Worlds)

Use BMAD sequentially for the **first epic** (foundation), then switch to parallel mode for the remaining epics:

1. **Epic 1 (Foundation):** Run `/bmad-dev-story` sequentially. This locks in patterns, project structure, naming conventions.
2. **Epics 2-N:** Switch to parallel — ask Claude to read epics.md and the now-established codebase, then build remaining stories in waves.

Result: solid foundation built carefully, fast iteration on isolated features.

### Limitations to Be Aware Of

- **Claude can't estimate wall-clock time accurately** — it doesn't know your machine speed, network latency, or how long file ops take. "By tomorrow" is a human concept, not a Claude one.
- **File collisions are real** — even with smart partitioning, two agents touching shared config files = problems. Always review before committing.
- **Subagents don't natively know BMAD's workflow protocol** — you have to brief each one with the full story spec and architecture context.
- **No automatic merge logic** — subagents don't know about each other's changes during execution.

---

## Who Executes Which Command (Role Mapping)

A common question: "Which BMAD commands does my PM run vs. my architect vs. my developers?" Each command maps to the role that would normally do that work in a traditional engineering team.

### Command-to-Role Mapping

| BMAD Command | Real-World Role |
|--------------|----------------|
| `/bmad-product-brief` | **Product Manager** (or Product Owner) |
| `/bmad-create-prd` | **Product Manager** (with input from stakeholders, business analyst) |
| `/bmad-validate-prd` | **Product Manager** or **Tech Lead** (sanity check) |
| `/bmad-create-ux-design` | **UX Designer** (or PM if no dedicated designer) |
| `/bmad-create-architecture` | **Architect** or **Tech Lead** |
| `/bmad-create-epics-and-stories` | **Tech Lead** or **Scrum Master** (with Architect input) |
| `/bmad-check-implementation-readiness` | **Tech Lead** or **Engineering Manager** (gate keeper) |
| `/bmad-sprint-planning` | **Scrum Master** or **Engineering Manager** |
| `/bmad-create-story` | **Tech Lead** or **Senior Developer** (preparing the story for the dev) |
| `/bmad-dev-story` | **Developer** (the person who would normally implement the story) |
| `/bmad-code-review` | **Senior Developer** or **Tech Lead** |
| `/bmad-sprint-status` | **Scrum Master**, **Engineering Manager**, or **Director/ED** |
| `/bmad-retrospective` | **Scrum Master** facilitates, whole team participates |
| `/bmad-quick-dev` | **Any Developer** (for bug fixes and small changes) |

### The Key Insight

**Each BMAD command maps to the role that would normally do that work.** The agent just becomes a force multiplier for that role:

- A PM still owns the PRD — they just use Claude to draft it faster
- An Architect still owns the design — Claude helps explore options and document decisions
- A Developer still owns the code — Claude implements based on the spec they prepared

BMAD is **role-respecting**: it doesn't replace your PM, architect, or developers — it gives each of them a structured AI co-pilot for their specific responsibilities.

### Three Ways to Distribute the Work

| Approach | How It Works | When to Use |
|----------|--------------|-------------|
| **Same person, multiple hats** | A single tech lead or senior developer runs the entire BMAD pipeline solo | Solo projects, prototypes, small teams. Faster but loses multi-perspective input |
| **Handoff between roles** | PM runs Phase 1-2 → Architect runs Phase 3 → Developers run Phase 4 | Larger teams with specialized roles. Slower but each role contributes their expertise |
| **Pair model** | PM + Tech Lead together for planning, then Developers take over for implementation | Best for important projects. Combines speed of solo with multi-perspective benefits |

### For Team Adoption

When introducing BMAD to your engineering organization, decide upfront which model fits your team:

- **Small teams (< 5 engineers)** — Same person, multiple hats. Tech lead runs the whole pipeline.
- **Medium teams with PM + Engineering** — Handoff model. Clear ownership transitions.
- **Larger orgs with dedicated roles** — Pair model for major initiatives, handoff for routine work.

The role mapping also makes it easier to set permissions and review responsibilities — your PMs touch planning artifacts, your engineers touch implementation artifacts.

---

## Testing Workflow Integration (TEA Module)

The default BMAD sequence does not include testing workflows in the core path — testing is treated as **opt-in**. But for enterprise teams, the TEA (Test Architect Enterprise) module should be considered **mandatory**. It weaves into the existing phases rather than being a separate stage.

### Why TEA Isn't in the Default Sequence

BMAD treats testing as opt-in because:
- Not every project needs full ATDD/CI setup (prototypes, demos, throwaway tools)
- TEA is a separate module — some installs don't have it
- Keeps the core sequence simple for first-time users

### Two Test/QA-Related Persona Sets

| Module | Persona | Command | Purpose |
|--------|---------|---------|---------|
| BMM (core) | Quinn (QA Engineer) | `/bmad-agent-qa` | Conversational QA persona — ask about test strategy, edge cases, coverage gaps. No fixed workflow attached |
| TEA (enterprise) | Murat (Test Architect) | `/bmad-tea` | Master test architect — has structured workflows for test design, automation, CI gates, NFR assessment |

**Murat (TEA) is the real testing power.** Quinn is for ad-hoc QA conversations.

### Full TEA Skill Catalog

| Command | When to Run | Purpose |
|---------|-------------|---------|
| `/bmad-tea` | Anytime | Talk to Murat (Test Architect persona) |
| `/bmad-teach-me-testing` | Anytime | Learn testing fundamentals (TEA Academy) |
| `/bmad-testarch-test-design` | After Architecture, before coding (Phase 3) | Risk-based test plan |
| `/bmad-testarch-framework` | Before first dev story (Phase 3 → 4) | Initialize test framework (Playwright/Cypress) |
| `/bmad-testarch-ci` | Before first dev story (Phase 3 → 4) | Set up CI/CD pipeline with quality gates |
| `/bmad-testarch-atdd` | Per story, before dev (Phase 4) | Generate failing acceptance tests (TDD red phase) |
| `/bmad-testarch-automate` | Per story, after dev (Phase 4) | Expand test coverage |
| `/bmad-testarch-nfr` | After implementation (Phase 4) | Assess non-functional requirements |
| `/bmad-testarch-test-review` | After implementation (Phase 4) | Quality audit (0-100 scoring) |
| `/bmad-testarch-trace` | After implementation (Phase 4) | Traceability matrix + gate decision |
| `/bmad-qa-generate-e2e-tests` | After feature complete (Phase 4) | Generate E2E tests for existing features |

### TEA-Enhanced BMAD Workflow

When TEA is included, the workflow looks like this:

```
Phase 1 — Analysis
  /bmad-product-brief

Phase 2 — Planning
  /bmad-create-prd
  /bmad-validate-prd
  /bmad-create-ux-design                  (optional)

Phase 3 — Solutioning
  /bmad-create-architecture
  /bmad-testarch-test-design              ← TEA: Risk-based test plan from architecture
  /bmad-testarch-framework                ← TEA: Initialize Playwright/Cypress
  /bmad-testarch-ci                       ← TEA: CI/CD quality gates
  /bmad-create-epics-and-stories
  /bmad-check-implementation-readiness

Phase 4 — Implementation (per story loop)
  /bmad-sprint-planning
  /bmad-create-story
  /bmad-testarch-atdd                     ← TEA: Failing acceptance tests first
  /bmad-dev-story
  /bmad-testarch-automate                 ← TEA: Expand coverage
  /bmad-code-review
  /bmad-testarch-test-review              ← TEA: Quality score
  /bmad-testarch-trace                    ← TEA: Traceability matrix
  /bmad-testarch-nfr                      ← TEA: NFR validation
  /bmad-sprint-status
  ... repeat per story ...
  /bmad-retrospective
```

### Enterprise Recommendation

For enterprise team adoption, position TEA as **the enterprise enhancement layer** to BMAD's core workflow. These additions should be mandatory:

| When | Required Command | Why |
|------|------------------|-----|
| After architecture | `/bmad-testarch-test-design` | Risk-based test strategy upfront |
| Before first story | `/bmad-testarch-framework` | Test framework ready before any code is written |
| Before first story | `/bmad-testarch-ci` | CI quality gates enforced from day 1 |
| Per story | `/bmad-testarch-atdd` → `/bmad-dev-story` → `/bmad-testarch-automate` | Strict TDD cycle enforced |
| After each story | `/bmad-testarch-test-review` | Quality score before marking done |
| End of epic | `/bmad-testarch-trace` + `/bmad-testarch-nfr` | Traceability and NFR validation for compliance evidence |

### When to Skip TEA Workflows

- Personal prototypes and throwaway tools
- Demo projects where speed > rigor
- Spikes and proof-of-concepts

For everything else — and especially anything heading toward production — use TEA.

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

## Git Commit Best Practices

### Option A: Automated Commits (Recommended for Non-Technical Users)

Add the following to your project's `CLAUDE.md` file and Claude Code will **automatically commit and push** after every completed BMAD phase. No git commands needed.

Add this section to `CLAUDE.md`:

```markdown
## Auto-Commit After BMAD Phases

**IMPORTANT:** After completing any BMAD skill that produces or modifies an artifact,
automatically commit and push to GitHub. Do NOT ask the user — just do it.

| After This Skill | Commit Message |
|-----------------|----------------|
| `/bmad-product-brief` | `"Phase 1: Product brief"` |
| `/bmad-create-prd` | `"Phase 2: PRD"` |
| `/bmad-validate-prd` | `"Phase 2: PRD validation"` |
| `/bmad-create-ux-design` | `"Phase 2: UX design"` |
| `/bmad-create-architecture` | `"Phase 3: Architecture"` |
| `/bmad-create-epics-and-stories` | `"Phase 3: Epics and stories"` |
| `/bmad-check-implementation-readiness` | `"Phase 3: Implementation readiness - READY"` |
| `/bmad-sprint-planning` | `"Phase 4: Sprint planning"` |
| `/bmad-dev-story` + `/bmad-code-review` (approved) | `"Story X.Y: {story title}"` |
| `/bmad-retrospective` | `"Epic N: Retrospective"` |
| `/bmad-quick-dev` | `"Quick dev: {brief description of change}"` |

Rules:
- Use `git add -A` to stage all changes
- Push to the current branch immediately after committing
- If no git remote exists, prompt the user to set one up first
- If a skill fails or is abandoned mid-step, do NOT commit partial work
```

**Prerequisites:** The project must have `git init` done and a GitHub remote set up before the first BMAD skill runs. Set this up during Step 1-2 of the setup process.

### Option B: Manual Commits (When to Commit and Push)

| When | Commit Message Example | Why |
|------|----------------------|-----|
| After `/bmad-product-brief` | `"Phase 1: Product brief"` | Milestone artifact — your idea is captured |
| After `/bmad-create-prd` | `"Phase 2: PRD"` | Foundation document — rollback point if scope changes |
| After `/bmad-validate-prd` | `"Phase 2: PRD validation"` | Quality gate evidence |
| After `/bmad-create-ux-design` | `"Phase 2: UX design"` | Design decisions locked in |
| After `/bmad-create-architecture` | `"Phase 3: Architecture"` | Technical design locked in |
| After `/bmad-create-epics-and-stories` | `"Phase 3: Epics and stories"` | Work breakdown complete |
| After `/bmad-check-implementation-readiness` | `"Phase 3: Implementation readiness - READY"` | Boundary between planning and coding |
| After each story (`/bmad-dev-story` + `/bmad-code-review` approved) | `"Story 1.1: Project scaffolding"` | One story = one commit. Clean history, easy to review and revert |
| After `/bmad-retrospective` | `"Epic 1: Retrospective"` | Closes out an epic cleanly |

### When NOT to Commit

- **Mid-step** — e.g., halfway through `/bmad-create-prd` (it builds the doc incrementally across 12 steps)
- **After `/bmad-sprint-planning` alone** — bundle it with the first story commit
- **After `/bmad-create-story` alone** — bundle the story spec with its `/bmad-dev-story` code (spec + code belong together)

### The Pattern

```bash
# After each planning phase
git add -A && git commit -m "Phase 1: Product brief"
git add -A && git commit -m "Phase 2: PRD"
git add -A && git commit -m "Phase 3: Architecture"
git add -A && git commit -m "Phase 3: Epics and stories"
git add -A && git commit -m "Phase 3: Implementation readiness - READY"

# After each completed story (spec + code + review together)
git add -A && git commit -m "Story 1.1: Project scaffolding"
git add -A && git commit -m "Story 1.2: SSO authentication"
git add -A && git commit -m "Story 2.1: API registration form"

# Push whenever you want — after each commit or in batches
git push
```

This gives you a git history that reads like a project timeline:
```
Story 2.1: API registration form
Story 1.2: SSO authentication
Story 1.1: Project scaffolding
Phase 3: Implementation readiness - READY
Phase 3: Epics and stories
Phase 3: Architecture
Phase 2: PRD
Phase 1: Product brief
Initial commit
```

---

## Tips for Building a Fully Functional App

1. **Be specific during Product Brief** — the more detail you give Mary, the better everything downstream
2. **Don't rush the PRD** — this is the foundation. Spend the time here.
3. **During Architecture, choose a stack you know** — don't experiment with new tech in a demo project
4. **Always run Implementation Readiness check** — it catches gaps that would cost hours to fix later
5. **Run `/bmad-dev-story` in `acceptEdits` permission mode** — autonomous code writing without constant approval prompts
6. **Commit after each planning phase and each completed story** — see Git Commit Best Practices above
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
