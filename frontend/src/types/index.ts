export interface ApiRegistration {
  id: string;
  apiName: string;
  owningTeam: string;
  criticalityTier: 1 | 2 | 3;
  description: string;
  telemetrySourceType: string;
  status: "pending_configuration" | "active" | "decommissioned";
  slaAvailabilityTarget?: number;
  slaLatencyP95Ms?: number;
}

export interface HealthMetric {
  apiId: string;
  metricType: "LATENCY_P50" | "LATENCY_P95" | "LATENCY_P99" | "ERROR_RATE" | "THROUGHPUT" | "UPTIME";
  value: number;
  unit: string;
  timestamp: string;
  source: string;
}

export interface ApiResponse<T> {
  data: T;
  meta: {
    timestamp: string;
    requestId: string;
  };
}

export interface ApiError {
  error: {
    code: string;
    message: string;
    details?: Array<{ field: string; issue: string }>;
  };
  meta: {
    timestamp: string;
    requestId: string;
  };
}
