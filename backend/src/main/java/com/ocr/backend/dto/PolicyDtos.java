package com.ocr.backend.dto;

import java.util.List;

public final class PolicyDtos {

    private PolicyDtos() {
    }

    public record PolicyResponse(
            String id,
            String name,
            String targetType,
            String ruleExpression,
            String maskingMode,
            boolean enabled,
            int version,
            String updatedAt) {
    }

    public record PolicyListResponse(List<PolicyResponse> policies) {
    }
}

