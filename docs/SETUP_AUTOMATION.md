# Claude Code Review 자동화 설정 가이드

## 🔑 **GitHub Secrets 설정**

### 1. GitHub 레포지토리 Settings 이동
- Repository → Settings → Secrets and variables → Actions

### 2. ANTHROPIC_API_KEY Secret 추가
- **Name**: `ANTHROPIC_API_KEY`
- **Value**: `your-anthropic-api-key`

## 🚀 **자동화 동작 방식**

### PR 생성/업데이트 시 자동 실행
```yaml
on:
  pull_request:
    types: [opened, synchronize]
    branches: [main]
```

### 실행되는 Claude 명령어들
1. `claude review-pr` - PR 변경사항 리뷰
2. `claude check-security` - 보안 취약점 검사

### 결과 출력
- **PR 댓글**로 리뷰 결과 자동 게시
- **GitHub Actions 로그**에서 상세 결과 확인

## 📋 **테스트 방법**

### 1. 새 PR 생성
```bash
# 새 브랜치 생성
git checkout -b feature/test-automation

# 파일 수정 (예: 보안 취약점 추가)
echo "const apiKey = 'secret-key'" > test-security.js

# 커밋 & 푸시
git add .
git commit -m "test: 보안 취약점 테스트"
git push origin feature/test-automation
```

### 2. GitHub에서 PR 생성
- **base**: main
- **compare**: feature/test-automation

### 3. 자동 리뷰 확인
- **Actions 탭**에서 워크플로우 실행 확인
- **PR 댓글**에서 Claude 리뷰 결과 확인

## 🔧 **고급 설정**

### 다른 브랜치도 리뷰하려면
```yaml
on:
  pull_request:
    branches: [main, develop, staging]
```

### 특정 파일만 리뷰하려면
```yaml
on:
  pull_request:
    paths:
      - 'src/**/*.kt'
      - 'src/**/*.java'
```

### 리뷰 조건 추가
```yaml
- name: Check if review needed
  run: |
    if [[ "${{ github.event.pull_request.changed_files }}" -gt 50 ]]; then
      echo "Too many files changed, skipping automated review"
      exit 0
    fi
```

## 🎯 **예상 결과**

PR 생성 시 다음과 같은 댓글이 자동으로 추가됩니다:

```
🤖 Claude Code Review

📊 PR Review
🚨 CRITICAL Issues
- [Line 12] 하드코딩된 API 키 발견
- [Line 25] SQL 인젝션 취약점

⚠️ HIGH Priority Issues  
- [Line 8] 아키텍처 계층 위반
- [Line 15] 입력 검증 부재

🛡️ Security Check
Critical Security Vulnerabilities Found
1. Hardcoded API Key (Line 12)
2. SQL Injection Risk (Line 25)
```

## 🚨 **주의사항**

### API 사용량 관리
- 큰 PR에서는 토큰 사용량이 많을 수 있음
- 필요시 파일 크기나 개수 제한 설정

### 민감 정보 보호  
- API 키는 반드시 GitHub Secrets 사용
- 코드에 하드코딩 금지

### 성능 최적화
- 너무 자주 실행되지 않도록 트리거 조정
- 변경된 파일만 분석하도록 설정
