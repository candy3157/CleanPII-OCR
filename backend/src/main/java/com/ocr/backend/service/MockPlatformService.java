package com.ocr.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocr.backend.domain.DocumentStatus;
import com.ocr.backend.domain.UserRole;
import com.ocr.backend.dto.AuditDtos.AuditLogListResponse;
import com.ocr.backend.dto.AuditDtos.AuditLogResponse;
import com.ocr.backend.dto.AuthDtos.LoginRequest;
import com.ocr.backend.dto.AuthDtos.LoginResponse;
import com.ocr.backend.dto.AuthDtos.UserProfile;
import com.ocr.backend.dto.DashboardDtos.DetectionCount;
import com.ocr.backend.dto.DashboardDtos.SummaryResponse;
import com.ocr.backend.dto.DashboardDtos.TrendPoint;
import com.ocr.backend.dto.DocumentDtos.ActionResponse;
import com.ocr.backend.dto.DocumentDtos.DocumentDetailResponse;
import com.ocr.backend.dto.DocumentDtos.DocumentListResponse;
import com.ocr.backend.dto.DocumentDtos.DocumentSummaryResponse;
import com.ocr.backend.dto.DocumentDtos.OcrPage;
import com.ocr.backend.dto.DocumentDtos.PiiItem;
import com.ocr.backend.dto.DocumentDtos.ReviewInfo;
import com.ocr.backend.dto.PolicyDtos.PolicyListResponse;
import com.ocr.backend.dto.PolicyDtos.PolicyResponse;

@Service
public class MockPlatformService {

    public LoginResponse login(LoginRequest request) {
        var user = switch (request.username()) {
            case "admin" -> new UserProfile("admin", "운영 관리자", UserRole.ADMIN);
            case "reviewer" -> new UserProfile("reviewer", "검수 담당자", UserRole.REVIEWER);
            default -> new UserProfile("viewer", "조회 사용자", UserRole.VIEWER);
        };

        return new LoginResponse(true, user, "Mock login succeeded");
    }

    public UserProfile currentUser() {
        return new UserProfile("admin", "운영 관리자", UserRole.ADMIN);
    }

    public SummaryResponse getSummary() {
        return new SummaryResponse(
                128,
                4,
                11,
                List.of(
                        new TrendPoint("2026-04-04", 12),
                        new TrendPoint("2026-04-05", 18),
                        new TrendPoint("2026-04-06", 21),
                        new TrendPoint("2026-04-07", 15),
                        new TrendPoint("2026-04-08", 24)),
                List.of(
                        new DetectionCount("PHONE", 42),
                        new DetectionCount("EMAIL", 23),
                        new DetectionCount("RRN", 11),
                        new DetectionCount("ADDRESS", 8)));
    }

    public DocumentListResponse getDocuments() {
        return new DocumentListResponse(List.of(
                new DocumentSummaryResponse(
                        "DOC-20260408-001",
                        "고객신청서_샘플.pdf",
                        "PDF",
                        2_104_554L,
                        "kim.admin",
                        "2026-04-08 09:12",
                        DocumentStatus.REVIEWED,
                        6),
                new DocumentSummaryResponse(
                        "DOC-20260408-002",
                        "주민등록등본.png",
                        "PNG",
                        894_200L,
                        "lee.reviewer",
                        "2026-04-08 10:03",
                        DocumentStatus.COMPLETED,
                        4),
                new DocumentSummaryResponse(
                        "DOC-20260408-003",
                        "보험청구서.jpg",
                        "JPG",
                        556_004L,
                        "park.viewer",
                        "2026-04-08 10:14",
                        DocumentStatus.PROCESSING,
                        0),
                new DocumentSummaryResponse(
                        "DOC-20260408-004",
                        "개인정보신청양식.pdf",
                        "PDF",
                        1_329_440L,
                        "kim.admin",
                        "2026-04-08 10:40",
                        DocumentStatus.FAILED,
                        0)));
    }

    public DocumentDetailResponse getDocument(String documentId) {
        return new DocumentDetailResponse(
                documentId,
                "고객신청서_샘플.pdf",
                DocumentStatus.COMPLETED,
                "kim.admin",
                "2026-04-08 09:12",
                "/mock-storage/documents/" + documentId,
                List.of(
                        new OcrPage(1, "홍길동 010-1234-5678 hong@example.com 서울시 강남구 ...", 0.97),
                        new OcrPage(2, "주민등록번호 900101-1234567 보험금 청구 관련 내용", 0.95)),
                List.of(
                        new PiiItem("PII-001", "PHONE", "010-1234-5678", "010-****-5678", "REGEX", 0.99, "page=1,x=112,y=240"),
                        new PiiItem("PII-002", "EMAIL", "hong@example.com", "h***@example.com", "REGEX", 0.96, "page=1,x=298,y=240"),
                        new PiiItem("PII-003", "RRN", "900101-1234567", "900101-1******", "REGEX", 0.98, "page=2,x=96,y=112")),
                new ReviewInfo("PENDING", "lee.reviewer", "전화번호와 이메일 마스킹 확인 필요", ""));
    }

    public ActionResponse approveDocument(String documentId) {
        return new ActionResponse(true, documentId + " reviewed and approved");
    }

    public ActionResponse rejectDocument(String documentId) {
        return new ActionResponse(true, documentId + " rejected with reviewer feedback");
    }

    public ActionResponse reprocessDocument(String documentId) {
        return new ActionResponse(true, documentId + " queued for reprocessing");
    }

    public PolicyListResponse getPolicies() {
        return new PolicyListResponse(List.of(
                new PolicyResponse("POL-001", "전화번호 기본 정책", "PHONE", "(01[0-9])-?([0-9]{3,4})-?([0-9]{4})", "PARTIAL", true, 3, "2026-04-07 11:12"),
                new PolicyResponse("POL-002", "이메일 기본 정책", "EMAIL", "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+", "PARTIAL", true, 2, "2026-04-06 16:22"),
                new PolicyResponse("POL-003", "주민번호 정책", "RRN", "[0-9]{6}-[1-4][0-9]{6}", "PARTIAL", true, 5, "2026-04-08 09:48")));
    }

    public AuditLogListResponse getAuditLogs() {
        return new AuditLogListResponse(List.of(
                new AuditLogResponse("AUD-001", "admin", "LOGIN", "SESSION", "S-001", "관리자 로그인 성공", "2026-04-08 08:55"),
                new AuditLogResponse("AUD-002", "admin", "POLICY_UPDATE", "POLICY", "POL-003", "주민번호 마스킹 규칙 수정", "2026-04-08 09:48"),
                new AuditLogResponse("AUD-003", "reviewer", "REVIEW_APPROVE", "DOCUMENT", "DOC-20260408-001", "검수 확정 처리", "2026-04-08 10:11"),
                new AuditLogResponse("AUD-004", "viewer", "DOWNLOAD", "DOCUMENT", "DOC-20260408-001", "최종 결과 다운로드", "2026-04-08 10:18")));
    }
}

