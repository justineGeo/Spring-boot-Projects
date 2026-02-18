package com.jso.IamService_payRoll.controller;

import com.jso.IamService_payRoll.Dtos.*;
import com.jso.IamService_payRoll.Services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping("/createOrganization")
    public ResponseEntity<OrganizationResponse> create(@RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/getAllOrganization")
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // --- INTER-SERVICE VALIDATION ENDPOINT ---
    @GetMapping("/validate/{id}")
    public ResponseEntity<Void> validate(@PathVariable Long id) {
        if (service.validateExists(id)) {
            return ResponseEntity.ok().build(); // 200 OK
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}