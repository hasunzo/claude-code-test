package com.example.domain.user

/**
 * 사용자 도메인 서비스
 * - 복잡한 비즈니스 로직 처리
 * - 여러 엔티티를 조합한 비즈니스 규칙
 * - 순수한 도메인 로직만 포함 (외부 의존성 없음)
 */
class UserService {
    
    /**
     * 사용자 유효성 검증
     * - 복합적인 비즈니스 규칙 적용
     * - 단일 엔티티로는 표현하기 어려운 검증 로직
     */
    fun validateUser(user: User): UserValidationResult {
        val errors = mutableListOf<String>()
        
        // 이름 검증
        if (user.name.isBlank()) {
            errors.add("이름은 비어있을 수 없습니다")
        }
        
        if (user.name.length < 2) {
            errors.add("이름은 최소 2자 이상이어야 합니다")
        }
        
        if (user.name.length > 50) {
            errors.add("이름은 50자를 초과할 수 없습니다")
        }
        
        // 특수 문자 포함 여부 검증
        if (user.name.any { !it.isLetterOrDigit() && it != ' ' }) {
            errors.add("이름에는 특수문자를 포함할 수 없습니다")
        }
        
        return if (errors.isEmpty()) {
            UserValidationResult.Valid
        } else {
            UserValidationResult.Invalid(errors)
        }
    }
    
    /**
     * 사용자 중복 여부 확인
     * - 이메일 기반 중복 확인
     * - 비즈니스 규칙: 같은 이메일로는 계정 생성 불가
     */
    suspend fun checkDuplication(
        email: Email, 
        userRepository: UserRepository
    ): DuplicationCheckResult {
        val existingUser = userRepository.findByEmail(email)
        
        return if (existingUser != null) {
            DuplicationCheckResult.Duplicated(existingUser.id)
        } else {
            DuplicationCheckResult.Available
        }
    }
    
    /**
     * 사용자 간 관계 확인
     * - 복잡한 비즈니스 규칙 (예: 같은 도메인 이메일 사용자)
     */
    fun areRelatedUsers(user1: User, user2: User): Boolean {
        val domain1 = user1.email.value.substringAfter("@")
        val domain2 = user2.email.value.substringAfter("@")
        
        return domain1 == domain2
    }
    
    /**
     * 계정 잠금 필요 여부 판단
     * - 보안 관련 비즈니스 규칙
     */
    fun shouldLockAccount(user: User, failedLoginAttempts: Int): Boolean {
        // 비즈니스 규칙: 5회 이상 로그인 실패 시 잠금
        if (failedLoginAttempts >= 5) {
            return true
        }
        
        // 비즈니스 규칙: 비활성 사용자는 잠금
        if (!user.isActive()) {
            return true
        }
        
        return false
    }
}

/**
 * 사용자 검증 결과
 */
sealed class UserValidationResult {
    object Valid : UserValidationResult()
    data class Invalid(val errors: List<String>) : UserValidationResult()
}

/**
 * 중복 확인 결과
 */
sealed class DuplicationCheckResult {
    object Available : DuplicationCheckResult()
    data class Duplicated(val existingUserId: UserId) : DuplicationCheckResult()
}
