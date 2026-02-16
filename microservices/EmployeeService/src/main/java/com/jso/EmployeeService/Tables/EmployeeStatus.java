package com.jso.EmployeeService.Tables;

import lombok.Getter;

@Getter
public enum EmployeeStatus {
    PROBATION("Employee is under review"),
    ACTIVE("Employee is currently active"),
    SUSPENDED("Employee is temporarily suspended"),
    TERMINATED("Employee is no longer with the organization"),
    ON_LEAVE("Employee is on an extended leave of absence");

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }
}