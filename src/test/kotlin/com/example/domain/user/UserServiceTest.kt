package com.example.domain.user

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserServiceTest {

    private lateinit var userService: UserService
    private val mockUserRepository = mockk<UserRepository>()

    @BeforeEach
    fun setUp() {
        userService = UserService()
    }

    @Test
    fun `should validate user with valid name`() {
        val user = User(
            id = 1L,
            name = "John Doe",
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Valid)
    }

    @Test
    fun `should invalidate user with blank name`() {
        val user = User(
            id = 1L,
            name = "",
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Invalid)
        assertEquals(listOf("이름은 비어있을 수 없습니다"), (result as UserValidationResult.Invalid).errors)
    }

    @Test
    fun `should invalidate user with name too short`() {
        val user = User(
            id = 1L,
            name = "A",
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Invalid)
        assertEquals(listOf("이름은 최소 2자 이상이어야 합니다"), (result as UserValidationResult.Invalid).errors)
    }

    @Test
    fun `should invalidate user with name too long`() {
        val user = User(
            id = 1L,
            name = "A".repeat(51),
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Invalid)
        assertEquals(listOf("이름은 50자를 초과할 수 없습니다"), (result as UserValidationResult.Invalid).errors)
    }

    @Test
    fun `should invalidate user with special characters in name`() {
        val user = User(
            id = 1L,
            name = "John@Doe",
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Invalid)
        assertEquals(listOf("이름에는 특수문자를 포함할 수 없습니다"), (result as UserValidationResult.Invalid).errors)
    }

    @Test
    fun `should allow space in name`() {
        val user = User(
            id = 1L,
            name = "John Doe",
            email = "john@example.com"
        )

        val result = userService.validateUser(user)

        assertTrue(result is UserValidationResult.Valid)
    }

    @Test
    fun `should detect related users with same email domain`() {
        val user1 = User(1L, "John", "john@company.com")
        val user2 = User(2L, "Jane", "jane@company.com")

        val result = userService.areRelatedUsers(user1, user2)

        assertTrue(result)
    }

    @Test
    fun `should not detect related users with different email domains`() {
        val user1 = User(1L, "John", "john@company1.com")
        val user2 = User(2L, "Jane", "jane@company2.com")

        val result = userService.areRelatedUsers(user1, user2)

        assertFalse(result)
    }

    @Test
    fun `should lock account after 5 failed login attempts`() {
        val user = User(1L, "John", "john@example.com")

        val result = userService.shouldLockAccount(user, 5)

        assertTrue(result)
    }

    @Test
    fun `should not lock account with less than 5 failed attempts for active user`() {
        val user = User(1L, "John", "john@example.com")

        val result = userService.shouldLockAccount(user, 3)

        assertFalse(result)
    }
}