# Create PR Command

**Automatically create PR with description based on git changes**

## 📋 Command Overview
Analyze current branch changes and generate Korean PR title and description automatically.

## 🔍 Analysis Steps

### 1. Git Changes Analysis
- Check modified files list
- Analyze commit messages
- Summarize code changes
- Identify feature additions/bug fixes

### 2. PR Description Generation
- Create comprehensive summary in Korean
- List major changes by file
- Provide testing guidelines
- Highlight review points

## 📤 Output Format (Korean)

Generate PR description in Korean following this structure:

```markdown
## 📋 요약
변경사항에 대한 간단한 요약을 한글로 작성

## 🔧 주요 변경사항
- 파일별 변경 내용을 한글로 설명
- 새로 추가된 기능들
- 수정된 로직 설명

## 🧪 테스트 방법
- 테스트 절차를 한글로 안내
- 확인해야 할 사항들
- 예상 결과

## 📝 리뷰 포인트
- 리뷰어가 중점적으로 확인해야 할 부분
- 특별한 주의사항이나 고려사항
- 아키텍처나 보안 관련 중요 사항
```

## 🎯 Usage
```bash
claude create-pr
```

## 📋 Requirements
- Must output all content in Korean
- Include commit message analysis
- Provide actionable review guidelines
- Focus on security and architecture aspects if applicable
