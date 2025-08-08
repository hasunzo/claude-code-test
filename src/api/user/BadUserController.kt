package com.example.api.user

import com.example.domain.user.User
import com.example.domain.user.UserRepository
import org.springframework.web.bind.annotation.*

/**
 * ❌ 나쁜 코드 예시 - 아키텍처 위반 및 보안 취약점
 */
@RestController
@RequestMapping("/api/bad-users")
class BadUserController(
    private val userRepository: UserRepository // ❌ Controller → Repository 직접 의존
) {
    
    @PostMapping
    fun createUser(@RequestBody request: Map<String, String>): String {
        // ❌ 입력 검증 없음
        val email = request["email"] ?: "default@example.com"
        val password = request["password"] ?: "password123" // ❌ 기본 비밀번호
        
        // ❌ 비밀번호 평문 저장
        val user = User(
            id = com.example.domain.user.UserId.generate(),
            email = com.example.domain.user.Email(email),
            name = request["name"] ?: "Anonymous",
            password = com.example.domain.user.Password.fromHashedValue(password) // 평문을 해시로 잘못 처리
        )
        
        userRepository.save(user) // ❌ UseCase 없이 직접 호출
        
        // ❌ 민감한 정보 노출
        return "User created with password: $password"
    }
    
    @GetMapping("/search")
    fun searchUsers(@RequestParam query: String): String {
        // ❌ 하드코딩된 API 키
        val apiKey = "sk-1234567890abcdef"
        
        // ❌ SQL 인젝션 위험 (가상의 예시)
        val sql = "SELECT * FROM users WHERE name = '$query'"
        
        return "Searching with: $sql"
    }
}
