# Generate Tests Command

**Automatically generate test code for changed files**

## 📋 Command Overview
Analyze modified files and generate appropriate unit test code with Korean comments and descriptions.

## 🔍 Analysis Steps

### 1. Modified Files Analysis
- Identify newly added methods/classes
- Detect changes in existing code
- Extract logic that requires testing
- Classify by layer (Controller/Service/Domain)

### 2. Test Code Generation
- Generate JUnit 5 based tests
- Use MockK for mocking when needed
- Include boundary value test cases
- Add exception scenario tests
- Follow Korean testing conventions

## 📤 Test Code Patterns

### Controller Test Template
```kotlin
@WebMvcTest(UserController::class)
class UserControllerTest {
    
    @Test
    fun `사용자 조회 성공_유효한_ID가_주어졌을때`() {
        // given: 유효한 사용자 ID
        // when: 사용자 조회 API 호출
        // then: 사용자 정보가 반환됨
    }
    
    @Test
    fun `사용자 조회 실패_존재하지않는_ID가_주어졌을때`() {
        // 예외 케이스 테스트
    }
}
```

### Service Test Template
```kotlin
@ExtendWith(MockKExtension::class)
class UserServiceTest {
    
    @Test
    fun `사용자 생성 성공_모든_필수정보가_제공되었을때`() {
        // given: 필수 사용자 정보
        // when: 사용자 생성 서비스 호출
        // then: 사용자가 성공적으로 생성됨
    }
}
```

### Domain Test Template
```kotlin
class UserTest {
    
    @Test
    fun `사용자 검증 성공_유효한_이메일형식일때`() {
        // 도메인 로직 테스트
    }
}
```

## 🎯 Usage Examples
```bash
claude generate-tests
claude generate-tests --file src/api/user/UserController.kt
claude generate-tests --layer controller
claude generate-tests --focus security
```

## 📋 Requirements
- Generate Korean comments and test names
- Follow Korean testing naming conventions (given/when/then 형태)
- Include comprehensive test scenarios
- Add security-focused tests for controllers
- Generate mock setups with Korean comments
- Provide test execution guidance in Korean

## 🚫 Exclusions
- Skip `.claude/` directory files (commands, agents, etc.)
- Skip documentation files (*.md, *.txt)
- Skip configuration files (*.yml, *.yaml, *.json)
- Skip build files (build.gradle, pom.xml)
- Focus only on source code files that require business logic testing

## 🚨 Test Focus Areas
- **Security**: Input validation, authorization checks
- **Business Logic**: Domain rules, calculation logic  
- **Error Handling**: Exception scenarios, edge cases
- **Integration**: Layer interaction, dependency injection
