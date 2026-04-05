CREATE TABLE api_metrics (
    api_id      UUID NOT NULL REFERENCES api_registrations(id),
    timestamp   TIMESTAMPTZ NOT NULL,
    metric_type VARCHAR(30) NOT NULL,
    value       DOUBLE PRECISION NOT NULL,
    unit        VARCHAR(20) NOT NULL,
    source      VARCHAR(50) NOT NULL
);

-- Convert to TimescaleDB hypertable
SELECT create_hypertable('api_metrics', 'timestamp');

-- Composite index for efficient queries per API and metric type
CREATE INDEX idx_api_metrics_lookup ON api_metrics(api_id, metric_type, timestamp DESC);

-- Continuous aggregates for retention tiering
-- Hourly aggregates (retained 13 months)
CREATE MATERIALIZED VIEW api_metrics_hourly
WITH (timescaledb.continuous) AS
SELECT
    api_id,
    metric_type,
    time_bucket('1 hour', timestamp) AS bucket,
    AVG(value) AS avg_value,
    MIN(value) AS min_value,
    MAX(value) AS max_value,
    COUNT(*) AS sample_count
FROM api_metrics
GROUP BY api_id, metric_type, bucket;

-- Daily aggregates (retained 7 years for audit)
CREATE MATERIALIZED VIEW api_metrics_daily
WITH (timescaledb.continuous) AS
SELECT
    api_id,
    metric_type,
    time_bucket('1 day', timestamp) AS bucket,
    AVG(value) AS avg_value,
    MIN(value) AS min_value,
    MAX(value) AS max_value,
    COUNT(*) AS sample_count
FROM api_metrics
GROUP BY api_id, metric_type, bucket;

-- Retention policy: drop raw data older than 30 days
SELECT add_retention_policy('api_metrics', INTERVAL '30 days');
