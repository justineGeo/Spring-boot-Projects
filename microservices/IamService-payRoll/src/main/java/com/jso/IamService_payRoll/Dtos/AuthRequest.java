package com.jso.IamService_payRoll.Dtos;

public record AuthRequest(
        String email,
        String password
) {}