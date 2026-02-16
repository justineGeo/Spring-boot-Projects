package com.jso.EmployeeService.jdbc;

import com.jso.EmployeeService.Tables.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByOrganizationId(Long organizationId);
    List<Department> findAllByOrganizationId(Long orgId);
    boolean existsByNameAndOrganizationId(String name, Long organizationId);
}