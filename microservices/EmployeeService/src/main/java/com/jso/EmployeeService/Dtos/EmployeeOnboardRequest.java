package com.jso.EmployeeService.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeOnboardRequest(
        // Identity
        String firstName, String lastName, String email,
        String employeeCode, Long userId, Long departmentId,

        // Tax (Your specific requirement)
        String taxNumber, String socialSecurityNumber,

        // Bank & Job
        String bankName, String accountNumber,
        BigDecimal basicSalary, LocalDate joiningDate
) {}