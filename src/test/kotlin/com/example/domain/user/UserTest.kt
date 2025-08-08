package com.example.domain.user

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun `사용자 생성 성공_모든_필수정보가_제공되었을때`() {
        // given: 유효한 사용자 정보
        val user = User(
            id = 1L,
            name = "홍길동",
            email = "hong@example.com"
        )
        
        // then: 사용자 객체가 올바르게 생성됨
        assertEquals(1L, user.id)
        assertEquals("홍길동", user.name)
        assertEquals("hong@example.com", user.email)
    }

    @Test
    fun `사용자 검증 성공_유효한_이름과_이메일이_제공되었을때`() {
        // given: 유효한 이름과 이메일을 가진 사용자
        val user = User(
            id = 1L,
            name = "김철수",
            email = "kim@example.com"
        )
        
        // when: 사용자 유효성 검사 수행
        val result = user.isValid()
        
        // then: 검증이 성공함
        assertTrue(result)
    }

    @Test
    fun `사용자 검증 실패_빈_이름이_제공되었을때`() {
        // given: 빈 이름을 가진 사용자
        val user = User(
            id = 1L,
            name = "",
            email = "test@example.com"
        )
        
        // when: 사용자 유효성 검사 수행
        val result = user.isValid()
        
        // then: 검증이 실패함
        assertFalse(result)
    }

    @Test
    fun `사용자 검증 실패_잘못된_이메일형식이_제공되었을때`() {
        // given: @ 기호가 없는 잘못된 이메일 형식
        val user = User(
            id = 1L,
            name = "이영희",
            email = "testexample.com"
        )
        
        // when: 사용자 유효성 검사 수행
        val result = user.isValid()
        
        // then: 검증이 실패함
        assertFalse(result)
    }

    @Test
    fun `사용자 검증 실패_공백으로만_구성된_이름이_제공되었을때`() {
        // given: 공백으로만 구성된 이름을 가진 사용자
        val user = User(
            id = 1L,
            name = "   ",
            email = "test@example.com"
        )
        
        // when: 사용자 유효성 검사 수행
        val result = user.isValid()
        
        // then: 검증이 실패함
        assertFalse(result)
    }
}