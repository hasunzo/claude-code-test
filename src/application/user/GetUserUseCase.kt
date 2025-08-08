package com.example.application.user

import com.example.domain.user.*

/**
 * 사용자 조회 Use Case 인터페이스
 */
interface GetUserUseCase {
    suspend fun execute(query: GetUserQuery): GetUserResult
}

/**
 * 사용자 조회 쿼리
 */
data class GetUserQuery(
    val userId: UserId
)

/**
 * 사용자 조회 결과
 */
sealed class GetUserResult {
    data class Found(val user: User) : GetUserResult()
    object NotFound : GetUserResult()
}

/**
 * 사용자 조회 Use Case 구현체
 */
class GetUserUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserUseCase {
    
    override suspend fun execute(query: GetUserQuery): GetUserResult {
        val user = userRepository.findById(query.userId)
        
        return if (user != null) {
            GetUserResult.Found(user)
        } else {
            GetUserResult.NotFound
        }
    }
}

/**
 * 이메일로 사용자 조회 Use Case
 */
interface GetUserByEmailUseCase {
    suspend fun execute(query: GetUserByEmailQuery): GetUserByEmailResult
}

data class GetUserByEmailQuery(
    val email: String
)

sealed class GetUserByEmailResult {
    data class Found(val user: User) : GetUserByEmailResult()
    object NotFound : GetUserByEmailResult()
    data class InvalidEmail(val message: String) : GetUserByEmailResult()
}

class GetUserByEmailUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserByEmailUseCase {
    
    override suspend fun execute(query: GetUserByEmailQuery): GetUserByEmailResult {
        return try {
            val email = Email(query.email)
            val user = userRepository.findByEmail(email)
            
            if (user != null) {
                GetUserByEmailResult.Found(user)
            } else {
                GetUserByEmailResult.NotFound
            }
            
        } catch (e: IllegalArgumentException) {
            GetUserByEmailResult.InvalidEmail(e.message ?: "유효하지 않은 이메일 형식입니다")
        }
    }
}
