// src/main/kotlin/com/monitoring/collector/service/LogIngestionService.kt

package com.monitoring.collector.service

import com.monitoring.collector.model.logs.ApiLog
import com.monitoring.collector.model.logs.LogType
import com.monitoring.collector.model.metadata.Incident
import com.monitoring.collector.model.metadata.IncidentStatus
import com.monitoring.collector.model.metadata.IncidentType
import com.monitoring.collector.repository.logs.ApiLogRepository
import com.monitoring.collector.repository.metadata.IncidentRepository
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class LogIngestionService(
    private val apiLogRepository: ApiLogRepository,
    private val incidentRepository: IncidentRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun processLog(log: ApiLog) {
        // 1. ALWAYS save the raw log to the Primary DB (Logs DB) [cite: 46-48]
        apiLogRepository.save(log)

        // 2. Analyze the log for problems (Metadata DB) [cite: 54, 96]
        detectAndRecordIncidents(log)
    }

    private fun detectAndRecordIncidents(log: ApiLog) {
        var incidentType: IncidentType? = null

        // Rule 1: Rate Limit Exceeded [cite: 99]
        if (log.type == LogType.RATE_LIMIT_HIT) {
            incidentType = IncidentType.RATE_LIMIT
        }
        // Rule 2: Broken API (5xx) [cite: 98]
        else if (log.status >= 500) {
            incidentType = IncidentType.BROKEN_API
        }
        // Rule 3: Slow API (> 500ms) [cite: 97]
        else if (log.latencyMs > 500) {
            incidentType = IncidentType.SLOW_API
        }

        // If an issue was detected, save it to Secondary DB
        if (incidentType != null) {
            createIncidentIfNotExists(log, incidentType)
        }
    }

    private fun createIncidentIfNotExists(log: ApiLog, type: IncidentType) {
        // Check if there is already an OPEN incident for this specific endpoint
        // This prevents flooding the DB with duplicate alerts for the same ongoing issue.
        val existing = incidentRepository.findByServiceNameAndEndpointAndStatus(
            log.serviceName, log.endpoint, IncidentStatus.OPEN
        )

        if (existing == null) {
            val incident = Incident(
                serviceName = log.serviceName,
                endpoint = log.endpoint,
                type = type,
                status = IncidentStatus.OPEN
            )
            incidentRepository.save(incident)
            logger.info("Created new Incident: [$type] for ${log.serviceName} ${log.endpoint}")
        }
    }
}