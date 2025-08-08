# Review PR Command

**Analyze changed files in current PR for DDD+Hexagonal Architecture compliance**

## 📋 Command Overview
이 명령어는 현재 Git에서 변경된 파일들을 DDD+Hexagonal Architecture 기준으로 리뷰합니다.

## 🔍 Analysis Steps

### 1. Git Changes Detection
```bash
# 변경된 파일 목록 확인
git diff --name-only HEAD~1
git diff --cached --name-only  # staged files
git status --porcelain         # working directory changes
```

### 2. File Classification
- **Domain Layer**: `src/domain/**/*.kt`
- **Application Layer**: `src/application/**/*.kt`  
- **API Layer**: `src/api/**/*.kt`
- **Infrastructure Layer**: `src/infrastructure/**/*.kt`

### 3. Review Focus Areas

#### 🚨 Security Analysis
- Authentication and authorization logic
- Sensitive data handling (passwords, tokens, PII)
- Input validation and sanitization
- SQL injection and XSS vulnerabilities

#### 🏗️ Architecture Compliance
- Layer separation and dependency rules
- Domain purity (no infrastructure dependencies)
- Proper use of ports and adapters
- Hexagonal architecture principles

#### 💡 Code Quality
- Kotlin coding conventions
- Naming consistency
- Error handling patterns
- SOLID principles adherence

## 📤 Output Format (한국어 응답 필수)

**중요: 모든 분석 결과는 반드시 한국어로 작성해주세요.**

### Structure
```
🚨 CRITICAL Issues (심각한 문제가 있는 경우)
⚠️ HIGH Priority Issues (높은 우선순위 문제가 있는 경우)  
💡 Suggestions for Improvement (개선 제안사항)
📋 Action Items (실행해야 할 항목들)
✅ Positive Observations (긍정적인 관찰사항)
```

### Example Output (한국어 예시)
```
🚨 CRITICAL (심각한 문제)
- [UserController.kt:15] 직접 Repository 의존성 주입 - 아키텍처 위반
- [AuthService.kt:23] 평문 비밀번호 저장 - 보안 취약점

⚠️ HIGH (높은 우선순위)
- [CreateUserUseCase.kt:12] 예외 처리 누락  
- [UserDto.kt:8] 검증 어노테이션 부재

💡 SUGGESTIONS (개선 제안)
- [User.kt:5] 값 객체(Value Object) 패턴 적용 고려
- [UserService.kt] 메서드 분리로 단일 책임 원칙 강화

📋 ACTION ITEMS (실행 항목)
1. UserController에서 UserFacade 통해 UseCase 호출하도록 수정
2. 비밀번호 해싱 로직 추가 (BCrypt 사용 권장)  
3. CreateUserRequest에 @Valid 어노테이션 추가

✅ POSITIVE (긍정적 측면)
- Clean한 도메인 모델 설계
- 적절한 패키지 구조 사용
```

## 🎯 Usage Examples

### Basic Review
```bash
claude review-pr
```

### With Specific Focus
```bash
claude review-pr --focus security
claude review-pr --focus architecture  
claude review-pr --focus quality
```

### Include Staged Changes
```bash
claude review-pr --staged
```

## ⚙️ Configuration Options

### Severity Levels
- `CRITICAL`: Security vulnerabilities, architecture violations
- `HIGH`: Performance issues, design problems
- `MEDIUM`: Code quality improvements
- `LOW`: Style and convention suggestions

### File Filters
- Include only specific file patterns
- Exclude test files (optional)
- Focus on specific layers

## 🚫 Limitations
- Only analyzes files tracked by Git
- Requires proper Git history for comparison
- Cannot analyze binary files
- Limited to text-based code review (no runtime analysis)
