// src/main/kotlin/com/monitoring/collector/model/logs/ApiLog.kt

package com.monitoring.collector.model.logs

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "api_logs")
data class ApiLog(
    @Id
    val id: String? = null,
    val traceId: String,       // Unique ID for the request
    val serviceName: String,   // e.g., "orders-service"
    val endpoint: String,      // e.g., "/api/v1/orders"
    val method: String,        // GET, POST, etc.
    val status: Int,           // 200, 404, 500
    val latencyMs: Long,       // Time taken in ms
    val timestamp: Instant = Instant.now(),
    val requestSize: Long = 0,
    val responseSize: Long = 0,
    val type: LogType = LogType.TRAFFIC // To distinguish normal logs from rate-limit hits
)

enum class LogType {
    TRAFFIC,
    RATE_LIMIT_HIT
}