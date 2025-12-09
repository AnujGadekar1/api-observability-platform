// src/main/kotlin/com/monitoring/apitracker/TrackerLog.kt

package com.monitoring.apitracker

data class TrackerLog(
    val traceId: String,
    val serviceName: String,
    val endpoint: String,
    val method: String,
    val status: Int,
    val latencyMs: Long,
    val timestamp: String,
    val type: String // "TRAFFIC" or "RATE_LIMIT_HIT"
)