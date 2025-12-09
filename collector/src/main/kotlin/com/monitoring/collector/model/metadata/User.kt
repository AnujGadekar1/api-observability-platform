//src/main/kotlin/com/monitoring/collector/model/metadata/User.kt
package com.monitoring.collector.model.metadata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed // Import this
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,

    @Indexed(unique = true) // <--- ADD THIS ANNOTATION
    val username: String,

    val password: String
)