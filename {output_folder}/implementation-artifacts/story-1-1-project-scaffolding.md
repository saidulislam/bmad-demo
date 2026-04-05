# Story 1.1: Project Scaffolding & Local Dev Environment

Status: review

## Story
As a developer,
I want a fully initialized project with backend (Spring Boot/Kotlin), frontend (React/TypeScript), and local database (TimescaleDB via Docker Compose),
so that I can begin feature development with a working local environment.

## Acceptance Criteria
1. Running `docker-compose up` starts TimescaleDB and Redis containers accessible on standard ports
2. Spring Boot backend starts on port 8080 with health endpoint returning 200
3. React frontend starts on port 3000 and displays a placeholder page
4. Flyway migration creates the initial schema (api_registrations, api_metrics hypertable, audit_logs, users_roles tables)
5. Project structure follows the architecture document directory layout exactly
6. All naming conventions match architecture patterns (snake_case DB, kebab-case REST, PascalCase components)

## Tasks / Subtasks
- [x] Task 1: Create project root structure with Docker Compose (AC: #1)
  - [x] Create docker-compose.yaml with TimescaleDB and Redis services
  - [x] Create .env.example with default connection variables
- [x] Task 2: Initialize Spring Boot/Kotlin backend (AC: #2, #5, #6)
  - [x] Create build.gradle.kts with Spring Boot, Spring Data JPA, Flyway, Redis, WebSocket dependencies
  - [x] Create application.yaml with database, Redis, and server configuration
  - [x] Create main application class and health endpoint
  - [x] Create directory structure per architecture (controller/, service/, adapter/, model/, repository/, event/, security/)
- [x] Task 3: Create Flyway database migrations (AC: #4)
  - [x] V001__create_api_registrations.sql
  - [x] V002__create_api_metrics_hypertable.sql
  - [x] V003__create_audit_logs.sql
  - [x] V004__create_users_roles.sql
- [x] Task 4: Initialize React/TypeScript frontend (AC: #3, #5, #6)
  - [x] Create package.json with React 18, TypeScript, Vite, React Router, React Query, Recharts, Axios
  - [x] Create tsconfig.json and vite.config.ts
  - [x] Create directory structure per architecture (components/, pages/, hooks/, services/, types/, utils/)
  - [x] Create App.tsx with React Router and placeholder DashboardPage
- [x] Task 5: Create Kubernetes and CI manifests (AC: #5)
  - [x] Create Dockerfile (multi-stage build)
  - [x] Create k8s/ directory with deployment.yaml, service.yaml, ingress.yaml, configmap.yaml
  - [x] Create Jenkinsfile skeleton

## Dev Notes
- Architecture: Spring Boot (Kotlin) + React + TimescaleDB + Redis
- TimescaleDB is a PostgreSQL extension — use postgres:15 image with timescaledb extension
- Redis for health status cache (60s TTL) and alert deduplication
- Flyway migrations in src/main/resources/db/migration/
- api_metrics must be a TimescaleDB hypertable with composite index on (api_id, metric_type, timestamp DESC)
- All audit records use write-ahead logging — schema must support immutable append-only pattern

### Project Structure Notes
- Backend: backend/src/main/kotlin/com/firm/apihealthdashboard/
- Frontend: frontend/src/
- Database migrations: backend/src/main/resources/db/migration/
- K8s manifests: k8s/

### References
- Architecture document: {output_folder}/planning-artifacts/architecture.md — Project Structure, Data Architecture, Infrastructure sections
- PRD: NFR11 (TLS 1.2+), NFR12 (AES-256 at rest), NFR16 (13-month retention)

## Dev Agent Record

### Agent Model Used
Claude Opus 4.6

### Debug Log References

### Completion Notes List

### Completion Notes List
- Task 1: Docker Compose with TimescaleDB (pg15 + timescaledb extension) and Redis 7 Alpine, health checks configured
- Task 2: Spring Boot 3.3 + Kotlin 1.9 + JDK 21, structured JSON logging, security config permits health endpoints
- Task 3: 4 Flyway migrations — api_registrations, api_metrics hypertable with continuous aggregates and retention policy, immutable audit_logs with trigger protection, user_roles with AD group mappings
- Task 4: React 18 + TypeScript + Vite + React Router v6 + React Query + Recharts + Axios, 6 placeholder pages, API client, TypeScript types matching architecture response formats
- Task 5: Multi-stage Dockerfile, K8s deployment (2 replicas, rolling update, health probes), Jenkinsfile with test/build/deploy stages

### File List
docker-compose.yaml
.env.example
Dockerfile
Jenkinsfile
backend/build.gradle.kts
backend/src/main/resources/application.yaml
backend/src/main/resources/db/migration/V001__create_api_registrations.sql
backend/src/main/resources/db/migration/V002__create_api_metrics_hypertable.sql
backend/src/main/resources/db/migration/V003__create_audit_logs.sql
backend/src/main/resources/db/migration/V004__create_users_roles.sql
backend/src/main/kotlin/com/firm/apihealthdashboard/ApiHealthDashboardApplication.kt
backend/src/main/kotlin/com/firm/apihealthdashboard/controller/HealthController.kt
backend/src/main/kotlin/com/firm/apihealthdashboard/config/SecurityConfig.kt
frontend/package.json
frontend/tsconfig.json
frontend/vite.config.ts
frontend/index.html
frontend/src/main.tsx
frontend/src/App.tsx
frontend/src/pages/DashboardPage.tsx
frontend/src/pages/PortfolioPage.tsx
frontend/src/pages/RegistrationPage.tsx
frontend/src/pages/AlertsPage.tsx
frontend/src/pages/SlaReportsPage.tsx
frontend/src/pages/AdminPage.tsx
frontend/src/services/api.ts
frontend/src/types/index.ts
k8s/deployment.yaml
k8s/service.yaml
k8s/ingress.yaml
k8s/configmap.yaml
