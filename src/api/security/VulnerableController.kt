package com.example.api.security

import org.springframework.web.bind.annotation.*
import java.sql.DriverManager

/**
 * ❌ 매우 위험한 보안 취약점들이 포함된 테스트 컨트롤러
 * 자동화된 Claude Code Review가 이 모든 문제들을 탐지할 수 있는지 테스트
 */
@RestController  
@RequestMapping("/api/vulnerable")
class VulnerableController {
    
    // ❌ 하드코딩된 데이터베이스 연결 정보
    private val dbUrl = "jdbc:mysql://localhost:3306/mydb"
    private val dbUser = "admin"
    private val dbPassword = "password123"
    
    // ❌ 하드코딩된 JWT 비밀키
    private val jwtSecret = "mySecretKey123"
    
    // ❌ 외부 API 키들 하드코딩
    private val awsAccessKey = "AKIAIOSFODNN7EXAMPLE"
    private val awsSecretKey = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    private val googleApiKey = "AIzaSyDhGHjsaj9s0ag9s0ag9s0ag9s0ag9s0ag"
    
    @PostMapping("/login")
    fun dangerousLogin(@RequestParam username: String, @RequestParam password: String): String {
        // ❌ SQL 인젝션 매우 위험!
        val query = "SELECT * FROM users WHERE username = '$username' AND password = '$password'"
        
        try {
            val connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
            val statement = connection.createStatement()
            val result = statement.executeQuery(query)
            
            // ❌ 민감한 정보 로깅
            println("Login attempt: $username with password: $password")
            
            if (result.next()) {
                // ❌ 사용자 정보를 그대로 반환
                return "Welcome ${result.getString("username")}! Your email: ${result.getString("email")}, " +
                       "SSN: ${result.getString("ssn")}, Credit Card: ${result.getString("credit_card")}"
            }
        } catch (e: Exception) {
            // ❌ 스택트레이스 노출
            return "Database error: ${e.message}\n${e.stackTrace.joinToString("\n")}"
        }
        
        return "Login failed"
    }
    
    @GetMapping("/search")
    fun vulnerableSearch(@RequestParam query: String): String {
        // ❌ XSS 취약점 - 입력을 그대로 HTML에 삽입
        return "<html><body><h1>Search Results for: $query</h1></body></html>"
    }
    
    @PostMapping("/upload") 
    fun dangerousFileUpload(@RequestParam file: String): String {
        // ❌ 파일 업로드 검증 없음
        val fileName = file
        
        // ❌ 경로 순회 공격 가능
        java.io.File("/uploads/../../../etc/passwd").writeText(file)
        
        return "File uploaded: $fileName"
    }
    
    @GetMapping("/admin/{userId}")
    fun adminAccess(@PathVariable userId: String): String {
        // ❌ 권한 검사 없음
        // ❌ SQL 인젝션 또 다시
        val query = "SELECT * FROM admin_data WHERE user_id = $userId"
        
        // ❌ 민감한 관리자 정보 노출
        return "Admin data: All user passwords, API keys, financial records for user $userId"
    }
    
    @PostMapping("/eval")
    fun dangerousEval(@RequestParam code: String): String {
        // ❌ 코드 인젝션 - 매우 위험!
        return try {
            // 실제로는 eval 같은 기능 (Kotlin에는 직접적인 eval 없지만 유사한 위험)
            "Executed: $code"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    
    private fun generateWeakPassword(): String {
        // ❌ 약한 비밀번호 생성
        return "password123"
    }
    
    private fun unsafeDeserialization(data: String): Any? {
        // ❌ 안전하지 않은 역직렬화
        return try {
            // ObjectInputStream 같은 위험한 역직렬화 시뮬레이션
            data.split(",").map { it.trim() }
        } catch (e: Exception) {
            null
        }
    }
}
