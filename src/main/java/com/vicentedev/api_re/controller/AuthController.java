package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.LoginRequest;
import com.vicentedev.api_re.dto.request.RegisterRequest;
import com.vicentedev.api_re.dto.request.Verify2FaRequest;
import com.vicentedev.api_re.dto.response.AuthResponse;
import com.vicentedev.api_re.dto.response.LoginStepResponse;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginStepResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginStepResponse response = authService.initiateLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthResponse> verify2Fa(@Valid @RequestBody Verify2FaRequest request) {
        AuthResponse response = authService.verify2Fa(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse response = authService.getCurrentUser(user);
        return ResponseEntity.ok(response);
    }
}
