package com.jso.EmployeeService.Dtos;

// Used for GET responses
public record DepartmentResponse(
        Long id,
        String name,
        String description,
        Long organizationId
) {}