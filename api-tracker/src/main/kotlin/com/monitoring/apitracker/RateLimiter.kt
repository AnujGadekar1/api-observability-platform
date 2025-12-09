// src/main/kotlin/com/monitoring/apitracker/RateLimiter.kt

package com.monitoring.apitracker

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.time.Instant

@Component
class RateLimiter {

    // Default limit is 100, but can be overridden in application.yaml
    @Value("\${monitoring.rateLimit.limit:100}")
    private var limit: Int = 100

    // Store request counts per second: Map<EpochSecond, Count>
    // In a real production app, we might use Bucket4j, but a simple map works for this challenge.
    private val requestCounts = ConcurrentHashMap<Long, AtomicInteger>()

    /**
     * Returns TRUE if the request is allowed.
     * Returns FALSE if the limit is exceeded.
     */
    fun tryAcquire(): Boolean {
        val currentSecond = Instant.now().epochSecond

        // Clean up old entries (simple garbage collection for this demo)
        if (!requestCounts.containsKey(currentSecond)) {
            requestCounts.clear()
            requestCounts[currentSecond] = AtomicInteger(0)
        }

        val counter = requestCounts.computeIfAbsent(currentSecond) { AtomicInteger(0) }
        val currentCount = counter.incrementAndGet()

        return currentCount <= limit
    }
}