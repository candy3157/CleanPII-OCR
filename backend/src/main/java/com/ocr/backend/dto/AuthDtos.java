package com.ocr.backend.dto;

import com.ocr.backend.domain.UserRole;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(String username, String password) {
    }

    public record UserProfile(String username, String displayName, UserRole role) {
    }

    public record LoginResponse(boolean success, UserProfile user, String message) {
    }
}

