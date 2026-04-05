package com.firm.apihealthdashboard.repository

import com.firm.apihealthdashboard.model.entity.ApiRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApiRegistrationRepository : JpaRepository<ApiRegistration, UUID> {
    fun findByStatus(status: String): List<ApiRegistration>
    fun findByOwningTeam(owningTeam: String): List<ApiRegistration>
    fun findByCriticalityTier(tier: Int): List<ApiRegistration>
    fun findByStatusNot(status: String): List<ApiRegistration>
}
