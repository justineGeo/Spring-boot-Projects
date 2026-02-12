package com.jso.IamService_payRoll.Dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthResponse(
        String token,
        String email,
        String organizationCode,
        List<String> roles,
        List<String> permissions
) {}