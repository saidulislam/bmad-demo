package com.firm.apihealthdashboard.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1")
class HealthController {

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "data" to mapOf(
                    "status" to "UP",
                    "version" to "0.1.0"
                ),
                "meta" to mapOf(
                    "timestamp" to Instant.now().toString()
                )
            )
        )
    }
}
