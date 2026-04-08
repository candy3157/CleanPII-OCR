# OCR Project

React + Spring Boot 기반의 OCR 서비스 초기 구조입니다.

## 구조

- `frontend`: React + Vite
- `backend`: Spring Boot + Gradle
- `doc`: 요구사항 PDF 및 설계 문서

## 권장 방식

- 프론트엔드는 `5173` 포트에서 개발
- 백엔드는 `8080` 포트에서 개발
- 프론트 개발 서버에서 `/api` 요청은 백엔드로 프록시

## 실행

### 1. Frontend

```bash
cd frontend
npm install
npm run dev
```

### 2. Backend

시스템에 Java 17과 Gradle이 설치되어 있다면:

```bash
cd backend
gradle bootRun
```

Gradle wrapper를 쓰고 싶다면 네트워크 가능한 환경에서 한 번 생성하면 됩니다:

```bash
cd backend
gradle wrapper
./gradlew bootRun
```

### 3. 현재 워크스페이스 기준 실행

이 환경에서는 시스템 `java`, `gradle`이 없어서 로컬 툴체인을 `./.tools` 아래에 받아 검증했습니다.

```bash
cd backend
JAVA_HOME=/home/candy3157/workspace/OCR/.tools/jdk-17.0.18+8 \
GRADLE_USER_HOME=/home/candy3157/workspace/OCR/.gradle-home \
./gradlew -Djava.io.tmpdir=/home/candy3157/workspace/OCR/.tmp bootRun
```

## 다음 작업 추천

1. [doc/mvp-design.md](/home/candy3157/workspace/OCR/CleanPII-OCR/doc/mvp-design.md) 기준으로 DB 연동 시작
2. 문서 업로드 API와 실제 파일 저장 구현
3. OCR 엔진 연결 및 정규식 탐지/마스킹 엔진 추가
4. 인증을 mock에서 Spring Security 기반으로 전환
