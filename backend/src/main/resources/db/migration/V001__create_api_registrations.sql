CREATE TABLE api_registrations (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    api_name        VARCHAR(255) NOT NULL,
    owning_team     VARCHAR(255) NOT NULL,
    criticality_tier INTEGER NOT NULL CHECK (criticality_tier BETWEEN 1 AND 3),
    description     TEXT,
    telemetry_source_type VARCHAR(50),
    telemetry_config JSONB,
    sla_availability_target DECIMAL(5,2),
    sla_latency_p50_ms INTEGER,
    sla_latency_p95_ms INTEGER,
    sla_latency_p99_ms INTEGER,
    sla_error_rate_ceiling DECIMAL(5,2),
    status          VARCHAR(20) NOT NULL DEFAULT 'pending_configuration',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    decommissioned_at TIMESTAMPTZ
);

CREATE INDEX idx_api_registrations_team ON api_registrations(owning_team);
CREATE INDEX idx_api_registrations_status ON api_registrations(status);
CREATE INDEX idx_api_registrations_tier ON api_registrations(criticality_tier);
