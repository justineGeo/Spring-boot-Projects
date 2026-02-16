package com.jso.IamService_payRoll.Dtos;

import com.jso.IamService_payRoll.Tables.AccountStatus;

// --- READ RESPONSE ---
public record OrganizationResponse(
        Long id,
        String name,
        String code,
        String tinNumber,
        String vrnNumber,
        String nssfNumber,
        String contactEmail,
        String contactPhone,
        AccountStatus status,
        boolean isActive
) {}