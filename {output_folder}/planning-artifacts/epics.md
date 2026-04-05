---
stepsCompleted: [1, 2, 3, 4]
inputDocuments: [prd.md, architecture.md]
---

# API Health Dashboard - Epic Breakdown

## Overview

This document provides the complete epic and story breakdown for the API Health Dashboard, decomposing the requirements from the PRD and Architecture into implementable stories. Each story is sized for a single dev agent, uses BDD acceptance criteria, and traces back to functional requirements.

## Requirements Inventory

### Functional Requirements

FR1: Users can register an API by providing name, owning team, criticality tier (1/2/3), description, and telemetry source configuration
FR2: System validates telemetry source connectivity and displays sample metrics within 60 seconds of configuration
FR3: Users can define SLA targets per API: availability percentage, latency threshold (P50/P95/P99), and error rate ceiling
FR4: Users can configure alert thresholds per metric with notification delivery to email and Slack
FR5: Users can edit or decommission API registrations; changes logged to audit trail
FR6: Dashboard displays real-time metrics per API: uptime percentage, latency (P50/P95/P99), error rate, and throughput (requests/sec)
FR7: Metric data refreshes within 60 seconds of source availability
FR8: Dashboard provides historical trend views for 30, 60, and 90 day windows with daily granularity
FR9: Users can filter and search APIs by name, team, domain, criticality tier, and health status
FR10: Dashboard deep-links from alert notifications to the specific API's health view with breach context
FR11: Portfolio view displays aggregate health score across all registered APIs
FR12: Portfolio supports drill-down by team, domain, and criticality tier
FR13: Portfolio view displays SLA compliance trend (90-day) per team and per domain
FR14: Users can export portfolio summary as PDF or CSV
FR15: System generates monthly SLA compliance reports per API: target vs. actual availability, latency compliance, breach count
FR16: Reports delivered automatically via email to configured team distribution lists
FR17: Reports archived and retrievable in the dashboard for 13 months
FR18: Reports include: reporting period, API name, owning team, SLA target, actual metric, compliance status (pass/fail), and breach incidents with timestamps
FR19: System sends alert notifications within 2 minutes of threshold breach detection
FR20: Alerts delivered to email and Slack channels as configured per API
FR21: Alert notifications include: API name, breached metric, current value, threshold, breach start time, and deep-link to dashboard
FR22: Users can configure alert suppression windows for planned maintenance
FR23: Authentication via firm SSO (SAML 2.0) with Active Directory group mapping
FR24: Three RBAC roles enforced: Viewer (read-only), Operator (configure alerts/SLAs), Admin (manage users, register APIs)
FR25: All configuration changes logged with timestamp, actor identity, and change detail
FR26: Audit log queryable by compliance and internal audit teams with date range, actor, and action type filters

### Non-Functional Requirements

NFR1: Dashboard page load time < 2 seconds for 95th percentile under normal load (up to 500 concurrent users)
NFR2: API metric ingestion pipeline processes 10,000 metric data points per second
NFR3: Search and filter operations return results within 500ms for 95th percentile
NFR4: System availability 99.9% during business hours (6am-8pm ET, Monday-Friday)
NFR5: Recovery Time Objective (RTO): 1 hour
NFR6: Recovery Point Objective (RPO): 1 hour for metric data
NFR7: Zero data loss for audit trail records
NFR8: Support 500 concurrent dashboard users without degradation
NFR9: Support 2,000 registered APIs with 13 months of retained metric data
NFR10: Horizontal scaling of metric ingestion pipeline without downtime
NFR11: All data in transit encrypted via TLS 1.2+
NFR12: All data at rest encrypted via AES-256
NFR13: Session timeout after 30 minutes of inactivity
NFR14: MFA required for Admin role actions
NFR15: No PII or Restricted data ingested or displayed by the system
NFR16: Metric data retained for minimum 13 months
NFR17: Audit trail records retained for minimum 7 years
NFR18: Automated data archival for records beyond active retention window

### Additional Requirements (from Architecture)

- Project initialization from Spring Boot (Kotlin) + React starter with TimescaleDB
- Flyway migration framework for schema versioning
- TelemetryAdapter interface pattern for all source integrations
- Redis caching layer for health status and alert deduplication
- WebSocket (STOMP) for real-time metric streaming
- Docker + Kubernetes deployment manifests
- Structured JSON logging to Splunk
- HashiCorp Vault integration for secrets management

### FR Coverage Map

FR1: Epic 2 - API Registration & Onboarding
FR2: Epic 2 - API Registration & Onboarding
FR3: Epic 2 - API Registration & Onboarding
FR4: Epic 4 - Alerting & Notifications
FR5: Epic 2 - API Registration & Onboarding
FR6: Epic 3 - Real-Time Health Dashboard
FR7: Epic 3 - Real-Time Health Dashboard
FR8: Epic 3 - Real-Time Health Dashboard
FR9: Epic 3 - Real-Time Health Dashboard
FR10: Epic 4 - Alerting & Notifications
FR11: Epic 5 - Portfolio View & Leadership Insights
FR12: Epic 5 - Portfolio View & Leadership Insights
FR13: Epic 5 - Portfolio View & Leadership Insights
FR14: Epic 5 - Portfolio View & Leadership Insights
FR15: Epic 6 - SLA Reporting & Compliance
FR16: Epic 6 - SLA Reporting & Compliance
FR17: Epic 6 - SLA Reporting & Compliance
FR18: Epic 6 - SLA Reporting & Compliance
FR19: Epic 4 - Alerting & Notifications
FR20: Epic 4 - Alerting & Notifications
FR21: Epic 4 - Alerting & Notifications
FR22: Epic 4 - Alerting & Notifications
FR23: Epic 1 - Foundation & Authentication
FR24: Epic 1 - Foundation & Authentication
FR25: Epic 1 - Foundation & Authentication
FR26: Epic 7 - Audit & Administration
FR-Arch: Epic 1 - Foundation & Authentication (project setup, database, infrastructure)

## Epic List

### Epic 1: Foundation & Authentication
Engineers can log in via firm SSO, see a secured application shell, and all actions are role-controlled — establishing the platform foundation.
**FRs covered:** FR23, FR24, FR25, Architecture requirements (project setup, DB, infra)

### Epic 2: API Registration & Onboarding
Engineering teams can register their APIs, connect telemetry sources, define SLA targets, and manage registrations — achieving the <30-minute onboarding goal.
**FRs covered:** FR1, FR2, FR3, FR5

### Epic 3: Real-Time Health Dashboard
Engineers can view live health metrics for any registered API, explore historical trends, and search/filter across all APIs.
**FRs covered:** FR6, FR7, FR8, FR9

### Epic 4: Alerting & Notifications
Engineers receive proactive alerts when API metrics breach thresholds, with deep-links to the dashboard for immediate investigation.
**FRs covered:** FR4, FR10, FR19, FR20, FR21, FR22

### Epic 5: Portfolio View & Leadership Insights
Engineering leadership can see aggregate API health across the organization, drill down by team/domain, and export summaries.
**FRs covered:** FR11, FR12, FR13, FR14

### Epic 6: SLA Reporting & Compliance
Monthly SLA compliance reports are generated automatically and delivered to teams — zero manual effort.
**FRs covered:** FR15, FR16, FR17, FR18

### Epic 7: Audit & Administration
Compliance and audit teams can query the full audit trail, and administrators can manage access via AD group mappings.
**FRs covered:** FR26

---

## Epic 1: Foundation & Authentication

Engineers can log in via firm SSO, see a secured application shell with role-based access, and all configuration changes are audit-logged.

### Story 1.1: Project Scaffolding & Local Dev Environment

As a developer,
I want a fully initialized project with backend (Spring Boot/Kotlin), frontend (React/TypeScript), and local database (TimescaleDB via Docker Compose),
So that I can begin feature development with a working local environment.

**Acceptance Criteria:**

**Given** the project repository is cloned
**When** a developer runs `docker-compose up` and the application start command
**Then** the Spring Boot backend starts on port 8080 and the React frontend starts on port 3000
**And** TimescaleDB and Redis containers are running and accessible
**And** the Flyway migration creates the initial schema (api_registrations, api_metrics hypertable, audit_logs, users/roles tables)
**And** the project structure follows the architecture document's directory layout exactly

### Story 1.2: SSO Authentication with SAML 2.0

As an engineer,
I want to log in using my firm SSO credentials,
So that I don't need a separate account for this application.

**Acceptance Criteria:**

**Given** an unauthenticated user navigates to the application
**When** they are redirected to the firm SSO login page and authenticate successfully
**Then** they are redirected back to the dashboard with an active session
**And** a JWT token is issued and stored in an HTTP-only secure cookie
**And** the session expires after 30 minutes of inactivity (NFR13)
**And** unauthenticated API requests return HTTP 401

### Story 1.3: Role-Based Access Control (RBAC)

As an administrator,
I want users to have role-based permissions mapped from Active Directory groups,
So that access is controlled without manual user management.

**Acceptance Criteria:**

**Given** a user authenticates via SSO with AD group membership
**When** their AD groups are evaluated against the RBAC mapping
**Then** they are assigned the correct role: Viewer, Operator, or Admin (FR24)
**And** Viewer users can only access read-only endpoints
**And** Operator users can configure alerts and SLA targets but cannot register APIs or manage users
**And** Admin users have full access including API registration and user management
**And** MFA is required for Admin role actions (NFR14)

### Story 1.4: Audit Trail Logging

As a compliance officer,
I want all configuration changes to be logged with actor identity and timestamps,
So that we can demonstrate regulatory compliance.

**Acceptance Criteria:**

**Given** any user performs a configuration change (API registration, SLA edit, alert config, role assignment)
**When** the change is persisted
**Then** an immutable audit record is created with: timestamp, actor identity (from JWT), action type, resource affected, old value, new value (FR25)
**And** audit records are written using write-ahead logging with synchronous replication (NFR7)
**And** audit records cannot be modified or deleted via any application endpoint
**And** audit data is retained for minimum 7 years (NFR17)

---

## Epic 2: API Registration & Onboarding

Engineering teams can register their APIs, connect to existing telemetry sources, validate connectivity, and define SLA targets — all within 30 minutes.

### Story 2.1: API Registration Form

As an SRE,
I want to register a new API by providing its details,
So that it appears in the monitoring dashboard.

**Acceptance Criteria:**

**Given** an authenticated Operator or Admin user navigates to "Register API"
**When** they submit the form with: API name, owning team, criticality tier (1/2/3), and description
**Then** the API registration is saved to the database
**And** the registration is logged to the audit trail (FR5)
**And** the new API appears in the API list with status "Pending Configuration"

### Story 2.2: Telemetry Source Configuration & Validation

As an SRE,
I want to connect my API to an existing telemetry source and verify the connection works,
So that I can confirm metrics will flow before leaving the setup.

**Acceptance Criteria:**

**Given** an API is registered and in "Pending Configuration" status
**When** the user selects a telemetry source type (AppDynamics, Dynatrace, Splunk, or API Gateway) and provides connection configuration
**Then** the system validates connectivity by calling the TelemetryAdapter.validateConnection() method
**And** sample metrics are displayed within 60 seconds of successful validation (FR2)
**And** if validation fails, a clear error message is shown with troubleshooting guidance
**And** the API status changes to "Active" upon successful validation
**And** the telemetry source configuration is logged to the audit trail

### Story 2.3: SLA Target Definition

As an SRE,
I want to define SLA targets for my registered API,
So that the system can track compliance and alert on breaches.

**Acceptance Criteria:**

**Given** an API is registered and in "Active" status
**When** the user sets SLA targets: availability percentage, latency thresholds (P50/P95/P99), and error rate ceiling (FR3)
**Then** the targets are saved and associated with the API
**And** the SLA configuration change is logged to the audit trail
**And** the targets are immediately used for compliance calculations

### Story 2.4: Edit and Decommission API Registrations

As an SRE,
I want to update or decommission an API registration,
So that the dashboard stays current as services evolve.

**Acceptance Criteria:**

**Given** an authenticated Operator or Admin views an existing API registration
**When** they edit API details (name, team, tier, description, telemetry config) and save
**Then** the changes are persisted and the old values are captured in the audit trail (FR5)
**And** when they decommission an API, it is soft-deleted (hidden from dashboard but retained for historical reporting)
**And** decommissioned APIs no longer trigger alerts or appear in active health views

---

## Epic 3: Real-Time Health Dashboard

Engineers can view live health metrics for any registered API, explore historical trends, and search/filter across all APIs for rapid incident investigation.

### Story 3.1: Telemetry Ingestion Pipeline

As a system,
I want to poll registered telemetry sources on a 30-second interval and store metric data,
So that the dashboard has fresh data to display.

**Acceptance Criteria:**

**Given** one or more APIs are registered with active telemetry sources
**When** the polling scheduler triggers (every 30 seconds)
**Then** each API's TelemetryAdapter fetches current metrics (latency P50/P95/P99, error rate, throughput, uptime)
**And** metrics are stored in the TimescaleDB api_metrics hypertable
**And** metric data refreshes within 60 seconds of source availability (FR7)
**And** failed adapter connections retry 3 times with exponential backoff, then mark API as "source unreachable"
**And** current health status is cached in Redis with 60-second TTL

### Story 3.2: Real-Time Dashboard View

As an engineer,
I want to see live health metrics for any API on a dashboard,
So that I can monitor service health at a glance.

**Acceptance Criteria:**

**Given** an authenticated user navigates to an API's detail page
**When** the page loads
**Then** current metrics are displayed: uptime percentage, latency (P50/P95/P99), error rate, and throughput (FR6)
**And** metrics update in real-time via WebSocket (STOMP) without page refresh
**And** the page loads in under 2 seconds for 95th percentile (NFR1)
**And** health status is color-coded: green (healthy), yellow (degraded), red (critical)

### Story 3.3: Historical Trend Views

As an engineer,
I want to view metric trends over 30, 60, and 90 day windows,
So that I can identify patterns and chronic issues.

**Acceptance Criteria:**

**Given** an authenticated user is on an API's detail page
**When** they select a time window (30, 60, or 90 days)
**Then** trend charts display latency, error rate, throughput, and uptime with daily granularity (FR8)
**And** charts are rendered using Recharts with interactive tooltips
**And** data is sourced from TimescaleDB continuous aggregates (hourly rollups)

### Story 3.4: Search, Filter, and API List

As an engineer,
I want to search and filter APIs by name, team, domain, criticality, and health status,
So that I can quickly find the service I'm investigating.

**Acceptance Criteria:**

**Given** an authenticated user is on the main dashboard page
**When** they type in the search bar or apply filters
**Then** the API list updates to show matching results within 500ms (NFR3, FR9)
**And** filters are combinable (e.g., team="Payments" AND status="degraded" AND tier=1)
**And** results show: API name, owning team, tier, current health status, and key metrics summary

---

## Epic 4: Alerting & Notifications

Engineers receive proactive alerts when API metrics breach thresholds, with deep-links to the dashboard for immediate investigation.

### Story 4.1: Alert Threshold Configuration

As an SRE,
I want to configure alert thresholds per metric for my API,
So that I'm notified when health degrades.

**Acceptance Criteria:**

**Given** an authenticated Operator or Admin views an API's settings
**When** they set thresholds for latency, error rate, and uptime with notification channels (email and Slack) (FR4)
**Then** the thresholds are saved and active immediately
**And** the configuration change is logged to the audit trail

### Story 4.2: Alert Evaluation and Deduplication

As a system,
I want to evaluate incoming metrics against thresholds and deduplicate alerts,
So that engineers receive timely but not excessive notifications.

**Acceptance Criteria:**

**Given** a metric data point is ingested for an API with configured thresholds
**When** the metric value breaches a threshold
**Then** an alert event is created within 2 minutes of detection (FR19)
**And** alerts are deduplicated: same API + same metric + same breach within 5 minutes = one alert (Redis-based)
**And** alert state is tracked: OPEN → ACKNOWLEDGED → RESOLVED
**And** alerts auto-resolve when the metric returns to healthy range

### Story 4.3: Alert Notification Delivery

As an on-call engineer,
I want to receive alert notifications via email and Slack with actionable context,
So that I can investigate immediately.

**Acceptance Criteria:**

**Given** an alert event is created
**When** the notification worker processes it
**Then** notifications are delivered to configured email and Slack channels (FR20)
**And** notifications include: API name, breached metric, current value, threshold, breach start time, and deep-link to the API's dashboard view (FR21)
**And** the deep-link navigates directly to the affected API with breach context highlighted (FR10)

### Story 4.4: Alert Suppression for Planned Maintenance

As an SRE,
I want to suppress alerts during planned maintenance windows,
So that expected downtime doesn't trigger false alarms.

**Acceptance Criteria:**

**Given** an authenticated Operator or Admin views an API's alert settings
**When** they configure a suppression window with start and end time (FR22)
**Then** no alerts are triggered for that API during the suppression window
**And** the suppression is logged to the audit trail
**And** suppression automatically expires at the configured end time

---

## Epic 5: Portfolio View & Leadership Insights

Engineering leadership can see aggregate API health across the organization, drill down by team and domain, view SLA compliance trends, and export summaries.

### Story 5.1: Aggregate Portfolio Health View

As an engineering leader,
I want to see a single view of aggregate API health across all registered APIs,
So that I can assess overall operational resilience.

**Acceptance Criteria:**

**Given** an authenticated user navigates to the Portfolio page
**When** the page loads
**Then** an aggregate health score is displayed based on all registered APIs (FR11)
**And** summary statistics show: total APIs, healthy count, degraded count, critical count
**And** the page loads in under 2 seconds (NFR1)

### Story 5.2: Drill-Down by Team, Domain, and Tier

As an engineering leader,
I want to drill down into portfolio health by team, domain, or criticality tier,
So that I can identify which areas need attention.

**Acceptance Criteria:**

**Given** a user is on the Portfolio page
**When** they filter by team, domain, or criticality tier (FR12)
**Then** the health summary and API list update to show only the selected segment
**And** filters are combinable and results update within 500ms

### Story 5.3: SLA Compliance Trend (90-Day)

As an engineering leader,
I want to view 90-day SLA compliance trends per team and domain,
So that I can identify chronic reliability issues.

**Acceptance Criteria:**

**Given** a user is on the Portfolio page with a team or domain filter applied
**When** they view the SLA compliance trend section
**Then** a 90-day trend chart shows compliance percentage over time (FR13)
**And** individual APIs with SLA breaches are listed with breach details

### Story 5.4: Export Portfolio Summary

As an engineering leader,
I want to export the portfolio summary as PDF or CSV,
So that I can share it in leadership meetings.

**Acceptance Criteria:**

**Given** a user is viewing the Portfolio page (with or without filters)
**When** they click "Export as PDF" or "Export as CSV" (FR14)
**Then** a file is generated containing: current health scores, API list with statuses, SLA compliance data
**And** the PDF uses a professional layout suitable for presentation
**And** the CSV includes all data columns for further analysis

---

## Epic 6: SLA Reporting & Compliance

Monthly SLA compliance reports are generated automatically and delivered to teams with zero manual effort, meeting audit retention requirements.

### Story 6.1: SLA Compliance Calculation Engine

As a system,
I want to calculate SLA compliance per API at end of each reporting period,
So that reports reflect accurate compliance data.

**Acceptance Criteria:**

**Given** a reporting period (calendar month) has ended
**When** the SLA calculation job runs
**Then** it computes per-API: actual availability, latency compliance (P50/P95/P99 vs target), error rate compliance, and breach incident count (FR15)
**And** calculations use TimescaleDB continuous aggregates for the reporting window
**And** results are stored for report generation and historical queries

### Story 6.2: Monthly Report Generation and Delivery

As an SRE,
I want to receive automated monthly SLA reports for my team's APIs,
So that I never have to manually compile compliance data.

**Acceptance Criteria:**

**Given** SLA compliance data is calculated for the month
**When** the report generation job runs
**Then** a per-team report is generated containing: reporting period, API name, owning team, SLA target, actual metric, compliance status (pass/fail), and breach incidents with timestamps (FR18)
**And** reports are delivered via email to configured team distribution lists (FR16)
**And** reports are archived in the dashboard and retrievable for 13 months (FR17, NFR16)

### Story 6.3: Report Retrieval and Archive

As a compliance officer,
I want to retrieve historical SLA reports from the dashboard,
So that I can provide evidence during audits.

**Acceptance Criteria:**

**Given** an authenticated user navigates to the SLA Reports page
**When** they search by team, API, or date range
**Then** archived reports are listed and downloadable
**And** reports are available for the full 13-month retention window (FR17)
**And** data beyond 13 months is automatically archived per retention policy (NFR18)

---

## Epic 7: Audit & Administration

Compliance and audit teams can query the full audit trail, and administrators can manage access controls.

### Story 7.1: Audit Log Query Interface

As a compliance officer,
I want to search the audit trail by date range, actor, and action type,
So that I can investigate configuration changes for regulatory reviews.

**Acceptance Criteria:**

**Given** an authenticated Admin or compliance user navigates to the Audit Log page
**When** they apply filters: date range, actor identity, action type (FR26)
**Then** matching audit records are displayed with: timestamp, actor, action, resource, old value, new value
**And** results are paginated and sortable
**And** search results return within 500ms (NFR3)

### Story 7.2: RBAC Administration via AD Groups

As an administrator,
I want to map Active Directory groups to application roles,
So that access management is self-service through existing AD infrastructure.

**Acceptance Criteria:**

**Given** an authenticated Admin navigates to the Administration page
**When** they configure AD group to role mappings (e.g., "API-Health-Admins" → Admin role)
**Then** users logging in with that AD group membership are automatically assigned the mapped role
**And** role mapping changes are logged to the audit trail
**And** no shared service accounts are allowed for human access (architecture security requirement)

---

## Validation Summary

### FR Coverage: 26/26 (100%)

All functional requirements are covered by at least one story with testable acceptance criteria.

### NFR Coverage

| NFR | Covered In |
|-----|-----------|
| NFR1 (page load <2s) | Stories 3.2, 5.1 |
| NFR2 (10K metrics/sec) | Story 3.1 |
| NFR3 (search <500ms) | Stories 3.4, 7.1 |
| NFR4-6 (availability, RTO, RPO) | Infrastructure in Story 1.1 + deployment config |
| NFR7 (zero audit data loss) | Story 1.4 |
| NFR8 (500 concurrent users) | NFR validated via load testing post-implementation |
| NFR9 (2000 APIs, 13mo retention) | Stories 3.1, 6.3 |
| NFR10 (horizontal scaling) | Architecture deployment (K8s horizontal pod autoscaling) |
| NFR11-12 (encryption) | Story 1.1 (infrastructure config) |
| NFR13 (session timeout) | Story 1.2 |
| NFR14 (MFA for Admin) | Story 1.3 |
| NFR15 (no PII) | Architecture constraint enforced across all stories |
| NFR16-18 (data retention) | Stories 1.4, 6.2, 6.3 |

### Epic Dependencies

```
Epic 1 (Foundation) → No dependencies — must complete first
Epic 2 (Registration) → Depends on Epic 1 (auth, audit, database)
Epic 3 (Dashboard) → Depends on Epic 2 (registered APIs with telemetry)
Epic 4 (Alerting) → Depends on Epic 2 (alert config) and Epic 3 (metric ingestion)
Epic 5 (Portfolio) → Depends on Epic 3 (health data available)
Epic 6 (SLA Reporting) → Depends on Epic 2 (SLA targets) and Epic 3 (metric data)
Epic 7 (Audit & Admin) → Depends on Epic 1 (audit trail exists)
```

### Recommended Implementation Order

1. **Epic 1** — Foundation (4 stories)
2. **Epic 2** — Registration (4 stories)
3. **Epic 3** — Dashboard (4 stories)
4. **Epic 4** — Alerting (4 stories) | **Epic 7** — Audit (2 stories) — can run in parallel
5. **Epic 5** — Portfolio (4 stories) | **Epic 6** — SLA Reporting (3 stories) — can run in parallel

**Total: 7 epics, 25 stories**
