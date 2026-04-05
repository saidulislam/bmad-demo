# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **BMAD Method v6.2.2** project — an AI-driven agile development framework. The demo project being built is an **API Health Dashboard** (enterprise internal tool for a financial institution) currently in Phase 3 (Solutioning).

BMAD uses specialized AI agent personas and structured workflows to move from product ideation through implementation. Each workflow is invoked as a slash command (e.g., `/bmad-create-prd`) and produces markdown artifacts that feed the next phase.

## BMAD Configuration

- **Modules installed:** Core, BMM (BMad Method), TEA (Test Architect Enterprise v1.7.2)
- **IDE integration:** Claude Code (53 skills in `.claude/skills/`)
- **User config:** `_bmad/bmm/config.yaml` — project name, user name (`Saidul`), skill level (`intermediate`), artifact paths
- **TEA config:** `_bmad/tea/config.yaml` — test stack auto-detect, Playwright enabled, risk threshold `p1`

## Directory Structure That Matters

```
_bmad/                              # BMAD installation — DO NOT modify manually
  _config/manifest.yaml             # Installation state
  _config/bmad-help.csv             # Workflow routing catalog (phases, dependencies, outputs)
  _config/skill-manifest.csv        # All 53 skills with metadata
  _config/agent-manifest.csv        # Agent personas (Mary, John, Winston, etc.)
  bmm/config.yaml                   # BMM module settings and artifact paths
  tea/config.yaml                   # TEA module settings
  bmm/{1-analysis,2-plan-workflows,3-solutioning,4-implementation}/  # Workflow source files

.claude/skills/                     # 53 skill entry points — each has SKILL.md, many point to workflow.md

{output_folder}/                    # All generated artifacts (literal directory name with braces)
  planning-artifacts/               # Briefs, PRDs, architecture, epics
  implementation-artifacts/         # Sprint status, stories, code reviews
  test-artifacts/                   # Test plans, traceability matrices

docs/                               # Project knowledge (referenced by bmm config as project_knowledge)
```

**Important:** The output folder is literally named `{output_folder}` (with braces) on disk. This is the resolved default from BMAD installation. Config files reference it via `{project-root}/{output_folder}/planning-artifacts`.

## Current Demo Project State

**Project:** API Health Dashboard — real-time API monitoring for engineering teams
**Tech stack (from architecture):** Spring Boot (Kotlin) + React + TimescaleDB + Redis

Completed artifacts in `{output_folder}/planning-artifacts/`:
- `product-brief.md` — Phase 1 output (Analyst: Mary)
- `prd.md` — Phase 2 output (PM: John) — 26 FRs, 18 NFRs, 5 user journeys
- `architecture.md` — Phase 3 output (Architect: Winston) — 4 ADRs, full project structure

**Next step:** Create Epics & Stories (`/bmad-create-epics-and-stories`)

## BMAD Workflow Sequence

Required path (gate = must complete before proceeding):

1. `/bmad-product-brief` → `planning-artifacts/product-brief.md`
2. `/bmad-create-prd` → `planning-artifacts/prd.md` **(gate)**
3. `/bmad-validate-prd` → validation report
4. `/bmad-create-ux-design` → `planning-artifacts/ux-design-specification.md` (optional)
5. `/bmad-create-architecture` → `planning-artifacts/architecture.md` **(gate)**
6. `/bmad-create-epics-and-stories` → `planning-artifacts/epics.md` **(gate)**
7. `/bmad-check-implementation-readiness` **(gate)**
8. `/bmad-sprint-planning` → `implementation-artifacts/sprint-status.yaml`
9. `/bmad-create-story` → `implementation-artifacts/{story-key}.md`
10. `/bmad-dev-story` → code implementation
11. `/bmad-code-review` → review findings
12. `/bmad-sprint-status` → progress check
13. `/bmad-retrospective` → post-epic review

Quick bypass: `/bmad-quick-dev` for bug fixes and small changes (skips full pipeline).

## Key BMAD Conventions

- Each skill should run in a **fresh conversation** for clean context
- Skills load config from `_bmad/bmm/config.yaml` — respect `communication_language`, `document_output_language`, `user_skill_level`
- Artifacts use **append-only** document building with YAML frontmatter tracking `stepsCompleted`
- PRD quality: high information density, SMART requirements, full traceability chain (Vision → Success Criteria → User Journeys → FRs), zero anti-patterns (no "user-friendly", no implementation leakage)
- Architecture must define enforcement guidelines that all dev agents follow (naming, structure, format patterns)
- Sprint status uses YAML with status flow: `backlog → ready-for-dev → in-progress → review → done`

## Workflow Orchestration

### Plan Before Building
- Enter plan mode for ANY non-trivial task (3+ steps or architectural decisions)
- If something goes sideways, STOP and re-plan immediately — don't keep pushing
- Write detailed specs upfront to reduce ambiguity
- Use plan mode for verification steps, not just building

### Subagent Strategy
- Use subagents liberally to keep main context window clean
- Offload research, exploration, and parallel analysis to subagents
- For complex problems, throw more compute at it via subagents
- One task per subagent for focused execution

### Verification Before Done
- Never mark a task complete without proving it works
- Diff behavior between main and your changes when relevant
- Ask yourself: "Would a staff engineer approve this?"
- Run tests, check logs, demonstrate correctness

### Self-Improvement Loop
- After ANY correction from the user: update `tasks/lessons.md` with the pattern
- Write rules for yourself that prevent the same mistake
- Review lessons at session start for relevant project

## Task Management

1. **Plan First:** Write plan to `tasks/todo.md` with checkable items
2. **Verify Plan:** Check in before starting implementation
3. **Track Progress:** Mark items complete as you go
4. **Explain Changes:** High-level summary at each step
5. **Document Results:** Add review section to `tasks/todo.md`
6. **Capture Lessons:** Update `tasks/lessons.md` after corrections

## Core Principles

- **Simplicity First:** Make every change as simple as possible. Minimal code impact.
- **No Laziness:** Find root causes. No temporary fixes. Senior developer standards.
- **Minimal Impact:** Changes should only touch what's necessary. Avoid introducing bugs.
- **Demand Elegance (Balanced):** For non-trivial changes, pause and ask "is there a more elegant way?" If a fix feels hacky: "Knowing everything I know now, implement the elegant solution." Skip this for simple, obvious fixes — don't over-engineer.
- **Autonomous Bug Fixing:** When given a bug report, just fix it. Point at logs, errors, failing tests — then resolve them. Zero context switching required from the user.
