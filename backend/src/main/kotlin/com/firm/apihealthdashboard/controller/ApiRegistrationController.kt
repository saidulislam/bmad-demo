package com.firm.apihealthdashboard.controller

import com.firm.apihealthdashboard.model.entity.ApiRegistration
import com.firm.apihealthdashboard.service.ApiRegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/v1/api-registrations")
class ApiRegistrationController(private val service: ApiRegistrationService) {

    data class RegisterRequest(
        val apiName: String,
        val owningTeam: String,
        val criticalityTier: Int,
        val description: String? = null
    )

    @PostMapping
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Map<String, Any>> {
        val registration = service.register(
            request.apiName, request.owningTeam, request.criticalityTier,
            request.description, "system" // Will use JWT principal in Story 1.2
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf("data" to registration, "meta" to mapOf("timestamp" to Instant.now().toString()))
        )
    }

    @GetMapping
    fun list(@RequestParam(required = false) team: String?,
             @RequestParam(required = false) tier: Int?): ResponseEntity<Map<String, Any>> {
        val results = when {
            team != null -> service.findByTeam(team)
            tier != null -> service.findByTier(tier)
            else -> service.findActive()
        }
        return ResponseEntity.ok(
            mapOf("data" to results, "meta" to mapOf("timestamp" to Instant.now().toString()))
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Map<String, Any>> {
        val registration = service.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            mapOf("data" to registration, "meta" to mapOf("timestamp" to Instant.now().toString()))
        )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody updates: Map<String, Any?>): ResponseEntity<Map<String, Any>> {
        val updated = service.update(id, updates, "system") ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            mapOf("data" to updated, "meta" to mapOf("timestamp" to Instant.now().toString()))
        )
    }

    @DeleteMapping("/{id}")
    fun decommission(@PathVariable id: UUID): ResponseEntity<Map<String, Any>> {
        val decommissioned = service.decommission(id, "system") ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            mapOf("data" to decommissioned, "meta" to mapOf("timestamp" to Instant.now().toString()))
        )
    }
}
