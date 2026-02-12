package com.jso.IamService_payRoll.controller;

import com.jso.IamService_payRoll.Dtos.*;
import com.jso.IamService_payRoll.Services.AuthService;
import com.jso.IamService_payRoll.Tables.RoleDtl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDtl>> getAvailableRoles() {
        return ResponseEntity.ok(authService.getRegistrationRoles());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String token = authService.initiatePasswordReset(email);
        return ResponseEntity.ok(Map.of(
                "message", "Reset link generated.",
                "token", token
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }
}