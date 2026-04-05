package com.firm.apihealthdashboard.model.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "api_registrations")
data class ApiRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "api_name", nullable = false)
    var apiName: String,

    @Column(name = "owning_team", nullable = false)
    var owningTeam: String,

    @Column(name = "criticality_tier", nullable = false)
    var criticalityTier: Int,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "telemetry_source_type")
    var telemetrySourceType: String? = null,

    @Column(name = "telemetry_config", columnDefinition = "jsonb")
    var telemetryConfig: String? = null,

    @Column(name = "sla_availability_target")
    var slaAvailabilityTarget: BigDecimal? = null,

    @Column(name = "sla_latency_p50_ms")
    var slaLatencyP50Ms: Int? = null,

    @Column(name = "sla_latency_p95_ms")
    var slaLatencyP95Ms: Int? = null,

    @Column(name = "sla_latency_p99_ms")
    var slaLatencyP99Ms: Int? = null,

    @Column(name = "sla_error_rate_ceiling")
    var slaErrorRateCeiling: BigDecimal? = null,

    @Column(name = "status", nullable = false)
    var status: String = "pending_configuration",

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Column(name = "decommissioned_at")
    var decommissionedAt: Instant? = null
)
