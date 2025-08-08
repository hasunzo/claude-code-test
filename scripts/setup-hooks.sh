#!/bin/bash

# Claude Code Pre-commit Hook 설정 스크립트

echo "🚀 Claude Code Pre-commit Hook 설정 중..."

# Git hooks 디렉토리 확인
if [ ! -d ".git/hooks" ]; then
    echo "❌ Git 레포지토리가 아닙니다."
    exit 1
fi

# Pre-commit hook 생성
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash

echo "🔍 Claude Code Review 실행 중..."

# Staged 파일 확인
staged_files=$(git diff --cached --name-only --diff-filter=ACM | grep -E '\.(kt|java|js|ts)$')

if [ -z "$staged_files" ]; then
    echo "✅ 리뷰할 코드 파일이 없습니다."
    exit 0
fi

echo "📝 변경된 파일들:"
echo "$staged_files"

# Claude Code 설치 확인
if ! command -v claude &> /dev/null; then
    echo "⚠️  Claude Code가 설치되지 않았습니다."
    echo "   npm install -g @anthropic-ai/claude-code"
    echo "   커밋을 계속 진행합니다..."
    exit 0
fi

# API 키 확인
if [ -z "$ANTHROPIC_API_KEY" ]; then
    echo "⚠️  ANTHROPIC_API_KEY가 설정되지 않았습니다."
    echo "   export ANTHROPIC_API_KEY=your-api-key"
    echo "   커밋을 계속 진행합니다..."
    exit 0
fi

# 임시 파일로 결과 저장
temp_dir=$(mktemp -d)
review_file="$temp_dir/review.txt"
security_file="$temp_dir/security.txt"

# Claude Code 실행
echo "🔍 보안 검사 실행 중..."
claude check-security > "$security_file" 2>&1

# 보안 이슈 확인
if grep -q "Critical\|🚨" "$security_file"; then
    echo ""
    echo "🚨 보안 취약점이 발견되었습니다!"
    echo "================================"
    cat "$security_file"
    echo "================================"
    echo ""
    read -p "계속 커밋하시겠습니까? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "❌ 커밋이 취소되었습니다."
        rm -rf "$temp_dir"
        exit 1
    fi
fi

echo "✅ Pre-commit 검사 완료!"
rm -rf "$temp_dir"
exit 0
EOF

# 실행 권한 부여
chmod +x .git/hooks/pre-commit

echo "✅ Pre-commit hook이 설정되었습니다!"
echo ""
echo "🎯 이제 다음과 같이 동작합니다:"
echo "   1. git commit 실행 시 자동으로 Claude 보안 검사"
echo "   2. 보안 취약점 발견 시 커밋 중단 옵션 제공"
echo "   3. GitHub Actions에서 PR 리뷰 자동화"
echo ""
echo "📋 테스트 방법:"
echo "   git add ."
echo "   git commit -m 'test: pre-commit hook'"
