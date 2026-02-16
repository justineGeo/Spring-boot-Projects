package com.jso.EmployeeService.Services;

import com.jso.EmployeeService.Dtos.*;
import com.jso.EmployeeService.Tables.Department;
import com.jso.EmployeeService.jdbc.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        // 1. Validate Organization exists in IAM Service
        validateOrganizationInIam(request.organizationId());

        // 2. Check for local duplicates
        if (repository.existsByNameAndOrganizationId(request.name(), request.organizationId())) {
            throw new RuntimeException("Department already exists in this organization");
        }

        Department dept = Department.builder()
                .name(request.name())
                .description(request.description())
                .organizationId(request.organizationId())
                .build();

        return mapToResponse(repository.save(dept));
    }

    public List<DepartmentResponse> getByOrganization(Long orgId) {
        return repository.findByOrganizationId(orgId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    private void validateOrganizationInIam(Long orgId) {
        try {
            HttpRequest iamReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7000/auth/organizations/validate/" + orgId))
                    .GET()
                    .build();

            HttpResponse<Void> response = httpClient.send(iamReq, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Invalid Organization: ID " + orgId + " not found in IAM.");
            }
        } catch (Exception e) {
            throw new RuntimeException("IAM Service Unavailable: " + e.getMessage());
        }
    }

    private DepartmentResponse mapToResponse(Department dept) {
        return new DepartmentResponse(dept.getId(), dept.getName(), dept.getDescription(), dept.getOrganizationId());
    }
}