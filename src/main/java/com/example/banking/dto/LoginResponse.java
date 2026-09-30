package com.example.banking.dto;

public record LoginResponse(String tokenType, String accessToken, long expiresInSeconds) {
}
