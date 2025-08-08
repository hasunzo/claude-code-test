# Analyze Code Command

**프로젝트 전체의 코드 품질을 분석합니다**

## 🎯 분석 목표
- **아키텍처 준수**: DDD + Hexagonal Architecture 원칙
- **코드 품질**: Clean Code, SOLID 원칙
- **성능**: 잠재적 성능 이슈 식별
- **유지보수성**: 코드 복잡도, 중복 코드

## 🔍 분석 영역

### 🏗️ Architecture Analysis
#### Layer Separation
- **Domain Layer**: 순수 비즈니스 로직, 외부 의존성 없음
- **Application Layer**: Use Case 구현, 도메인 조합
- **API Layer**: 외부 인터페이스, DTO 변환
- **Infrastructure Layer**: 외부 시스템 연동

#### Dependency Direction
```
✅ 올바른 의존성 방향
API → Application → Domain ← Infrastructure

❌ 잘못된 의존성 방향  
Domain → Infrastructure
API → Infrastructure (직접 호출)
```

### 💡 Code Quality Metrics

#### SOLID Principles
- **SRP**: 단일 책임 원칙
- **OCP**: 개방-폐쇄 원칙  
- **LSP**: 리스코프 치환 원칙
- **ISP**: 인터페이스 분리 원칙
- **DIP**: 의존성 역전 원칙

#### Clean Code Practices
- **의미있는 이름**: 클래스, 메서드, 변수명
- **함수 크기**: 작고 단순한 함수
- **주석**: 코드로 표현할 수 없는 의도만
- **포맷팅**: 일관된 코딩 스타일

### ⚡ Performance Analysis
- **N+1 쿼리**: JPA/Hibernate 성능 이슈
- **메모리 누수**: 리소스 해제 누락
- **불필요한 객체 생성**: 성능 영향
- **알고리즘 복잡도**: 비효율적인 로직

### 🔧 Maintainability Metrics
- **순환 복잡도**: 복잡한 조건문, 반복문
- **코드 중복**: DRY 원칙 위반
- **클래스 응집도**: 관련 없는 메서드들
- **결합도**: 과도한 의존성

## 📊 분석 결과 예시

```
🏗️ ARCHITECTURE ANALYSIS

✅ GOOD PRACTICES
- Clean한 레이어 분리 구조
- 도메인 중심 패키지 구조  
- 포트-어댑터 패턴 올바른 구현
- UseCase별 명확한 책임 분리

⚠️ ARCHITECTURE ISSUES
[UserController.kt:15] API Layer에서 Repository 직접 호출
→ 수정: UserFacade를 통해 UseCase 호출하도록 변경

[OrderService.kt:23] Domain Layer에서 JPA 어노테이션 사용  
→ 수정: Infrastructure Layer로 이동 필요

💡 CODE QUALITY ANALYSIS

SOLID 원칙 준수도: 75/100
- SRP 위반: OrderController가 너무 많은 책임 
- DIP 준수: 인터페이스 기반 설계 잘됨
- OCP 개선: 전략 패턴 적용 고려

Clean Code 점수: 82/100  
- 메서드명이 명확하고 직관적
- 함수 크기 적절 (평균 15줄)
- 주석 최소화 (코드로 의도 표현)

⚡ PERFORMANCE ISSUES

[UserService.kt:45] N+1 쿼리 가능성
users.forEach { user -> 
    val orders = orderRepository.findByUserId(user.id) // ❌
}
→ 수정: @EntityGraph 또는 Fetch Join 사용

[ProductController.kt:12] 불필요한 객체 생성
repeat(1000) { 
    val dto = ProductDto() // ❌ 반복 생성
}
→ 수정: 객체 재사용 또는 팩토리 패턴

🔧 MAINTAINABILITY METRICS

순환 복잡도: 평균 3.2 (양호)
- 복잡한 메서드: UserValidator.validate() (복잡도: 8)

코드 중복률: 12%  
- PaymentService와 RefundService 중복 로직
→ 공통 AbstractPaymentService 추출 권장

클래스 응집도: 85% (우수)
- 대부분 클래스가 단일 관심사 담당

결합도: 보통
- OrderService가 7개 의존성 주입
→ 일부 의존성 분리 고려
```

## 📋 상세 체크리스트

### Architecture Checklist
- [ ] 레이어별 패키지 구조 준수
- [ ] 도메인 객체의 외부 의존성 부재
- [ ] 인터페이스 기반 의존성 주입
- [ ] 횡단 관심사 분리 (AOP 활용)

### Code Quality Checklist  
- [ ] 클래스/메서드 크기 적절
- [ ] 매직 넘버/문자열 상수화
- [ ] 예외 처리 적절성
- [ ] 리소스 해제 (try-with-resources)

### Performance Checklist
- [ ] 데이터베이스 쿼리 최적화
- [ ] 캐싱 전략 적용
- [ ] 비동기 처리 활용
- [ ] 메모리 사용량 최적화

### Maintainability Checklist
- [ ] 테스트 코드 작성률
- [ ] 문서화 수준
- [ ] 코드 리뷰 프로세스
- [ ] CI/CD 파이프라인

## 🎯 개선 제안

### 우선순위 1 (즉시 수정)
1. 아키텍처 위반 사항 수정
2. 성능에 직접적 영향 주는 이슈
3. 보안과 연관된 코드 품질 문제

### 우선순위 2 (단기 개선)  
1. 코드 중복 제거
2. 복잡도 높은 메서드 리팩토링
3. 테스트 커버리지 향상

### 우선순위 3 (장기 개선)
1. 성능 최적화
2. 코드 문서화
3. 모니터링 및 알람 개선

## 🚀 사용법

```bash
# 전체 코드 분석
claude analyze-code

# 특정 영역만 분석
claude analyze-code --focus architecture
claude analyze-code --focus quality  
claude analyze-code --focus performance

# 특정 패키지만 분석
claude analyze-code --package src/domain/user
```
