package com.example.banking.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class AccountController {
    @GetMapping("/account/details")
    public Map<String, Object> accountDetails(Authentication authentication) {
        return Map.of(
                "message", "Authenticated access granted",
                "accountHolder", authentication.getName(),
                "accountNumber", "XXXX-XXXX-1234",
                "accountType", "Savings",
                "currency", "INR",
                "availableBalance", new BigDecimal("25000.00"),
                "note", "Demo data only; not connected to a real bank"
        );
    }
}
