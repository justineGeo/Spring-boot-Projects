package com.jso.EmployeeService.jdbc;

import com.jso.EmployeeService.Tables.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    List<Designation> findByOrganizationId(Long organizationId);
}