package com.monitoring.collector.repository.logs

import com.monitoring.collector.model.logs.ApiLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiLogRepository : MongoRepository<ApiLog, String> {
    // Basic filter for the dashboard
    fun findByServiceName(serviceName: String): List<ApiLog>
    fun findByStatusGreaterThanEqual(status: Int): List<ApiLog> // For broken APIs
}