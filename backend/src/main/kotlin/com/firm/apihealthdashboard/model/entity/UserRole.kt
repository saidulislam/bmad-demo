package com.firm.apihealthdashboard.model.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_roles")
data class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "display_name")
    var displayName: String? = null,

    @Column(name = "role", nullable = false)
    var role: String,

    @Column(name = "ad_group")
    var adGroup: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Column(name = "last_login")
    var lastLogin: Instant? = null
)
