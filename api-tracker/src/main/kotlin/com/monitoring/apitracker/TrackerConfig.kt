// src/main/kotlin/com/monitoring/apitracker/TrackerConfig.kt

package com.monitoring.apitracker

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ComponentScan(basePackages = ["com.monitoring.apitracker"])
class TrackerConfig(
    private val monitoringInterceptor: MonitoringInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        // Register our interceptor for all paths
        registry.addInterceptor(monitoringInterceptor).addPathPatterns("/**")
    }
}