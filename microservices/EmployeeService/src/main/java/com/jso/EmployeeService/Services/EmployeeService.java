package com.jso.EmployeeService.Services;

import com.jso.EmployeeService.Dtos.EmployeeCreateRequest;
import com.jso.EmployeeService.Dtos.EmployeeResponse;
import com.jso.EmployeeService.Tables.*;
import com.jso.EmployeeService.jdbc.DepartmentRepository;
import com.jso.EmployeeService.jdbc.DesignationRepository;
import com.jso.EmployeeService.jdbc.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    // --- CREATE (With Child Entities) ---
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        log.info("Provisioning new employee: {} {}", request.firstName(), request.lastName());

        var dept = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department ID not found"));
        var desig = designationRepository.findById(request.designationId())
                .orElseThrow(() -> new EntityNotFoundException("Designation ID not found"));

        Employee employee = Employee.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .employeeCode(request.employeeCode())
                .organizationId(1L) // In prod, pull from RequestContext.getOrgId()
                .userId(0L)
                .department(dept)
                .designation(desig)
                .status(EmployeeStatus.PROBATION) // Initial state
                .build();

        // One-to-One Relationships
        employee.setEmploymentDetail(EmploymentDetail.builder()
                .joiningDate(request.joiningDate())
                .basicSalary(request.basicSalary())
                .employee(employee)
                .build());

        employee.setTaxDetail(TaxDetail.builder()
                .taxNumber(request.taxNumber())
                .employee(employee)
                .build());

        return EmployeeResponse.fromEntity(employeeRepository.save(employee));
    }

    // --- READ (By Status - Highly Recommended) ---
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status) {
        log.debug("Filtering employees by status: {}", status);
        return employeeRepository.findByStatus(status).stream()
                .map(EmployeeResponse::fromEntity)
                .toList();
    }

    // --- READ (Get All) ---
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::fromEntity)
                .toList();
    }

    // --- UPDATE (Full Profile) ---
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeCreateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        // Note: We usually don't allow updating employeeCode or joiningDate after creation

        return EmployeeResponse.fromEntity(employeeRepository.save(employee));
    }

    // --- UPDATE (Status Change - State Machine) ---
    @Transactional
    public void changeStatus(Long id, EmployeeStatus newStatus) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee record missing"));

        log.info("Transitioning employee {} from {} to {}", id, employee.getStatus(), newStatus);
        employee.setStatus(newStatus);
        // Save is automatic at end of @Transactional due to dirty checking
    }

    // --- DELETE (Soft Delete - The Industry Standard) ---
    @Transactional
    public void terminateEmployee(Long id) {
        log.warn("Terminating employee ID: {}. Preserving records for audit.", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setStatus(EmployeeStatus.TERMINATED);
        employeeRepository.save(employee);
    }
}