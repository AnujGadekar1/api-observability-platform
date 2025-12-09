// src/main/kotlin/com/monitoring/apitracker/MonitoringInterceptor.kt

package com.monitoring.apitracker

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.util.UUID
import java.util.concurrent.Executors

@Component
class MonitoringInterceptor(
    private val rateLimiter: RateLimiter
) : HandlerInterceptor {

    @Value("\${spring.application.name:unknown-service}")
    private lateinit var serviceName: String

    @Value("\${monitoring.collector.url:http://localhost:8080/api/v1/collector/logs}")
    private lateinit var collectorUrl: String

    private val restTemplate = RestTemplate()
    // Use a separate thread pool so logging doesn't slow down the main API
    private val executor = Executors.newFixedThreadPool(5)
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 1. Start Timer
        request.setAttribute("startTime", System.currentTimeMillis())

        // 2. Check Rate Limit
        if (!rateLimiter.tryAcquire()) {
            // Requirement: "If limit is exceeded... request should still continue normally"
            // We just mark it so we know to log it as a violation later.
            request.setAttribute("rateLimitHit", true)
            logger.warn("Rate limit hit for ${request.requestURI}")
        }

        return true // Always allow the request to proceed
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val startTime = request.getAttribute("startTime") as Long
        val duration = System.currentTimeMillis() - startTime
        val isRateLimitHit = request.getAttribute("rateLimitHit") == true

        // Prepare the log object
        val logEntry = TrackerLog(
            traceId = UUID.randomUUID().toString(),
            serviceName = serviceName,
            endpoint = request.requestURI,
            method = request.method,
            status = response.status,
            latencyMs = duration,
            timestamp = Instant.now().toString(),
            type = if (isRateLimitHit) "RATE_LIMIT_HIT" else "TRAFFIC"
        )

        // Send to Collector Asynchronously
        executor.submit {
            try {
                restTemplate.postForEntity(collectorUrl, logEntry, Void::class.java)
            } catch (e: Exception) {
                logger.error("Failed to send log to collector: ${e.message}")
            }
        }
    }
}