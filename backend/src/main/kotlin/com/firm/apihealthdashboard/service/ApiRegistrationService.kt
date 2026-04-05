package com.firm.apihealthdashboard.service

import com.firm.apihealthdashboard.model.entity.ApiRegistration
import com.firm.apihealthdashboard.repository.ApiRegistrationRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ApiRegistrationService(
    private val repository: ApiRegistrationRepository,
    private val auditService: AuditService
) {

    fun register(apiName: String, owningTeam: String, criticalityTier: Int,
                 description: String?, actor: String): ApiRegistration {
        val registration = repository.save(
            ApiRegistration(
                apiName = apiName,
                owningTeam = owningTeam,
                criticalityTier = criticalityTier,
                description = description
            )
        )
        auditService.log(actor, "CREATE", "api_registration", registration.id.toString(),
            newValue = """{"apiName":"$apiName","owningTeam":"$owningTeam","tier":$criticalityTier}""")
        return registration
    }

    fun findById(id: UUID): ApiRegistration? = repository.findById(id).orElse(null)

    fun findActive(): List<ApiRegistration> = repository.findByStatusNot("decommissioned")

    fun findByTeam(team: String): List<ApiRegistration> = repository.findByOwningTeam(team)

    fun findByTier(tier: Int): List<ApiRegistration> = repository.findByCriticalityTier(tier)

    fun update(id: UUID, updates: Map<String, Any?>, actor: String): ApiRegistration? {
        val existing = repository.findById(id).orElse(null) ?: return null
        val oldValue = """{"apiName":"${existing.apiName}","owningTeam":"${existing.owningTeam}"}"""

        updates["apiName"]?.let { existing.apiName = it as String }
        updates["owningTeam"]?.let { existing.owningTeam = it as String }
        updates["criticalityTier"]?.let { existing.criticalityTier = it as Int }
        updates["description"]?.let { existing.description = it as String }
        existing.updatedAt = Instant.now()

        val saved = repository.save(existing)
        auditService.log(actor, "UPDATE", "api_registration", id.toString(), oldValue,
            """{"apiName":"${saved.apiName}","owningTeam":"${saved.owningTeam}"}""")
        return saved
    }

    fun decommission(id: UUID, actor: String): ApiRegistration? {
        val existing = repository.findById(id).orElse(null) ?: return null
        existing.status = "decommissioned"
        existing.decommissionedAt = Instant.now()
        existing.updatedAt = Instant.now()
        val saved = repository.save(existing)
        auditService.log(actor, "DECOMMISSION", "api_registration", id.toString())
        return saved
    }
}
