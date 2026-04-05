package com.firm.apihealthdashboard.service

import com.firm.apihealthdashboard.model.entity.AuditLog
import com.firm.apihealthdashboard.repository.AuditLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuditService(private val auditLogRepository: AuditLogRepository) {

    fun log(actor: String, action: String, resource: String, resourceId: String? = null,
            oldValue: String? = null, newValue: String? = null, ipAddress: String? = null) {
        auditLogRepository.save(
            AuditLog(
                actor = actor,
                action = action,
                resource = resource,
                resourceId = resourceId,
                oldValue = oldValue,
                newValue = newValue,
                ipAddress = ipAddress
            )
        )
    }

    fun query(actor: String? = null, action: String? = null,
              startDate: Instant? = null, endDate: Instant? = null,
              pageable: Pageable): Page<AuditLog> {
        val start = startDate ?: Instant.EPOCH
        val end = endDate ?: Instant.now()

        return when {
            actor != null -> auditLogRepository.findByActorAndTimestampBetween(actor, start, end, pageable)
            action != null -> auditLogRepository.findByAction(action, pageable)
            else -> auditLogRepository.findByTimestampBetween(start, end, pageable)
        }
    }
}
