package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.AuthenticationDtos;
import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.global.SignInDtos;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final LogoutService logoutService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationDtos> register(
            @RequestBody RegisterDtos request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationDtos> login(
            @RequestBody SignInDtos request
    ) {
        return ResponseEntity.ok(service.Login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Logged out successfully");
    }
}