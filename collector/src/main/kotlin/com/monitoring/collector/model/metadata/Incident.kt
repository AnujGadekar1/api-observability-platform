// src/main/kotlin/com/monitoring/collector/model/metadata/Incident.kt

package com.monitoring.collector.model.metadata

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "incidents")
data class Incident(
    @Id
    val id: String? = null,
    val serviceName: String,
    val endpoint: String,
    val type: IncidentType,       // SLOW or BROKEN
    var status: IncidentStatus,   // OPEN or RESOLVED
    var resolvedBy: String? = null,
    val createdAt: Instant = Instant.now(),
    var resolvedAt: Instant? = null,

    @Version // REQUIRED: Optimistic locking for concurrency safety
    val version: Long? = null
)

enum class IncidentType {
    SLOW_API,   // Latency > 500ms
    BROKEN_API, // Status 5xx
    RATE_LIMIT  // Rate limit exceeded
}

enum class IncidentStatus {
    OPEN,
    RESOLVED
}