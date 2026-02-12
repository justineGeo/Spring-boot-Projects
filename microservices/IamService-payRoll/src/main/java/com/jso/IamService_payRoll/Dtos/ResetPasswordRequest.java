package com.jso.IamService_payRoll.Dtos;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {}