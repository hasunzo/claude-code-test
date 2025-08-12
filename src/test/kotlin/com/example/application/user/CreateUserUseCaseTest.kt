package com.example.application.user

import com.example.domain.user.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateUserUseCaseTest {

    private val mockUserRepository = mockk<UserRepository>()
    private val mockUserService = mockk<UserService>()
    private lateinit var createUserUseCase: CreateUserUseCase

    @BeforeEach
    fun setUp() {
        createUserUseCase = CreateUserUseCaseImpl(mockUserRepository, mockUserService)
        clearAllMocks()
    }

    @Test
    fun `should create user successfully when all validations pass`() = runTest {
        val command = CreateUserCommand(
            email = "john@example.com",
            name = "John Doe",
            plainPassword = "password123"
        )

        val expectedUser = User(1L, "John Doe", "john@example.com")
        val expectedUserId = UserId(1L)

        coEvery { mockUserService.checkDuplication(any(), mockUserRepository) } returns DuplicationCheckResult.Available
        every { mockUserService.validateUser(any()) } returns UserValidationResult.Valid
        coEvery { mockUserRepository.save(any()) } returns expectedUser.copy(id = expectedUserId.value)

        val result = createUserUseCase.execute(command)

        assertTrue(result is CreateUserResult.Success)
        assertEquals(expectedUserId, (result as CreateUserResult.Success).userId)

        coVerify { mockUserService.checkDuplication(any(), mockUserRepository) }
        verify { mockUserService.validateUser(any()) }
        coVerify { mockUserRepository.save(any()) }
    }

    @Test
    fun `should fail when email is already in use`() = runTest {
        val command = CreateUserCommand(
            email = "john@example.com",
            name = "John Doe",
            plainPassword = "password123"
        )

        val existingUserId = UserId(99L)
        coEvery { mockUserService.checkDuplication(any(), mockUserRepository) } returns 
            DuplicationCheckResult.Duplicated(existingUserId)

        val result = createUserUseCase.execute(command)

        assertTrue(result is CreateUserResult.Failure)
        assertEquals(listOf("이미 사용 중인 이메일입니다"), (result as CreateUserResult.Failure).errors)

        coVerify { mockUserService.checkDuplication(any(), mockUserRepository) }
        verify(exactly = 0) { mockUserService.validateUser(any()) }
        coVerify(exactly = 0) { mockUserRepository.save(any()) }
    }

    @Test
    fun `should fail when user validation fails`() = runTest {
        val command = CreateUserCommand(
            email = "john@example.com",
            name = "A",
            plainPassword = "password123"
        )

        val validationErrors = listOf("이름은 최소 2자 이상이어야 합니다")
        
        coEvery { mockUserService.checkDuplication(any(), mockUserRepository) } returns DuplicationCheckResult.Available
        every { mockUserService.validateUser(any()) } returns UserValidationResult.Invalid(validationErrors)

        val result = createUserUseCase.execute(command)

        assertTrue(result is CreateUserResult.Failure)
        assertEquals(validationErrors, (result as CreateUserResult.Failure).errors)

        coVerify { mockUserService.checkDuplication(any(), mockUserRepository) }
        verify { mockUserService.validateUser(any()) }
        coVerify(exactly = 0) { mockUserRepository.save(any()) }
    }

    @Test
    fun `should handle invalid email format gracefully`() = runTest {
        val command = CreateUserCommand(
            email = "invalid-email",
            name = "John Doe",
            plainPassword = "password123"
        )

        val result = createUserUseCase.execute(command)

        assertTrue(result is CreateUserResult.Failure)
        assertTrue((result as CreateUserResult.Failure).errors.isNotEmpty())

        verify(exactly = 0) { mockUserService.checkDuplication(any(), mockUserRepository) }
        verify(exactly = 0) { mockUserService.validateUser(any()) }
        coVerify(exactly = 0) { mockUserRepository.save(any()) }
    }

    @Test
    fun `should handle repository exception gracefully`() = runTest {
        val command = CreateUserCommand(
            email = "john@example.com",
            name = "John Doe",
            plainPassword = "password123"
        )

        coEvery { mockUserService.checkDuplication(any(), mockUserRepository) } returns DuplicationCheckResult.Available
        every { mockUserService.validateUser(any()) } returns UserValidationResult.Valid
        coEvery { mockUserRepository.save(any()) } throws RuntimeException("Database error")

        val result = createUserUseCase.execute(command)

        assertTrue(result is CreateUserResult.Failure)
        assertEquals(listOf("사용자 생성 중 오류가 발생했습니다"), (result as CreateUserResult.Failure).errors)
    }
}

// Mock classes for missing domain objects
data class UserId(val value: Long) {
    companion object {
        fun generate() = UserId(System.currentTimeMillis())
    }
}

data class Email(val value: String) {
    init {
        require(value.contains("@")) { "유효하지 않은 이메일 형식입니다" }
    }
}

data class Password(val value: String) {
    companion object {
        fun fromPlainText(plainText: String) = Password(plainText)
    }
}

// Extended User class for CreateUserUseCase
data class User(
    val id: UserId,
    val email: Email,
    val name: String,
    val password: Password
) {
    constructor(id: Long, name: String, email: String) : this(
        UserId(id), 
        Email(email), 
        name, 
        Password.fromPlainText("default")
    )
    
    fun isActive(): Boolean = true
}