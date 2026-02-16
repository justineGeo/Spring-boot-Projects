package com.jso.IamService_payRoll.Services;

import com.jso.IamService_payRoll.Dtos.*;
import com.jso.IamService_payRoll.JDBC.OrganizationRepository;
import com.jso.IamService_payRoll.Tables.AccountStatus;
import com.jso.IamService_payRoll.Tables.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    @Transactional
    public OrganizationResponse create(OrganizationRequest request) {
        if(repository.existsByCode(request.code())) throw new RuntimeException("Code already exists");

        Organization org = Organization.builder()
                .name(request.name())
                .code(request.code().toUpperCase())
                .tinNumber(request.tinNumber())
                .vrnNumber(request.vrnNumber())
                .nssfNumber(request.nssfNumber())
                .contactEmail(request.contactEmail())
                .contactPhone(request.contactPhone())
                .status(request.status() != null ? request.status() : AccountStatus.ACTIVE)
                .build();

        return mapToResponse(repository.save(org));
    }

    public List<OrganizationResponse> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrganizationResponse getById(Long id) {
        Organization org = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return mapToResponse(org);
    }

    // This is the endpoint the Employee Service's HttpClient will call!
    public boolean validateExists(Long id) {
        return repository.existsById(id);
    }

    private OrganizationResponse mapToResponse(Organization org) {
        return new OrganizationResponse(
                org.getId(), org.getName(), org.getCode(),
                org.getTinNumber(), org.getVrnNumber(), org.getNssfNumber(),
                org.getContactEmail(), org.getContactPhone(),
                org.getStatus(), org.isActive()
        );
    }
}