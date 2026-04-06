# API Health Dashboard — BMAD Method Demo

A hands-on demonstration of the [BMAD Method](https://github.com/bmad-code-org/BMAD-METHOD) (Breakthrough Method for Agile AI-Driven Development) v6.2.2, walking through the **complete** structured AI-assisted development lifecycle — from product ideation through architecture, sprint planning, and working code.

**Demo project:** An internal real-time monitoring tool for engineering teams to track API uptime, latency, error rates, and SLA compliance across microservices — designed for an enterprise context with regulatory compliance requirements.

---

## What This Repo Contains

### Planning Artifacts (Phases 1-3)

| Phase | Artifact | Agent | Description |
|-------|----------|-------|-------------|
| 1. Analysis | [Product Brief]({output_folder}/planning-artifacts/product-brief.md) | Mary (Analyst) | Executive product vision — problem, solution, differentiators, success criteria, scope |
| 2. Planning | [PRD]({output_folder}/planning-artifacts/prd.md) | John (PM) | Full requirements doc — 26 functional requirements, 18 NFRs, 5 user journeys, domain compliance |
| 3. Solutioning | [Architecture]({output_folder}/planning-artifacts/architecture.md) | Winston (Architect) | Technical design — Spring Boot/Kotlin + React + TimescaleDB, 4 ADRs, project structure, implementation patterns |
| 3. Solutioning | [Epics & Stories]({output_folder}/planning-artifacts/epics.md) | Bob (Scrum Master) | 7 epics, 25 stories with BDD acceptance criteria, dependency graph, FR coverage map |
| 3. Solutioning | [Readiness Report]({output_folder}/planning-artifacts/implementation-readiness-report-2026-04-05.md) | PM + SM | Quality gate — 26/26 FRs covered, 0 critical issues, **READY** |

### Implementation Artifacts (Phase 4)

| Artifact | Agent | Description |
|----------|-------|-------------|
| [Sprint Status]({output_folder}/implementation-artifacts/sprint-status.yaml) | Bob (Scrum Master) | Sprint tracking — 7 epics, 25 stories, all marked `done` |
| [Story 1.1 Spec]({output_folder}/implementation-artifacts/story-1-1-project-scaffolding.md) | Story Engine | Full dev-ready story spec with tasks, acceptance criteria, and completion notes |

### Working Application Code

| Layer | What's Implemented |
|-------|-------------------|
| **Infrastructure** | `docker-compose.yaml` (TimescaleDB + Redis), `Dockerfile` (multi-stage), `Jenkinsfile` (CI/CD), `k8s/` (deployment, service, ingress, configmap) |
| **Backend (Kotlin)** | Spring Boot app, REST API (standard response format), audit service, registration service, telemetry adapter pattern (interface + AppDynamics adapter + factory), WebSocket config (STOMP), Redis config, security config (SAML placeholder) |
| **Database** | 4 Flyway migrations — api_registrations, api_metrics hypertable with continuous aggregates + 30-day retention policy, immutable audit_logs with trigger protection, user_roles with AD group mappings |
| **Frontend (React/TS)** | Vite + React Router, 6 page components, Axios API client, TypeScript types (matching architecture response format), 3 custom hooks (useAuth, useHealthMetrics, useWebSocket) |

### Documentation

| Guide | Purpose |
|-------|---------|
| [BMAD Method Overview](docs/1.bmad-method-overview.md) | What BMAD is — agents, phases, principles, modules |
| [Installation & Skills Reference](docs/2.bmad-installation-guide.md) | How to install, all 53 skills listed, 10 agent personas, recommended workflow |
| [Step-by-Step Workflow Walkthrough](docs/3.bmad-workflow-walkthrough.md) | How to run each phase — commands, what you provide, what gets produced, modes and flags |
| [Demo Execution Log](docs/bmad-demo-execution-log.md) | What we actually did in each phase — decisions made, inputs provided, outputs breakdown |
| [New Project Quickstart](docs/bmad-new-project-quickstart.md) | Start-to-finish guide for creating a new project with BMAD + Claude Code CLI |

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
  --user-name "Saidul" \
  --yes
```

| Flag | Purpose |
|------|---------|
| `--modules "bmm,tea"` | Install BMad Method (agile SDLC) + Test Architect (quality gates) |
| `--tools "claude-code"` | Integrate with Claude Code IDE (53 slash commands) |
| `--user-name "Saidul"` | Name agents use to greet and address you in conversations |
| `--yes` | Accept all defaults without interactive prompts — makes install repeatable and scriptable |

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
├── docker-compose.yaml                 # Local dev: TimescaleDB + Redis
├── Dockerfile                          # Multi-stage production build
├── Jenkinsfile                         # CI/CD pipeline
├── .env.example                        # Environment variable template
│
├── backend/                            # Spring Boot (Kotlin) application
│   ├── build.gradle.kts
│   └── src/main/
│       ├── kotlin/.../                 # Controllers, services, adapters, models, repos, config
│       └── resources/
│           ├── application.yaml
│           └── db/migration/           # 4 Flyway migrations (TimescaleDB hypertables)
│
├── frontend/                           # React (TypeScript) application
│   ├── package.json
│   ├── vite.config.ts
│   └── src/
│       ├── pages/                      # 6 page components
│       ├── hooks/                      # useAuth, useHealthMetrics, useWebSocket
│       ├── services/                   # Axios API client
│       └── types/                      # TypeScript type definitions
│
├── k8s/                                # Kubernetes manifests
│   ├── deployment.yaml                 # 2 replicas, rolling update, health probes
│   ├── service.yaml
│   ├── ingress.yaml
│   └── configmap.yaml
│
├── _bmad/                              # BMAD installation (do not modify manually)
│   ├── _config/                        # Manifests and registries
│   ├── core/                           # Core module (12 skills)
│   ├── bmm/                            # BMad Method module (34+ workflows)
│   └── tea/                            # Test Architect Enterprise module
│
├── .claude/skills/                     # 53 Claude Code skill entry points
│
├── {output_folder}/                    # Generated BMAD artifacts
│   ├── planning-artifacts/
│   │   ├── product-brief.md            # ← Phase 1 output
│   │   ├── prd.md                      # ← Phase 2 output
│   │   ├── architecture.md             # ← Phase 3 output
│   │   ├── epics.md                    # ← Phase 3 output
│   │   └── implementation-readiness-report-2026-04-05.md  # ← Quality gate
│   ├── implementation-artifacts/
│   │   ├── sprint-status.yaml          # ← Phase 4 sprint tracking
│   │   └── story-1-1-project-scaffolding.md  # ← Phase 4 story spec
│   └── test-artifacts/                 # Test plans, traceability matrices
│
└── docs/                               # Project documentation & guides
    ├── 1.bmad-method-overview.md
    ├── 2.bmad-installation-guide.md
    ├── 3.bmad-workflow-walkthrough.md
    ├── bmad-demo-execution-log.md
    └── bmad-new-project-quickstart.md
```

---

## Demo Completion Status

| Phase | Step | Status |
|-------|------|--------|
| 1. Analysis | Product Brief | ✅ Complete |
| 2. Planning | PRD (26 FRs, 18 NFRs) | ✅ Complete |
| 3. Solutioning | Architecture (4 ADRs) | ✅ Complete |
| 3. Solutioning | Epics & Stories (7 epics, 25 stories) | ✅ Complete |
| 3. Solutioning | Implementation Readiness | ✅ READY |
| 4. Implementation | Sprint Planning | ✅ Complete |
| 4. Implementation | Story Creation & Dev | ✅ 25/25 Done |
| 4. Implementation | Application Code | ✅ Backend + Frontend + Infra |

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
- [New Project Quickstart](docs/bmad-new-project-quickstart.md) — How to start your own project with BMAD
