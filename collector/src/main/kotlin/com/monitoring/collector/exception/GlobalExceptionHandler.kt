//src/main/kotlin/com/monitoring/collector/exception/GlobalExceptionHandler.kt
package com.monitoring.collector.exception

import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    // Handles JSON parsing errors (e.g., invalid body)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBadInput(e: HttpMessageNotReadableException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf("error" to "Invalid Request Body: Check your JSON format."))
    }

    // Handles all other errors safely
    @ExceptionHandler(Exception::class)
    fun handleGeneralError(e: Exception): ResponseEntity<Map<String, String>> {
        e.printStackTrace() // Print the real error to console
        return ResponseEntity.internalServerError().body(mapOf("error" to (e.message ?: "Unknown Error")))
    }
}