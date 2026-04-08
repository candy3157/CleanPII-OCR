# OCR MVP Design

## 1. 목표

요구사항 명세서 기준 MVP 범위는 아래 흐름을 실증 가능하게 만드는 것이다.

1. 사용자가 로그인한다.
2. 문서를 업로드한다.
3. 시스템이 비동기 처리 상태를 관리한다.
4. OCR 결과와 개인정보 탐지/마스킹 결과를 조회한다.
5. 검수자가 수정 후 확정 또는 반려한다.
6. 관리자가 정책, 이력, 감사 로그를 확인한다.

## 2. 권장 아키텍처

### 현재 개발 단계

- Frontend: React + Vite
- Backend API: Spring Boot
- Storage: 로컬 파일 스토리지
- Persistence: PostgreSQL
- Async processing: Spring 비동기/작업 테이블 기반
- OCR: Tesseract 또는 외부 OCR API
- PII detection: 정규식 기반

### 추후 확장 구조

- Frontend
- Backend API
- Worker
- Queue
- PostgreSQL
- Object Storage
- Monitoring stack

초기 버전은 Worker/Queue/Object Storage를 인터페이스로 분리해두고 실제 구현은 단일 Spring Boot 안에서 시작하는 편이 가장 현실적이다.

## 3. 화면 구조

### 공통

- 상단 앱 바
- 좌측 운영 메뉴
- 역할 표시
- 상태 배지

### 화면 목록

1. 로그인 화면
2. 대시보드
3. 문서 목록
4. 문서 상세 / 검수
5. 정책 관리
6. 이력 / 감사 로그

## 4. 백엔드 도메인

### 인증/권한

- User
- Role: `ADMIN`, `REVIEWER`, `VIEWER`
- Session

### 문서 처리

- Document
- OCRResult
- PIIItem
- Review
- ProcessingEvent

### 운영 관리

- Policy
- AuditLog

## 5. API 초안

### Auth

- `POST /api/auth/login`
- `GET /api/auth/me`
- `POST /api/auth/logout`

### Dashboard

- `GET /api/dashboard/summary`

응답 예시:

```json
{
  "totalDocuments": 128,
  "failedDocuments": 4,
  "pendingReviews": 11,
  "dailyProcessed": [
    { "date": "2026-04-05", "count": 14 }
  ],
  "detectionCounts": [
    { "type": "PHONE", "count": 24 }
  ]
}
```

### Documents

- `GET /api/documents`
- `GET /api/documents/{documentId}`
- `POST /api/documents`
- `POST /api/documents/{documentId}/reprocess`

### Reviews

- `POST /api/documents/{documentId}/reviews/approve`
- `POST /api/documents/{documentId}/reviews/reject`
- `POST /api/documents/{documentId}/reviews/items`

### Policies

- `GET /api/policies`
- `PUT /api/policies/{policyId}`

### Audit

- `GET /api/audit-logs`

## 6. DB 스키마 초안

### users

- `id`
- `username`
- `password_hash`
- `display_name`
- `role`
- `active`
- `created_at`

### documents

- `id`
- `original_file_name`
- `stored_path`
- `file_type`
- `file_size`
- `uploaded_by`
- `uploaded_at`
- `status`
- `failure_stage`
- `failure_message`

### ocr_results

- `id`
- `document_id`
- `page_number`
- `extracted_text`
- `bounding_boxes_json`
- `confidence_score`

### pii_items

- `id`
- `document_id`
- `page_number`
- `type`
- `original_value`
- `masked_value`
- `location_json`
- `detection_method`
- `confidence_score`
- `review_status`

### reviews

- `id`
- `document_id`
- `reviewer_id`
- `review_status`
- `memo`
- `rejection_reason`
- `reviewed_at`

### policies

- `id`
- `name`
- `target_type`
- `rule_expression`
- `masking_mode`
- `enabled`
- `version`
- `updated_at`

### audit_logs

- `id`
- `user_id`
- `action_type`
- `target_type`
- `target_id`
- `details`
- `created_at`

## 7. 프론트엔드 구성

### 상태 구분

- `Uploaded`
- `Processing`
- `Completed`
- `Failed`
- `Reviewed`

### 화면별 핵심 컴포넌트

- `DashboardPage`
- `DocumentListPage`
- `ReviewPage`
- `PolicyPage`
- `AuditLogPage`

## 8. 구현 우선순위

### Step 1

- 로그인 mock
- 대시보드 요약 API
- 문서 목록/상세 API
- 검수 확정/반려 API
- 정책 목록 API
- 감사 로그 API

### Step 2

- PostgreSQL 연동
- 파일 업로드 저장
- OCR 연동
- 정규식 탐지/마스킹 엔진

### Step 3

- 백그라운드 Worker 분리
- Queue 도입
- Object Storage 전환
- 알림/통계 확장

## 9. 현재 코드 기준 구현 전략

이번 단계에서는 프론트와 백엔드 모두 요구사항을 반영한 운영 콘솔 골격을 먼저 제공한다.

- Backend: 인메모리 목업 데이터 + 실제 API 경로
- Frontend: 운영 콘솔 형태의 다중 화면 프로토타입
- 차기 단계: DB, 업로드, OCR, 인증 고도화
