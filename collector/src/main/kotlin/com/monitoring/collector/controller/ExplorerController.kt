// src/main/kotlin/com/monitoring/collector/controller/ExplorerController.kt

package com.monitoring.collector.controller

import com.monitoring.collector.model.logs.ApiLog
import com.monitoring.collector.model.metadata.Incident
import com.monitoring.collector.model.metadata.IncidentStatus
import com.monitoring.collector.repository.logs.ApiLogRepository
import com.monitoring.collector.repository.metadata.IncidentRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/v1/collector")
@CrossOrigin(origins = ["http://localhost:3000"]) // Allow Next.js to access this
class ExplorerController(
    private val apiLogRepository: ApiLogRepository,
    private val incidentRepository: IncidentRepository
) {

    // --- LOGS DB (Primary) ---

    @GetMapping("/logs")
    fun getLogs(@RequestParam(required = false) service: String?): List<ApiLog> {
        return if (service != null) {
            apiLogRepository.findByServiceName(service)
        } else {
            apiLogRepository.findAll()
        }
    }

    // --- METADATA DB (Secondary) ---

    @GetMapping("/incidents")
    fun getIncidents(): List<Incident> {
        return incidentRepository.findAll()
    }

    @PutMapping("/incidents/{id}/resolve")
    fun resolveIncident(
        @PathVariable id: String,
        @RequestParam resolver: String
    ): ResponseEntity<Incident> {
        val incidentOpt = incidentRepository.findById(id)
        if (incidentOpt.isEmpty) return ResponseEntity.notFound().build()

        val incident = incidentOpt.get()
        incident.status = IncidentStatus.RESOLVED
        incident.resolvedBy = resolver
        incident.resolvedAt = Instant.now()

        // This save will trigger Optimistic Locking version check automatically!
        val saved = incidentRepository.save(incident)
        return ResponseEntity.ok(saved)
    }
}