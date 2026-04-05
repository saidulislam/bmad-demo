---
stepsCompleted: [1, 2, 3, 4, 5, 6, 7, 8]
inputDocuments: [product-brief.md, prd.md]
workflowType: 'architecture'
project_name: 'API Health Dashboard'
user_name: 'Saidul'
date: '2026-04-05'
lastStep: 8
status: 'complete'
completedAt: '2026-04-05'
---

# Architecture Decision Document

## Project Context Analysis

### Requirements Overview

**Functional Requirements:** 26 FRs across 6 capability areas — API Registration (5), Health Dashboard (5), Portfolio View (4), SLA Reporting (4), Alerting (4), Access Control & Audit (4).

**Non-Functional Requirements:** 18 NFRs across 5 quality attributes — Performance (3), Availability & Reliability (4), Scalability (3), Security (5), Data Retention (3).

**Scale & Complexity:**
- 2,000 registered APIs target capacity
- 10,000 metric data points/second ingestion rate
- 500 concurrent dashboard users
- 13 months metric retention, 7 years audit retention
- 4 telemetry source types (AppDynamics, Dynatrace, Splunk, API Gateway)
- Sub-60-second metric freshness requirement

### Technical Constraints & Dependencies

| Constraint | Impact |
|-----------|--------|
| Firm-approved infrastructure (on-prem Kubernetes or approved cloud tenancy) | Rules out SaaS-hosted solutions; must deploy to internal infrastructure |
| Firm SSO (SAML 2.0 / Active Directory) | Authentication architecture must integrate with existing identity provider |
| Firm-approved technology stack (Technology Review Board) | Technology choices must be from approved catalog |
| Application Security Review (ASR) required | Security architecture must be documented and defensible before production |
| Existing telemetry integration (AppDynamics, Dynatrace, Splunk, API Gateway) | Must build adapters for 4 source types, not a single unified API |

### Cross-Cutting Concerns

- **Audit logging** — all configuration changes must be immutable and retained 7 years (DR-2)
- **Encryption** — AES-256 at rest, TLS 1.2+ in transit (DR-1)
- **RBAC** — three roles (Viewer, Operator, Admin) enforced across all endpoints (DR-4)
- **Data classification** — all data is Internal Use; no PII ingestion (DR-1)
- **Operational resilience** — the dashboard itself is a Tier-2 service with 99.9% availability target (DR-3)

---

## Starter Template Evaluation

### Primary Technology Domain

**Full-stack web application** with a heavy backend focus on data ingestion, time-series storage, and real-time streaming to frontend dashboards.

### Starter Options Considered

| Option | Stack | Pros | Cons |
|--------|-------|------|------|
| Spring Boot + React | Java/Kotlin, React, PostgreSQL | Firm-standard, strong enterprise support, approved by TRB | Heavier boilerplate, slower iteration |
| Node.js (Express/Fastify) + React | TypeScript, React, PostgreSQL | Fast iteration, shared language front/back, strong real-time support | Less common in firm's backend catalog |
| Python (FastAPI) + React | Python, React, PostgreSQL | Excellent for data pipelines, fast API development | Less standard for firm web applications |

### Selected Starter

**Spring Boot (Kotlin) + React + TimescaleDB**

**Rationale:**
- Spring Boot is on the firm's approved technology catalog and widely used across engineering teams
- Kotlin provides modern language features with full Java interop — reduces boilerplate while staying in the JVM ecosystem
- React is the firm-standard frontend framework
- TimescaleDB (PostgreSQL extension) provides time-series optimization without introducing a non-approved database

---

## Core Architectural Decisions

### Data Architecture

**Database: TimescaleDB (PostgreSQL extension)**

| Decision | Detail |
|----------|--------|
| **Primary store** | TimescaleDB for time-series metric data (hypertables with automatic partitioning) |
| **Relational store** | PostgreSQL (same instance) for API registrations, configurations, user roles, audit logs |
| **Retention policy** | TimescaleDB continuous aggregates for rollup: raw data (30 days) → hourly aggregates (13 months) → daily aggregates (7 years for audit) |
| **Data model** | `api_metrics` hypertable: `(api_id, timestamp, metric_type, value)` with composite index on `(api_id, metric_type, timestamp DESC)` |
| **Migration** | Flyway for schema versioning — all migrations tracked in version control |
| **Caching** | Redis for: current health status per API (60-second TTL), dashboard session data, alert state deduplication |

**ADR-001: TimescaleDB over dedicated TSDB (InfluxDB, Prometheus long-term)**
- TimescaleDB is a PostgreSQL extension — stays within the firm's approved PostgreSQL infrastructure
- Avoids introducing a new database technology requiring separate TRB approval
- Hypertables and continuous aggregates handle the 10,000 points/second ingestion and 13-month retention
- Trade-off: slightly less optimized than purpose-built TSDB, but sufficient for projected scale

**ADR-002: Retention tiering via continuous aggregates**
- Raw data (1-second granularity) retained for 30 days — sufficient for incident investigation
- Hourly aggregates retained for 13 months — supports SLA reporting and trend analysis
- Daily aggregates retained for 7 years — meets audit retention requirements
- Automatic materialization via TimescaleDB policies — no application-level ETL

### Authentication & Security

| Decision | Detail |
|----------|--------|
| **Authentication** | SAML 2.0 via firm SSO (Active Directory) — Spring Security SAML integration |
| **Authorization** | RBAC with three roles: Viewer, Operator, Admin. Roles mapped to AD groups |
| **Session management** | JWT tokens with 30-minute idle timeout (firm policy). Tokens stored in HTTP-only secure cookies |
| **MFA** | Required for Admin role actions — delegated to firm SSO MFA infrastructure |
| **API security** | All internal APIs require valid JWT. No API key-based access for human users |
| **Audit logging** | Immutable append-only audit table with: timestamp, actor (from JWT), action, resource, old_value, new_value |

### API & Communication Patterns

| Decision | Detail |
|----------|--------|
| **REST API** | Spring Boot REST controllers for CRUD operations (registration, configuration, user management) |
| **WebSocket** | STOMP over WebSocket for real-time metric streaming to dashboard (Spring WebSocket) |
| **Telemetry ingestion** | Adapter pattern — one adapter per telemetry source type. Adapters run as scheduled polling jobs (configurable interval, default 30 seconds) |
| **Alert delivery** | Event-driven: metric breach detected → alert event published to internal message queue → notification workers deliver to email (SMTP) and Slack (webhook) |
| **Internal messaging** | Spring ApplicationEvents for in-process eventing. If scale demands, migrate to Kafka (firm-approved message broker) |

**ADR-003: Polling adapters over push-based telemetry**
- Existing APM tools (AppDynamics, Dynatrace) expose REST APIs for metric retrieval
- Polling is simpler to implement, debug, and operate than configuring push webhooks in each APM tool
- 30-second poll interval meets the <60-second freshness requirement (FR-7)
- Trade-off: slightly higher latency than push; acceptable for monitoring use case

### Frontend Architecture

| Decision | Detail |
|----------|--------|
| **Framework** | React 18+ with TypeScript |
| **State management** | React Query (TanStack Query) for server state, React Context for UI state |
| **Charting** | Recharts for time-series visualization (lightweight, React-native, sufficient for dashboards) |
| **Routing** | React Router v6 with route-based code splitting |
| **Styling** | Firm UI component library (if available) or Material UI as fallback |
| **Real-time updates** | WebSocket subscription via custom React hook; automatic reconnect with exponential backoff |
| **Export** | Client-side PDF generation (jsPDF) for portfolio summary export; server-side CSV generation for data exports |

### Infrastructure & Deployment

| Decision | Detail |
|----------|--------|
| **Container orchestration** | Firm Kubernetes cluster (on-prem or approved cloud tenancy) |
| **Container runtime** | Docker containers built via multi-stage Dockerfile |
| **CI/CD** | Jenkins (firm-standard) or GitHub Actions (if firm-approved) with blue-green deployment |
| **Database hosting** | Firm-managed PostgreSQL with TimescaleDB extension enabled |
| **Cache** | Firm-managed Redis cluster |
| **Load balancing** | Firm-standard ingress controller (NGINX or F5) |
| **Monitoring** | The dashboard monitors itself — register its own APIs in the dashboard. Plus firm-standard APM for application-level monitoring |
| **Logging** | Structured JSON logging → firm Splunk infrastructure |
| **Secrets** | HashiCorp Vault (firm-standard) for database credentials, API keys, Slack webhook URLs |

**ADR-004: Blue-green deployment for zero-downtime updates**
- Dashboard is a Tier-2 service with 99.9% availability target
- Blue-green deployment ensures zero-downtime during releases
- Database migrations must be backward-compatible (expand-and-contract pattern)

---

## Implementation Patterns & Consistency Rules

### Naming Patterns

| Context | Convention | Example |
|---------|-----------|---------|
| Database tables | `snake_case`, plural | `api_registrations`, `api_metrics`, `audit_logs` |
| Database columns | `snake_case` | `api_id`, `created_at`, `metric_type` |
| REST endpoints | `kebab-case`, plural nouns | `/api/v1/api-registrations`, `/api/v1/health-metrics` |
| Kotlin classes | `PascalCase` | `ApiRegistrationService`, `MetricIngestionAdapter` |
| Kotlin functions | `camelCase` | `findByApiId()`, `calculateSlaCompliance()` |
| React components | `PascalCase` | `HealthDashboard`, `ApiRegistrationForm` |
| React hooks | `camelCase` with `use` prefix | `useHealthMetrics()`, `useWebSocket()` |
| Environment variables | `SCREAMING_SNAKE_CASE` | `DATABASE_URL`, `REDIS_HOST`, `SLACK_WEBHOOK_URL` |

### Structure Patterns

**All AI agents MUST follow this project organization:**

```
src/
  main/
    kotlin/com/firm/apihealthdashboard/
      config/           # Spring configuration classes
      controller/       # REST controllers (thin — delegate to services)
      service/          # Business logic
      adapter/          # Telemetry source adapters (one per source type)
      model/            # Domain entities and DTOs
      repository/       # Database access (Spring Data JPA)
      event/            # Internal event definitions and handlers
      security/         # SAML config, RBAC filters, audit interceptors
    resources/
      db/migration/     # Flyway migration scripts (V001__, V002__, ...)
      application.yaml  # Spring configuration
  test/
    kotlin/             # Mirrors main/ structure

frontend/
  src/
    components/         # Reusable UI components
    pages/              # Route-level page components
    hooks/              # Custom React hooks
    services/           # API client functions
    types/              # TypeScript type definitions
    utils/              # Utility functions
  public/
  tests/
```

### Format Patterns

**API Response Format (all endpoints):**

```json
{
  "data": { ... },
  "meta": {
    "timestamp": "2026-04-05T12:00:00Z",
    "requestId": "uuid"
  }
}
```

**API Error Format:**

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Human-readable description",
    "details": [{ "field": "apiName", "issue": "required" }]
  },
  "meta": {
    "timestamp": "2026-04-05T12:00:00Z",
    "requestId": "uuid"
  }
}
```

**Metric Data Point Format (internal):**

```json
{
  "apiId": "uuid",
  "metricType": "LATENCY_P95",
  "value": 142.5,
  "unit": "ms",
  "timestamp": "2026-04-05T12:00:30Z",
  "source": "appdynamics"
}
```

### Process Patterns

**Error Handling:**
- Controllers return appropriate HTTP status codes (400, 401, 403, 404, 500)
- Services throw domain-specific exceptions (e.g., `ApiNotFoundException`, `TelemetryConnectionException`)
- Global exception handler maps exceptions to standard error response format
- All errors logged with correlation ID for tracing

**Telemetry Adapter Pattern:**
- All adapters implement `TelemetryAdapter` interface: `connect()`, `fetchMetrics(apiId, timeRange)`, `validateConnection()`
- Each adapter is a Spring `@Component` with source-type qualifier
- Adapter factory resolves the correct adapter based on API registration's source type
- Failed adapter connections retry 3 times with exponential backoff, then mark API as "source unreachable"

**Alert Processing Pattern:**
- Metric ingestion → threshold evaluation → deduplication (Redis, 5-minute window) → event publish → notification delivery
- Alerts are deduplicated: same API + same metric + same breach within 5 minutes = one alert
- Alert state tracked: `OPEN` → `ACKNOWLEDGED` → `RESOLVED` (auto-resolves when metric returns to healthy)

### Enforcement Guidelines

**All AI development agents MUST:**
- Follow the naming conventions above with zero exceptions
- Place files in the correct directory per the structure pattern
- Use the standard API response/error formats for all endpoints
- Implement the `TelemetryAdapter` interface for any new telemetry source
- Write unit tests for all service classes and integration tests for all adapters
- Include Flyway migration scripts for any database schema changes
- Log all configuration changes to the audit table via the `AuditService`
- Never hardcode credentials — all secrets via Vault/environment variables

---

## Project Structure & Boundaries

### Complete Project Directory Structure

```
api-health-dashboard/
├── docker-compose.yaml              # Local dev: PostgreSQL + TimescaleDB + Redis
├── Dockerfile                       # Multi-stage build
├── Jenkinsfile                      # CI/CD pipeline definition
├── k8s/                             # Kubernetes manifests
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── ingress.yaml
│   └── configmap.yaml
├── backend/
│   ├── build.gradle.kts             # Kotlin/Spring Boot build config
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/firm/apihealthdashboard/
│   │   │   │   ├── ApiHealthDashboardApplication.kt
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.kt          # SAML SSO + RBAC
│   │   │   │   │   ├── WebSocketConfig.kt         # STOMP WebSocket
│   │   │   │   │   ├── RedisConfig.kt
│   │   │   │   │   └── SchedulingConfig.kt        # Adapter polling schedules
│   │   │   │   ├── controller/
│   │   │   │   │   ├── ApiRegistrationController.kt
│   │   │   │   │   ├── HealthMetricsController.kt
│   │   │   │   │   ├── PortfolioController.kt
│   │   │   │   │   ├── SlaReportController.kt
│   │   │   │   │   ├── AlertConfigController.kt
│   │   │   │   │   └── AdminController.kt
│   │   │   │   ├── service/
│   │   │   │   │   ├── ApiRegistrationService.kt
│   │   │   │   │   ├── MetricIngestionService.kt
│   │   │   │   │   ├── HealthCalculationService.kt
│   │   │   │   │   ├── SlaComplianceService.kt
│   │   │   │   │   ├── AlertEvaluationService.kt
│   │   │   │   │   ├── NotificationService.kt
│   │   │   │   │   └── AuditService.kt
│   │   │   │   ├── adapter/
│   │   │   │   │   ├── TelemetryAdapter.kt        # Interface
│   │   │   │   │   ├── AppDynamicsAdapter.kt
│   │   │   │   │   ├── DynatraceAdapter.kt
│   │   │   │   │   ├── SplunkAdapter.kt
│   │   │   │   │   ├── ApiGatewayAdapter.kt
│   │   │   │   │   └── AdapterFactory.kt
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/                    # JPA entities
│   │   │   │   │   └── dto/                       # Request/response DTOs
│   │   │   │   ├── repository/
│   │   │   │   ├── event/
│   │   │   │   └── security/
│   │   │   └── resources/
│   │   │       ├── application.yaml
│   │   │       └── db/migration/                  # Flyway migrations
│   │   └── test/
│   │       └── kotlin/                            # Mirrors main/ structure
├── frontend/
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   ├── src/
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   ├── components/
│   │   │   ├── common/                            # Shared UI components
│   │   │   ├── dashboard/                         # Health dashboard widgets
│   │   │   ├── portfolio/                         # Portfolio view components
│   │   │   ├── registration/                      # API registration forms
│   │   │   └── alerts/                            # Alert configuration UI
│   │   ├── pages/
│   │   │   ├── DashboardPage.tsx
│   │   │   ├── PortfolioPage.tsx
│   │   │   ├── ApiDetailPage.tsx
│   │   │   ├── RegistrationPage.tsx
│   │   │   ├── AlertsPage.tsx
│   │   │   ├── SlaReportsPage.tsx
│   │   │   └── AdminPage.tsx
│   │   ├── hooks/
│   │   │   ├── useHealthMetrics.ts
│   │   │   ├── useWebSocket.ts
│   │   │   └── useAuth.ts
│   │   ├── services/
│   │   │   └── api.ts                             # API client (Axios)
│   │   ├── types/
│   │   │   └── index.ts
│   │   └── utils/
│   └── tests/
└── docs/                                          # Project documentation
```

### Architectural Boundaries

| Boundary | Rule |
|----------|------|
| **Controller ↔ Service** | Controllers are thin — validate input, delegate to service, return response. No business logic in controllers |
| **Service ↔ Repository** | Services contain all business logic. Repositories contain only data access queries |
| **Service ↔ Adapter** | Services call adapters via the `TelemetryAdapter` interface. Services never reference specific adapter implementations |
| **Backend ↔ Frontend** | Communication only via REST API and WebSocket. No shared code. Frontend treats backend as a black box |
| **Adapter ↔ External** | Each adapter encapsulates all logic for one telemetry source. External API specifics never leak beyond the adapter |

### Requirements to Structure Mapping

| Capability Area | Backend Components | Frontend Components |
|----------------|-------------------|-------------------|
| API Registration (FR-1 to FR-5) | `controller/ApiRegistrationController`, `service/ApiRegistrationService`, `adapter/*` | `pages/RegistrationPage`, `components/registration/` |
| Health Dashboard (FR-6 to FR-10) | `service/MetricIngestionService`, `service/HealthCalculationService`, WebSocket | `pages/DashboardPage`, `pages/ApiDetailPage`, `components/dashboard/`, `hooks/useHealthMetrics` |
| Portfolio View (FR-11 to FR-14) | `controller/PortfolioController`, `service/SlaComplianceService` | `pages/PortfolioPage`, `components/portfolio/` |
| SLA Reporting (FR-15 to FR-18) | `controller/SlaReportController`, `service/SlaComplianceService` | `pages/SlaReportsPage` |
| Alerting (FR-19 to FR-22) | `service/AlertEvaluationService`, `service/NotificationService`, `event/` | `pages/AlertsPage`, `components/alerts/` |
| Access Control & Audit (FR-23 to FR-26) | `config/SecurityConfig`, `security/`, `service/AuditService` | `pages/AdminPage`, `hooks/useAuth` |

---

## Architecture Validation Results

### Coherence Validation

| Check | Status | Notes |
|-------|--------|-------|
| Decision compatibility | PASS | All technology choices are within firm-approved catalog |
| Pattern consistency | PASS | Naming, structure, and format patterns are consistent across backend and frontend |
| Structure alignment | PASS | Project structure maps cleanly to capability areas |
| Cross-cutting concern coverage | PASS | Audit logging, encryption, RBAC, and data classification addressed in architecture |

### Requirements Coverage Validation

| Category | Total | Covered | Gap |
|----------|-------|---------|-----|
| Functional Requirements (FR) | 26 | 26 | 0 |
| Non-Functional Requirements (NFR) | 18 | 18 | 0 |
| Domain Requirements (DR) | 4 | 4 | 0 |

### Implementation Readiness Validation

| Check | Status |
|-------|--------|
| Can a developer agent implement FR-1 through FR-5 from this document? | YES — adapter pattern, registration model, and controller structure defined |
| Can a developer agent implement FR-6 through FR-10 from this document? | YES — ingestion service, WebSocket config, and dashboard component structure defined |
| Can a developer agent set up CI/CD from this document? | YES — Jenkinsfile location, Docker config, K8s manifests, and blue-green strategy defined |
| Are all technology choices specific enough to code against? | YES — specific libraries (Spring Boot, React Query, Recharts, TimescaleDB) with versions implied by current LTS |

### Architecture Completeness Checklist

- [x] Data architecture defined with schema, retention, and migration strategy
- [x] Authentication and authorization architecture defined with firm SSO integration
- [x] API design patterns defined with standard request/response formats
- [x] Frontend architecture defined with state management and real-time patterns
- [x] Infrastructure defined with containerization, CI/CD, and deployment strategy
- [x] All naming conventions defined for database, API, backend, and frontend
- [x] Complete project directory structure defined
- [x] All FRs mapped to specific components
- [x] All NFRs addressed by architecture decisions
- [x] All domain requirements (compliance, audit, security) covered
- [x] Enforcement guidelines documented for AI development agents

### Architecture Readiness Assessment

| Attribute | Value |
|-----------|-------|
| **Status** | READY FOR IMPLEMENTATION |
| **Confidence** | High |
| **Strengths** | Complete traceability from requirements to components. Clear adapter pattern for extensibility. Firm-standard technology choices reduce TRB risk |
| **Future enhancements** | Kafka migration path documented (ADR: internal messaging). ML anomaly detection architecture deferred to V2 |

### Implementation Handoff — AI Agent Guidelines

1. **Start with infrastructure** — Docker Compose for local dev, Flyway migrations for schema
2. **Then core domain** — API registration, telemetry adapter interface, one adapter (AppDynamics)
3. **Then real-time** — WebSocket streaming, health calculation, dashboard UI
4. **Then supporting** — alerting, SLA reporting, portfolio view, admin/audit
5. **Always** — follow naming conventions, project structure, and enforcement guidelines exactly
