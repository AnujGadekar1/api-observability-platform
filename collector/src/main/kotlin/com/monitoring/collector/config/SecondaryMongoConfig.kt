// src/main/kotlin/com/monitoring/collector/config/SecondaryMongoConfig.kt

package com.monitoring.collector.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(
    basePackages = ["com.monitoring.collector.repository.metadata"],
    mongoTemplateRef = "secondaryMongoTemplate"
)
class SecondaryMongoConfig {

    @Value("\${spring.data.mongodb.secondary.uri}")
    lateinit var mongoUri: String

    @Bean(name = ["secondaryMongoFactory"])
    fun secondaryMongoFactory(): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoUri)
    }

    @Bean(name = ["secondaryMongoTemplate"])
    fun secondaryMongoTemplate(secondaryMongoFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(secondaryMongoFactory)
    }
}