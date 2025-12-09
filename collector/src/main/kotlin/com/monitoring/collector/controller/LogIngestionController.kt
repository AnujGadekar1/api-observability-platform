// src/main/kotlin/com/monitoring/collector/controller/LogIngestionController.kt

package com.monitoring.collector.controller

import com.monitoring.collector.model.logs.ApiLog
import com.monitoring.collector.service.LogIngestionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/collector")
class LogIngestionController(private val service: LogIngestionService) {

    @PostMapping("/logs")
    fun ingestLog(@RequestBody log: ApiLog): ResponseEntity<Void> {
        service.processLog(log)
        return ResponseEntity.ok().build()
    }
}