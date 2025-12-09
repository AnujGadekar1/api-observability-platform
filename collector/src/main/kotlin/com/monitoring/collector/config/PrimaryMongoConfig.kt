// src/main/kotlin/com/monitoring/collector/config/PrimaryMongoConfig.kt

package com.monitoring.collector.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(
    basePackages = ["com.monitoring.collector.repository.logs"],
    mongoTemplateRef = "primaryMongoTemplate"
)
class PrimaryMongoConfig {

    // We inject the URI string directly instead of using MongoProperties
    @Value("\${spring.data.mongodb.primary.uri}")
    lateinit var mongoUri: String

    @Primary
    @Bean(name = ["primaryMongoFactory"])
    fun primaryMongoFactory(): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoUri)
    }

    @Primary
    @Bean(name = ["primaryMongoTemplate"])
    fun primaryMongoTemplate(primaryMongoFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(primaryMongoFactory)
    }
}