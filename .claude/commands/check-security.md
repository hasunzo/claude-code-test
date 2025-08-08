# Security Check Command

**프로젝트 전체의 보안 취약점을 검사합니다**

## 🔍 보안 검사 항목

### 🚨 Critical Security Issues
- **하드코딩된 비밀정보**: 비밀번호, API 키, 토큰
- **SQL 인젝션**: 동적 쿼리, 파라미터 바인딩 부재
- **인증 우회**: 인증 로직 누락, 잘못된 권한 검사
- **민감한 데이터 노출**: 로그, 응답에 포함된 개인정보

### ⚠️ High Priority Issues  
- **입력 검증 누락**: 사용자 입력 검증 부재
- **XSS 취약점**: HTML 이스케이프 누락
- **CSRF 보호 부재**: 상태 변경 요청 보호 누락
- **세션 관리**: 부적절한 세션 처리

### 💡 Security Best Practices
- **비밀번호 정책**: 강도, 해싱 알고리즘
- **HTTPS 사용**: 민감한 데이터 전송 시
- **에러 메시지**: 민감한 정보 노출 방지
- **로깅 보안**: 민감한 데이터 로깅 방지

## 🎯 검사 대상 파일 패턴

### 우선순위 높은 파일들
```
src/api/**/*Controller.kt     # API 엔드포인트
src/api/**/*Facade.kt         # API 파사드
src/application/**/*UseCase.kt # 비즈니스 로직
src/domain/**/*Service.kt      # 도메인 서비스
src/infrastructure/**/*.kt     # 외부 연동
```

### 설정 파일들
```
application.yml
application.properties
docker-compose.yml
Dockerfile
```

## 📋 체크리스트

### Authentication & Authorization
- [ ] @PreAuthorize, @Secured 어노테이션 사용
- [ ] JWT 토큰 검증 로직
- [ ] 세션 관리 구현
- [ ] 권한 체크 로직

### Input Validation
- [ ] @Valid, @Validated 어노테이션
- [ ] 커스텀 검증 로직
- [ ] SQL 파라미터 바인딩
- [ ] HTML 이스케이프

### Data Protection  
- [ ] 비밀번호 해싱 (BCrypt, Argon2)
- [ ] 민감한 데이터 암호화
- [ ] 로그에서 민감한 정보 제거
- [ ] 에러 응답에서 내부 정보 숨김

### Configuration Security
- [ ] 환경변수 사용 (하드코딩 금지)
- [ ] 보안 헤더 설정
- [ ] CORS 설정
- [ ] SSL/TLS 설정

## 📤 출력 예시

```
🚨 CRITICAL SECURITY ISSUES

[UserController.kt:23] 하드코딩된 API 키 발견
private val apiKey = "sk-1234567890abcdef" // ❌ 보안 위험

[AuthService.kt:15] 평문 비밀번호 저장
user.password = request.password // ❌ 해싱 필요

[UserRepository.kt:45] SQL 인젝션 위험
query = "SELECT * FROM users WHERE id = " + userId // ❌ 파라미터 바인딩 필요

⚠️ HIGH PRIORITY

[CreateUserRequest.kt:8] 입력 검증 부재
val email: String // ❌ @Email 어노테이션 필요

[UserController.kt:12] CORS 설정 부재
@CrossOrigin("*") // ❌ 와일드카드 사용 위험

💡 SECURITY RECOMMENDATIONS

1. 환경변수 사용: API 키, DB 비밀번호는 환경변수로 관리
2. 비밀번호 해싱: BCryptPasswordEncoder 사용 권장  
3. 입력 검증: Bean Validation 어노테이션 추가
4. 보안 헤더: Spring Security 설정으로 XSS, CSRF 보호

📋 IMMEDIATE ACTIONS REQUIRED

1. 모든 하드코딩된 비밀정보를 환경변수로 이동
2. 비밀번호 해싱 로직 구현 
3. SQL 쿼리를 JPA/MyBatis 파라미터 바인딩으로 변경
4. 모든 API 엔드포인트에 입력 검증 추가
```

## 🔧 사용법

```bash
# 전체 보안 검사
claude check-security

# 특정 디렉토리만 검사  
claude check-security --path src/api

# 특정 보안 이슈만 검사
claude check-security --focus hardcoded-secrets
claude check-security --focus sql-injection
claude check-security --focus input-validation
```

## ⚡ 추가 기능

### 보안 점수 매기기
- **Critical**: 즉시 수정 필요 (점수: 0-30)
- **High**: 우선 수정 필요 (점수: 31-60) 
- **Medium**: 개선 권장 (점수: 61-80)
- **Good**: 양호 (점수: 81-100)

### 보안 리포트 생성
- 발견된 취약점 요약
- 수정 우선순위 가이드
- 보안 모범 사례 제안
