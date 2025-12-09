// src/main/kotlin/com/monitoring/collector/repository/metadata/IncidentRepository.kt

package com.monitoring.collector.repository.metadata

import com.monitoring.collector.model.metadata.Incident
import com.monitoring.collector.model.metadata.IncidentStatus
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IncidentRepository : MongoRepository<Incident, String> {

    // REQUIRED: Used by LogIngestionService to prevent duplicate alerts
    fun findByServiceNameAndEndpointAndStatus(
        serviceName: String,
        endpoint: String,
        status: IncidentStatus
    ): Incident?

}