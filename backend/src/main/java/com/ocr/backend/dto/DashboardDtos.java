package com.ocr.backend.dto;

import java.util.List;

public final class DashboardDtos {

    private DashboardDtos() {
    }

    public record SummaryResponse(
            int totalDocuments,
            int failedDocuments,
            int pendingReviews,
            List<TrendPoint> dailyProcessed,
            List<DetectionCount> detectionCounts) {
    }

    public record TrendPoint(String date, int count) {
    }

    public record DetectionCount(String type, int count) {
    }
}

