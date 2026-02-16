package com.jso.IamService_payRoll.Dtos;

import com.jso.IamService_payRoll.Tables.AccountStatus;
import jakarta.validation.constraints.NotBlank;



public record OrganizationRequest(
        @NotBlank(message = "Organization name is required")
        String name,

        @NotBlank(message = "Organization code is required")
        String code,

        @NotBlank(message = "TIN number is required")
        String tinNumber,

        String vrnNumber,
        String nssfNumber,
        String contactEmail,
        String contactPhone,
        AccountStatus status
) {}
