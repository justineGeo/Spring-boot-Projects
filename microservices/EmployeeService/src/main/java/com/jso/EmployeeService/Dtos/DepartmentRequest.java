package com.jso.EmployeeService.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Used for POST and PUT
public record DepartmentRequest(
        @NotBlank(message = "Department name is required")
        String name,

        String description,

        @NotNull(message = "Organization ID is required")
        Long organizationId
) {}