package com.example.application.user

import com.example.domain.user.*

/**
 * 사용자 생성 Use Case 인터페이스
 * - 애플리케이션 서비스의 계약 정의
 * - 도메인 로직과 외부 세계를 연결하는 경계
 */
interface CreateUserUseCase {
    suspend fun execute(command: CreateUserCommand): CreateUserResult
}

/**
 * 사용자 생성 명령
 * - 외부에서 전달받는 입력 데이터
 * - DTO와 유사하지만 Use Case 전용
 */
data class CreateUserCommand(
    val email: String,
    val name: String,
    val plainPassword: String
)

/**
 * 사용자 생성 결과
 * - Use Case 실행 결과를 타입 안전하게 표현
 * - 성공/실패 케이스를 명확히 구분
 */
sealed class CreateUserResult {
    data class Success(val userId: UserId) : CreateUserResult()
    data class Failure(val errors: List<String>) : CreateUserResult()
}

/**
 * 사용자 생성 Use Case 구현체
 * - 애플리케이션 서비스 (Application Service)
 * - 도메인 객체들을 조합하여 비즈니스 플로우 구현
 * - 트랜잭션 경계 관리
 */
class CreateUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val userService: UserService
) : CreateUserUseCase {
    
    override suspend fun execute(command: CreateUserCommand): CreateUserResult {
        try {
            // 1. 입력 검증 및 값 객체 생성
            val email = Email(command.email)
            val password = Password.fromPlainText(command.plainPassword)
            
            // 2. 이메일 중복 확인 (도메인 서비스 활용)
            when (val duplicationCheck = userService.checkDuplication(email, userRepository)) {
                is DuplicationCheckResult.Duplicated -> {
                    return CreateUserResult.Failure(listOf("이미 사용 중인 이메일입니다"))
                }
                is DuplicationCheckResult.Available -> {
                    // 계속 진행
                }
            }
            
            // 3. 사용자 엔티티 생성
            val user = User(
                id = UserId.generate(),
                email = email,
                name = command.name,
                password = password
            )
            
            // 4. 도메인 검증 (도메인 서비스 활용)
            when (val validationResult = userService.validateUser(user)) {
                is UserValidationResult.Valid -> {
                    // 검증 통과
                }
                is UserValidationResult.Invalid -> {
                    return CreateUserResult.Failure(validationResult.errors)
                }
            }
            
            // 5. 사용자 저장 (Repository 포트 활용)
            val savedUser = userRepository.save(user)
            
            // 6. 성공 결과 반환
            return CreateUserResult.Success(savedUser.id)
            
        } catch (e: IllegalArgumentException) {
            // 값 객체 생성 시 발생하는 검증 오류
            return CreateUserResult.Failure(listOf(e.message ?: "입력값이 유효하지 않습니다"))
        } catch (e: Exception) {
            // 예상치 못한 오류
            return CreateUserResult.Failure(listOf("사용자 생성 중 오류가 발생했습니다"))
        }
    }
}
