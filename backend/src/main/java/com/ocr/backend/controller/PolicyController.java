package com.ocr.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocr.backend.dto.PolicyDtos.PolicyListResponse;
import com.ocr.backend.service.MockPlatformService;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final MockPlatformService platformService;

    public PolicyController(MockPlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public PolicyListResponse list() {
        return platformService.getPolicies();
    }
}

