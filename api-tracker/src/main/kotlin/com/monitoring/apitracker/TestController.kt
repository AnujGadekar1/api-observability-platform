//src/main/kotlin/com/monitoring/apitracker/TestController.kt
package com.monitoring.apitracker

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/api/test")
    fun testEndpoint(): String {
        // Simulate some work
        Thread.sleep(100)
        return "Hello from the Tracked Service!"
    }
}