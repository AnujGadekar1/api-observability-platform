package com.monitoring.apitracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiTrackerApplication

fun main(args: Array<String>) {
	runApplication<ApiTrackerApplication>(*args)
}
