package com.jso.EmployeeService.Controllers;

import com.jso.EmployeeService.Dtos.EmployeeCreateRequest;
import com.jso.EmployeeService.Dtos.EmployeeResponse;
import com.jso.EmployeeService.Services.EmployeeService;
import com.jso.EmployeeService.Tables.EmployeeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping ("/createEmployee")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeCreateRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        return ResponseEntity.ok(employeeService.getEmployeesByStatus(status));
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeCreateRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam EmployeeStatus newStatus) {
        employeeService.changeStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.terminateEmployee(id);
        return ResponseEntity.noContent().build();
    }
}