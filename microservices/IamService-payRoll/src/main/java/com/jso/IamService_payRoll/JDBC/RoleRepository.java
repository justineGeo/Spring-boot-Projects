package com.jso.IamService_payRoll.JDBC;

import com.jso.IamService_payRoll.Tables.RoleDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleDtl, Long> {

    // Fetch all roles belonging to a specific company (e.g., NSSF, TRA Admins)
    List<RoleDtl> findByOrganization_Id(Long organizationId);
    Optional<RoleDtl> findByName(String name);

    // Find a specific role by name within a company
    Optional<RoleDtl> findByNameAndOrganization_Id(String name, Long organizationId);
}