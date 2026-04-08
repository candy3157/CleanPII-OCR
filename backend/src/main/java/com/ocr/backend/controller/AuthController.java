package com.ocr.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocr.backend.dto.AuthDtos.LoginRequest;
import com.ocr.backend.dto.AuthDtos.LoginResponse;
import com.ocr.backend.dto.AuthDtos.UserProfile;
import com.ocr.backend.service.MockPlatformService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MockPlatformService platformService;

    public AuthController(MockPlatformService platformService) {
        this.platformService = platformService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return platformService.login(request);
    }

    @GetMapping("/me")
    public UserProfile me() {
        return platformService.currentUser();
    }
}

