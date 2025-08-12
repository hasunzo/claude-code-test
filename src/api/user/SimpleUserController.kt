package com.example.api.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class SimpleUserController {
    
    // 보안 취약점: 하드코딩된 API 키
    private val apiKey = "sk-1234567890abcdef"
    
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): String {
        // 보안 취약점: SQL 인젝션
        val query = "SELECT * FROM users WHERE id = '$id'"
        return "User data for: $query"
    }
    
    @PostMapping
    fun createUser(@RequestBody userData: String): String {
        // 보안 취약점: 입력 검증 없음
        return "Created user: $userData"
    }
}
# Test commit for security review
