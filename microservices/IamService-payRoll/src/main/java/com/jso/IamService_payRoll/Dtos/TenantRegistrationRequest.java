package com.jso.IamService_payRoll.Dtos;

public record TenantRegistrationRequest(
        String companyName,
        String companyCode,
        String tinNumber,
        String nssfNumber,
        String adminFirstName,
        String adminLastName,
        String adminEmail,
        String adminPassword
) {}