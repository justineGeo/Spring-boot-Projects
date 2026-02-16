package com.jso.EmployeeService.Dtos;

import com.jso.EmployeeService.Tables.Employee;
import com.jso.EmployeeService.Tables.EmployeeStatus;

public record EmployeeResponse(
        Long id,
        String fullName,
        String email,
        String departmentName,
        String designationTitle,
        EmployeeStatus status
) {
    // Factory method to map from Entity to Record
    public static EmployeeResponse fromEntity(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getFirstName() + " " + e.getLastName(),
                e.getEmail(),
                e.getDepartment().getName(),
                e.getDesignation().getTitle(),
                e.getStatus()
        );
    }
}