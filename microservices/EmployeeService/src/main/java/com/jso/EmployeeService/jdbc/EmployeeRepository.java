package com.jso.EmployeeService.jdbc;

import com.jso.EmployeeService.Tables.Employee;
import com.jso.EmployeeService.Tables.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByOrganizationId(Long organizationId);
    Optional<Employee> findByEmployeeCodeAndOrganizationId(String code, Long orgId);
    List<Employee> findByStatus(EmployeeStatus status);
}

