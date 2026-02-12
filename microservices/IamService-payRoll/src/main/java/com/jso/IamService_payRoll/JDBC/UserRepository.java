package com.jso.IamService_payRoll.JDBC;

import com.jso.IamService_payRoll.Tables.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTbl, Long> {

    // 1. Standard lookup with EntityGraph to prevent N+1 issues
    @EntityGraph(attributePaths = {"roles", "roles.permissions", "organization"})
    Optional<UserTbl> findByEmail(String email);

    // 2. Strict multi-tenant lookup (Using organization_id)
    Optional<UserTbl> findByEmailAndOrganization_Id(String email, Long organizationId);

    // 3. Password Reset Lookup
    @EntityGraph(attributePaths = {"organization"})
    Optional<UserTbl> findByPasswordResetTokenHash(String tokenHash);

    // 4. Existence check for registration
    boolean existsByEmailAndOrganization_Id(String email, Long organizationId);


}