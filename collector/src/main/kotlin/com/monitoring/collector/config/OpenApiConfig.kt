//src/main/kotlin/com/monitoring/collector/config/OpenApiConfig.kt
package com.monitoring.collector.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun monitoringOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(Info().title("Observability Collector API")
                .description("API for ingesting logs and managing incidents")
                .version("v1.0"))
    }
}