# API Health Dashboard — BMAD Method Demo

A hands-on demonstration of the [BMAD Method](https://github.com/bmad-code-org/BMAD-METHOD) (Breakthrough Method for Agile AI-Driven Development) v6.2.2, walking through the full structured AI-assisted development lifecycle from product ideation to implementation-ready architecture.

**Demo project:** An internal real-time monitoring tool for engineering teams to track API uptime, latency, error rates, and SLA compliance across microservices — designed for an enterprise context with regulatory compliance requirements.

---

## What This Repo Contains

### Planning Artifacts (Completed Phases 1-3)

These are the actual outputs produced by walking through the BMAD workflow with AI agent personas:

| Phase | Artifact | Agent | Description |
|-------|----------|-------|-------------|
| 1. Analysis | [Product Brief]({output_folder}/planning-artifacts/product-brief.md) | Mary (Analyst) | Executive product vision — problem, solution, differentiators, success criteria, scope |
| 2. Planning | [PRD]({output_folder}/planning-artifacts/prd.md) | John (PM) | Full requirements doc — 26 functional requirements, 18 NFRs, 5 user journeys, domain compliance |
| 3. Solutioning | [Architecture]({output_folder}/planning-artifacts/architecture.md) | Winston (Architect) | Technical design — Spring Boot/Kotlin + React + TimescaleDB, 4 ADRs, project structure, implementation patterns |

### Documentation

| Guide | Purpose |
|-------|---------|
| [BMAD Method Overview](docs/1.bmad-method-overview.md) | What BMAD is — agents, phases, principles, modules |
| [Installation & Skills Reference](docs/2.bmad-installation-guide.md) | How to install, all 53 skills listed, 10 agent personas, recommended workflow |
| [Step-by-Step Workflow Walkthrough](docs/3.bmad-workflow-walkthrough.md) | How to run each phase — commands, what you provide, what gets produced, modes and flags |
| [Demo Execution Log](docs/bmad-demo-execution-log.md) | What we actually did in each phase — decisions made, inputs provided, outputs breakdown |

### Configuration

| File | Purpose |
|------|---------|
| [CLAUDE.md](CLAUDE.md) | Claude Code project context — architecture, conventions, workflow orchestration principles |
| [_bmad/_config/manifest.yaml](_bmad/_config/manifest.yaml) | BMAD installation manifest — version, modules, IDE |
| [_bmad/bmm/config.yaml](_bmad/bmm/config.yaml) | BMM module settings — project name, artifact paths, user config |
| [_bmad/tea/config.yaml](_bmad/tea/config.yaml) | TEA module settings — test framework, CI platform, risk threshold |

---

## BMAD Installation

**Prerequisites:** Node.js v20+

```bash
npx bmad-method install \
  --directory /path/to/your/project \
  --modules "bmm,tea" \
  --tools "claude-code" \
  --user-name "YourName" \
  --yes
```

This installs:
- **Core** — help system, brainstorming, editorial reviews, elicitation tools
- **BMM** (BMad Method) — full agile SDLC framework with 34+ workflows and 9 agent personas
- **TEA** (Test Architect Enterprise) — risk-based test strategy, automation, CI/CD quality gates

---

## The BMAD Workflow

```
Phase 1: Analysis
  /bmad-product-brief              → Product brief (what are we building?)

Phase 2: Planning
  /bmad-create-prd                 → PRD with SMART requirements
  /bmad-validate-prd               → Quality gate
  /bmad-create-ux-design           → UX specifications (optional)

Phase 3: Solutioning
  /bmad-create-architecture        → Technical architecture + ADRs
  /bmad-create-epics-and-stories   → Epics with BDD acceptance criteria
  /bmad-check-implementation-readiness → Quality gate

Phase 4: Implementation
  /bmad-sprint-planning            → Sprint tracking (sprint-status.yaml)
  /bmad-create-story               → Story file with full dev context
  /bmad-dev-story                  → Code implementation
  /bmad-code-review                → Adversarial code review
  /bmad-sprint-status              → Progress dashboard
  /bmad-retrospective              → Post-epic team review
```

Each phase produces artifacts that feed the next. Quality gates prevent incomplete work from moving forward.

---

## Agent Personas

| Agent | Name | Role | Invoke |
|-------|------|------|--------|
| Analyst | Mary | Requirements & research | `/bmad-agent-analyst` |
| Product Manager | John | PRD & acceptance criteria | `/bmad-agent-pm` |
| Architect | Winston | Technical design | `/bmad-agent-architect` |
| UX Designer | Sally | UX patterns & specs | `/bmad-agent-ux-designer` |
| Scrum Master | Bob | Sprint planning & stories | `/bmad-agent-sm` |
| Developer | Amelia | Story implementation | `/bmad-agent-dev` |
| QA Engineer | Quinn | Test automation & coverage | `/bmad-agent-qa` |
| Tech Writer | Paige | Documentation | `/bmad-agent-tech-writer` |
| Quick Flow Dev | Barry | Rapid spec & implementation | `/bmad-agent-quick-flow-solo-dev` |
| Test Architect | Murat | Quality strategy & CI/CD gates | `/bmad-tea` |

---

## Project Structure

```
bmad-demo/
├── CLAUDE.md                           # Claude Code project guidance
├── README.md                           # This file
├── _bmad/                              # BMAD installation (do not modify manually)
│   ├── _config/                        # Manifests and registries
│   ├── core/                           # Core module (12 skills)
│   ├── bmm/                            # BMad Method module (34+ workflows)
│   │   ├── 1-analysis/                 # Phase 1 workflows
│   │   ├── 2-plan-workflows/           # Phase 2 workflows
│   │   ├── 3-solutioning/              # Phase 3 workflows
│   │   └── 4-implementation/           # Phase 4 workflows
│   └── tea/                            # Test Architect Enterprise module
├── .claude/skills/                     # 53 Claude Code skill entry points
├── {output_folder}/                    # Generated artifacts
│   ├── planning-artifacts/             # Briefs, PRDs, architecture, epics
│   │   ├── product-brief.md            # ← Phase 1 output
│   │   ├── prd.md                      # ← Phase 2 output
│   │   └── architecture.md             # ← Phase 3 output
│   ├── implementation-artifacts/       # Sprint status, stories, reviews
│   └── test-artifacts/                 # Test plans, traceability matrices
└── docs/                               # Project documentation & guides
    ├── 1.bmad-method-overview.md
    ├── 2.bmad-installation-guide.md
    ├── 3.bmad-workflow-walkthrough.md
    └── bmad-demo-execution-log.md
```

---

## Why BMAD?

Traditional AI-assisted coding ("vibe coding") is ad-hoc and unpredictable. BMAD solves three problems:

1. **Unpredictability** — Structured agents with scoped roles produce consistent outputs
2. **Hallucination & drift** — Quality gates between phases catch issues early
3. **Context loss** — Persistent markdown artifacts preserve context across sessions

Documentation is the source of truth. Code is a downstream derivative.

---

## Resources

- [BMAD Method GitHub](https://github.com/bmad-code-org/BMAD-METHOD) — Framework source (43k+ stars)
- [BMAD Documentation](https://docs.bmad-method.org/) — Official docs
- [BMAD Discord](https://discord.gg/gk8jAdXWmj) — Community & support
