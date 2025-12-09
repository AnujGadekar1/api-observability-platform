//src/main/kotlin/com/monitoring/collector/repository/metadata/UserRepository.kt

package com.monitoring.collector.repository.metadata

import com.monitoring.collector.model.metadata.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
}