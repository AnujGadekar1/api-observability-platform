//src/main/kotlin/com/monitoring/collector/controller/AuthController.kt
package com.monitoring.collector.controller

import com.monitoring.collector.model.metadata.User
import com.monitoring.collector.repository.metadata.UserRepository
import com.monitoring.collector.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) {

    // Simple Login (In prod, use AuthenticationManager)
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Map<String, String>): ResponseEntity<Map<String, String>> {
        val username = loginRequest["username"]
        val password = loginRequest["password"]

        // 1. Check DB for user
        val user = userRepository.findByUsername(username ?: "")

        // 2. Simple password check (In prod, use BCrypt matches)
        if (user != null && user.password == password) {
            val token = jwtUtil.generateToken(username!!)
            return ResponseEntity.ok(mapOf("token" to token))
        }

        return ResponseEntity.status(401).build()
    }

    // Quick helper to seed a user for testing
    @PostMapping("/register")
    fun register(@RequestBody user: User): User {
        return userRepository.save(user)
    }
}