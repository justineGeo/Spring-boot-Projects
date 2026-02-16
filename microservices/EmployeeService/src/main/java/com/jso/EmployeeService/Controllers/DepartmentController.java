package com.jso.EmployeeService.Controllers;

import com.jso.EmployeeService.Dtos.*;
import com.jso.EmployeeService.Services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping("/createDepartment")
    public ResponseEntity<DepartmentResponse> create(@RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<DepartmentResponse>> getByOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(service.getByOrganization(orgId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}