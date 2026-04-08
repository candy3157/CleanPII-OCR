package com.ocr.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocr.backend.dto.AuditDtos.AuditLogListResponse;
import com.ocr.backend.service.MockPlatformService;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final MockPlatformService platformService;

    public AuditLogController(MockPlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public AuditLogListResponse list() {
        return platformService.getAuditLogs();
    }
}

