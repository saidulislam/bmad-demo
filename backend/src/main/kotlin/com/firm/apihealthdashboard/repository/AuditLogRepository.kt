package com.firm.apihealthdashboard.repository

import com.firm.apihealthdashboard.model.entity.AuditLog
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface AuditLogRepository : JpaRepository<AuditLog, Long> {
    fun findByTimestampBetween(start: Instant, end: Instant, pageable: Pageable): Page<AuditLog>
    fun findByActor(actor: String, pageable: Pageable): Page<AuditLog>
    fun findByAction(action: String, pageable: Pageable): Page<AuditLog>
    fun findByActorAndTimestampBetween(actor: String, start: Instant, end: Instant, pageable: Pageable): Page<AuditLog>
}
