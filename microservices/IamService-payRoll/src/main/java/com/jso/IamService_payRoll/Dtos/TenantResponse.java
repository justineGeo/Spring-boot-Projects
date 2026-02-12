package com.jso.IamService_payRoll.Dtos;

public record TenantResponse(
        Long organizationId,
        String companyCode,
        String message
) {}