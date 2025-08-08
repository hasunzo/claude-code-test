package com.example.application.user

import com.example.domain.user.User
import com.example.domain.user.UserRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserUseCaseTest {

    private val mockUserRepository = mockk<UserRepository>()
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var getUserByEmailUseCase: GetUserByEmailUseCase

    @BeforeEach
    fun setUp() {
        getUserUseCase = GetUserUseCaseImpl(mockUserRepository)
        getUserByEmailUseCase = GetUserByEmailUseCaseImpl(mockUserRepository)
        clearAllMocks()
    }

    @Test
    fun `should return user when found by id`() = runTest {
        val userId = UserId(1L)
        val expectedUser = User(1L, "John Doe", "john@example.com")
        val query = GetUserQuery(userId)

        coEvery { mockUserRepository.findById(userId) } returns expectedUser

        val result = getUserUseCase.execute(query)

        assertTrue(result is GetUserResult.Found)
        assertEquals(expectedUser, (result as GetUserResult.Found).user)

        coVerify { mockUserRepository.findById(userId) }
    }

    @Test
    fun `should return not found when user does not exist`() = runTest {
        val userId = UserId(999L)
        val query = GetUserQuery(userId)

        coEvery { mockUserRepository.findById(userId) } returns null

        val result = getUserUseCase.execute(query)

        assertTrue(result is GetUserResult.NotFound)

        coVerify { mockUserRepository.findById(userId) }
    }

    @Test
    fun `should return user when found by email`() = runTest {
        val email = Email("john@example.com")
        val expectedUser = User(1L, "John Doe", "john@example.com")
        val query = GetUserByEmailQuery("john@example.com")

        coEvery { mockUserRepository.findByEmail(email) } returns expectedUser

        val result = getUserByEmailUseCase.execute(query)

        assertTrue(result is GetUserByEmailResult.Found)
        assertEquals(expectedUser, (result as GetUserByEmailResult.Found).user)

        coVerify { mockUserRepository.findByEmail(email) }
    }

    @Test
    fun `should return not found when user does not exist by email`() = runTest {
        val email = Email("nonexistent@example.com")
        val query = GetUserByEmailQuery("nonexistent@example.com")

        coEvery { mockUserRepository.findByEmail(email) } returns null

        val result = getUserByEmailUseCase.execute(query)

        assertTrue(result is GetUserByEmailResult.NotFound)

        coVerify { mockUserRepository.findByEmail(email) }
    }

    @Test
    fun `should return invalid email when email format is wrong`() = runTest {
        val query = GetUserByEmailQuery("invalid-email")

        val result = getUserByEmailUseCase.execute(query)

        assertTrue(result is GetUserByEmailResult.InvalidEmail)
        assertTrue((result as GetUserByEmailResult.InvalidEmail).message.isNotEmpty())
    }

    @Test
    fun `should handle empty email gracefully`() = runTest {
        val query = GetUserByEmailQuery("")

        val result = getUserByEmailUseCase.execute(query)

        assertTrue(result is GetUserByEmailResult.InvalidEmail)
    }
}