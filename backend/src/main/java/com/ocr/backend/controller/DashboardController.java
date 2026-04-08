package com.ocr.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocr.backend.dto.DashboardDtos.SummaryResponse;
import com.ocr.backend.service.MockPlatformService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final MockPlatformService platformService;

    public DashboardController(MockPlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/summary")
    public SummaryResponse summary() {
        return platformService.getSummary();
    }
}

