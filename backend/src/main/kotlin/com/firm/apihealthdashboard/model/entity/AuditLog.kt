package com.firm.apihealthdashboard.model.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "audit_logs")
data class AuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "timestamp", nullable = false)
    val timestamp: Instant = Instant.now(),

    @Column(name = "actor", nullable = false)
    val actor: String,

    @Column(name = "action", nullable = false)
    val action: String,

    @Column(name = "resource", nullable = false)
    val resource: String,

    @Column(name = "resource_id")
    val resourceId: String? = null,

    @Column(name = "old_value", columnDefinition = "jsonb")
    val oldValue: String? = null,

    @Column(name = "new_value", columnDefinition = "jsonb")
    val newValue: String? = null,

    @Column(name = "ip_address")
    val ipAddress: String? = null
)
