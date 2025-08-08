package com.example.domain.user

import java.time.LocalDateTime
import java.util.UUID

/**
 * 사용자 도메인 엔티티
 * - 순수한 비즈니스 로직만 포함
 * - 외부 의존성 없음 (인프라스트럭처 독립적)
 */
data class User(
    val id: UserId,
    val email: Email,
    val name: String,
    val password: Password,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    
    /**
     * 비밀번호 변경
     * 도메인 비즈니스 규칙: 새 비밀번호는 기존과 달라야 함
     */
    fun changePassword(newPassword: Password): User {
        require(newPassword != this.password) { "새 비밀번호는 기존 비밀번호와 달라야 합니다" }
        return this.copy(password = newPassword)
    }
    
    /**
     * 사용자 활성 상태 확인
     * 비즈니스 규칙: 생성된 지 1년 이내면 활성
     */
    fun isActive(): Boolean {
        val oneYearAgo = LocalDateTime.now().minusYears(1)
        return createdAt.isAfter(oneYearAgo)
    }
    
    /**
     * 이메일 변경 가능 여부 확인
     * 비즈니스 규칙: 생성된 지 24시간 이후부터 변경 가능
     */
    fun canChangeEmail(): Boolean {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        return createdAt.isBefore(twentyFourHoursAgo)
    }
}

/**
 * 사용자 ID 값 객체 (Value Object)
 * - 타입 안전성 보장
 * - 의미 있는 도메인 개념 표현
 */
@JvmInline
value class UserId(val value: UUID) {
    companion object {
        fun generate(): UserId = UserId(UUID.randomUUID())
        fun from(value: String): UserId = UserId(UUID.fromString(value))
    }
}

/**
 * 이메일 값 객체
 * - 이메일 형식 검증 포함
 * - 불변성 보장
 */
@JvmInline  
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "이메일은 비어있을 수 없습니다" }
        require(value.contains("@")) { "유효하지 않은 이메일 형식입니다" }
        require(value.length <= 254) { "이메일은 254자를 초과할 수 없습니다" }
    }
}

/**
 * 비밀번호 값 객체
 * - 항상 해시된 상태로 저장
 * - 평문 비밀번호는 생성 시점에만 허용
 */
@JvmInline
value class Password(val hashedValue: String) {
    companion object {
        /**
         * 평문 비밀번호로부터 Password 객체 생성
         * 실제 환경에서는 BCrypt 등의 해시 알고리즘 사용 필요
         */
        fun fromPlainText(plainText: String): Password {
            require(plainText.isNotBlank()) { "비밀번호는 비어있을 수 없습니다" }
            require(plainText.length >= 8) { "비밀번호는 최소 8자 이상이어야 합니다" }
            
            // TODO: 실제 프로덕션에서는 BCrypt 사용
            val hashed = "hashed_$plainText"
            return Password(hashed)
        }
        
        /**
         * 이미 해시된 비밀번호로부터 Password 객체 생성
         * DB에서 조회한 데이터 사용 시
         */
        fun fromHashedValue(hashedValue: String): Password {
            require(hashedValue.isNotBlank()) { "해시된 비밀번호는 비어있을 수 없습니다" }
            return Password(hashedValue)
        }
    }
    
    /**
     * 평문 비밀번호와 비교
     */
    fun matches(plainText: String): Boolean {
        // TODO: 실제 프로덕션에서는 BCrypt.checkpw() 사용
        return hashedValue == "hashed_$plainText"
    }
}
