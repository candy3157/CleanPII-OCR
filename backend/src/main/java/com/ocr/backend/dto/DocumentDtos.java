package com.ocr.backend.dto;

import java.util.List;

import com.ocr.backend.domain.DocumentStatus;

public final class DocumentDtos {

    private DocumentDtos() {
    }

    public record DocumentSummaryResponse(
            String id,
            String fileName,
            String fileType,
            long fileSize,
            String uploadedBy,
            String uploadedAt,
            DocumentStatus status,
            int piiCount) {
    }

    public record DocumentListResponse(List<DocumentSummaryResponse> documents) {
    }

    public record DocumentDetailResponse(
            String id,
            String fileName,
            DocumentStatus status,
            String uploadedBy,
            String uploadedAt,
            String previewUrl,
            List<OcrPage> ocrPages,
            List<PiiItem> piiItems,
            ReviewInfo review) {
    }

    public record OcrPage(int pageNumber, String extractedText, double confidence) {
    }

    public record PiiItem(
            String id,
            String type,
            String originalValue,
            String maskedValue,
            String detectionMethod,
            double confidence,
            String location) {
    }

    public record ReviewInfo(String status, String reviewer, String memo, String rejectionReason) {
    }

    public record ActionResponse(boolean success, String message) {
    }
}

