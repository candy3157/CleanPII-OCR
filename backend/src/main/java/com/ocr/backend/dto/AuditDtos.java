package com.ocr.backend.dto;

import java.util.List;

public final class AuditDtos {

    private AuditDtos() {
    }

    public record AuditLogResponse(
            String id,
            String actor,
            String actionType,
            String targetType,
            String targetId,
            String details,
            String createdAt) {
    }

    public record AuditLogListResponse(List<AuditLogResponse> logs) {
    }
}

