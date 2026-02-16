package com.jso.EmployeeService.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(
        String firstName,
        String lastName,
        String email,
        String employeeCode,
        Long departmentId,
        Long designationId,
        LocalDate joiningDate,
        BigDecimal basicSalary,
        String taxNumber,
        String accountNumber,
        String bankName
) {}