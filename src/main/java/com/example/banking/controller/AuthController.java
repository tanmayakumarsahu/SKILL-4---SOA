package com.example.banking.controller;

import com.example.banking.dto.LoginRequest;
import com.example.banking.dto.LoginResponse;
import com.example.banking.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final JwtService jwtService;
    private final String demoUsername;
    private final String demoPassword;

    public AuthController(
            JwtService jwtService,
            @Value("${app.demo.username:bankuser}") String demoUsername,
            @Value("${app.demo.password:BankPass123!}") String demoPassword) {
        this.jwtService = jwtService;
        this.demoUsername = demoUsername;
        this.demoPassword = demoPassword;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || request.username() == null || request.password() == null
                || !demoUsername.equals(request.username())
                || !demoPassword.equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new LoginResponse("Bearer", token, jwtService.getExpirationSeconds()));
    }
}
